import java.util.*;
import java.util.Random;
/**
 * Write a description of class Country here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Country
{
    private String name;
    private Map<City,List<Road>> network;
    public Country(String name, Map<City,List<Road>> network){
        this.name = name;
        this.network = network;
    }

    public String getName(){
        return this.name;
    }

    public Map<City,List<Road>> getNetwork(){
        return network;
    }

    public List<Road> getRoads(City c){
        if(network.containsKey(c)){
            return network.get(c);
        }
        List<Road> res = new ArrayList<Road>();
        return res;
    }

    public List<City> getCities(){
        //List<City> res = (ArrayList) network.keySet();
        List<City> res = new ArrayList<>(network.keySet());
        Collections.sort(res);
        return res;
    }

    public City getCity(String name){
        if (getCities().stream().filter( c -> c.getName().equals(name))
                                .findFirst()
                                .isPresent()){
            return getCities().stream().filter( c -> c.getName().equals(name))
            .findFirst()
            .get();
        }
        return null;
    }

    public void reset(){
        getCities().forEach((City c) -> c.reset());
    }

    public int bonus(int value){
        if(value > 0){
            Random random = new Random();
            return random.nextInt(value+1); //int i intervallet [0,value+1) = [0,value]
        }
        return 0;
    }

    /**
     * map.put()
     * If the map previously contained a mapping for the key,
     * the old value is replaced by the specified value.
     */
    public void addRoads(City a, City b, int length){
        if(network.containsKey(a)){
            Road res = new Road(a,b,length);
            List<Road> roadRes = getRoads(a);
            roadRes.add(res);
            network.put(a,roadRes);
        }
        if(network.containsKey(b)){
            Road res = new Road(b,a,length);
            List<Road> roadRes = getRoads(b);
            roadRes.add(res);
            network.put(b,roadRes);
        }
    }

    public Position position(City city){
        return new Position(city,city,0);
    }

    public Position readyToTravel(City from, City to){
        //if(from.equals(to)){
        //    return position(from);
        //}
        //if(!network.containsKey(from)){
        //    return position(from);
        //}
        Optional<Road> check = getRoads(from).stream()
                                             .filter((Road r) -> r.getTo().equals(to))
                                             .findFirst();
        if(check.isPresent()){
            return new Position(from,to,check.get().getLength());
        }
        else {
            return position(from);
        }
    }
}
