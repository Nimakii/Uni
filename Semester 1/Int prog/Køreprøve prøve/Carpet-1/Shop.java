import java.util.*;
import java.util.stream.Collectors;
/**
 * Write a description of class was here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Shop
{
    private String name;
    private List<Carpet> carpets;
    
    public Shop(String name){
        this.name = name;
        this.carpets = new ArrayList<Carpet>();
    }
    
    public void add(Carpet c){ carpets.add(c); }
    
    public ArrayList<Carpet> selectionI(String color){
        ArrayList<Carpet> res = new ArrayList<Carpet>();
        for (Carpet c : carpets){
            if (c.getColor().equals(color)){
                res.add(c);
            }
        }
        return res;
    }
    
    public List<Carpet> selectionF(String color){
        return carpets.stream().filter((Carpet c) -> c.getColor().equals(color))
                               .collect(Collectors.toList());
    }
    
    public Carpet cheapestI(String color){
        Carpet res = null;
        for (Carpet c : carpets){
            if (res == null || (c.getColor().equals(color) 
                && c.getPrice()/c.getLength() < res.getPrice()/res.getLength())){
                res = c;
            }
        }
        return res;
    }
    
    public Optional<Carpet> cheapestF(String color){
        return carpets.stream().filter(c -> c.getColor().equals(color))
                               .min(Comparator.comparing(c -> c.getPrice()/c.getLength()));
    }
    
    public void printShopI(){
        System.out.println(name+ "Imperial");
        Collections.sort(carpets);
        for (Carpet c: carpets){
            System.out.println(c);
        }
        Collections.shuffle(carpets);
    }
    
    public void printShopF(){
        System.out.println(name+ "Functional");
        Collections.sort(carpets , Comparator.comparing((Carpet c) -> c.getPrice()/c.getLength())
                                             .thenComparing(c -> c.getLength()));
        carpets.forEach(c -> System.out.println(c));
        Collections.shuffle(carpets);
    }
}
