import java.util.*;
import java.util.stream.Collectors;
/**
 * Write a description of class wasd here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TrainStation
{
    private String city;
    private List<Train> trains;
    public TrainStation(String city){
        this.city = city;
        this.trains = new ArrayList<Train>();
    }
    
    public void add(Train t){
        trains.add(t);
    }
    
    public int connectingTrains(){
        int n = 0;
        for (Train t : trains){
            if(t.getDeparture().equals(city) || t.getDestination().equals(city)){
                n++;
            }
        }
        return n;
    }
    
    public Train cheapTrainTo(String destination){
        Train res = null;
        for (Train t : trains){
            if(t.getDestination().equals(destination) && (res == null || res.getPrice() >= t.getPrice())){
                res = t;
            }
        }
        return res;
    }
    
    public void printTrainStation(){
        System.out.println("This station is located in: " + city);
        Collections.sort(trains);
        for(Train t : trains){
            System.out.println(t);
        }
    }
    
    public List<Train> trainsFrom(String departure){
        return trains.stream().filter(t -> t.getDeparture().equals(departure))
                              .filter(t -> t.getDestination().equals(city))
                              .collect(Collectors.toList());
    }
    
    public Optional<Train> cheapTrain(String destination){
        return trains.stream().filter((Train t) -> t.getDeparture().equals(city))
                              .filter((Train t) -> t.getDestination().equals(destination))
                              .min(Comparator.comparing(t -> t.getPrice()));
    }
}
