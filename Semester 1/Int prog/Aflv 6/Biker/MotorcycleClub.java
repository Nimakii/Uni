import java.util.*;
import java.util.stream.Collectors;
/**
 * Write a description of class MotorcycleClub here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MotorcycleClub
{
    private String name;
    private List<Biker> bikers;
    
    public MotorcycleClub(String name){
        this.name = name;
        bikers = new ArrayList<Biker>();
        
    }
    
    public void add(Biker b){
        bikers.add(b);
    }
    
    public Optional<Biker> leastRespectedBiker(){
        return bikers.stream().min(Comparator.comparing(b -> b.getBW()));
    }
    
    public List<Biker> readyBikers(int maxAmount){
        return bikers.stream().filter(b -> !b.getHosp()).limit(maxAmount).collect(Collectors.toList());
    }
    
    public void printMotorcycleClub(){
        System.out.println("In the mototcycle club known as " + name + " the following gangsters reside:");
        Collections.sort(bikers, Comparator.comparing( (Biker b) -> b.getBW())
                                           .thenComparing(b -> b.getName()));
        bikers.forEach(b -> System.out.println(b));
    }
}
