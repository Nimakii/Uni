import java.util.*;
import java.util.stream.Collectors;

/**
 * Write a description of class Shop here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Shop
{
    private String address;
    private List<Carpet> carpets;
    
    public Shop(String address){
        this.address = address;
        carpets = new ArrayList<Carpet>();
    }
    
    public void add(Carpet c){
        carpets.add(c);
    }
    
    public int totalPriceI(String material){
        int res = 0;
        for (Carpet c : carpets){
            if (c.getMaterial().equals(material)){
                res += c.getPrice();
            }
        }
        return res;
    }
    
    public int totalPriceF(String material){
        return carpets.stream().filter(c -> c.getMaterial().equals(material))
                               .mapToInt(c -> c.getPrice()).sum();
    }
    
    public List<Carpet> selectionF(String col, String mat){
        return carpets.stream().filter(c -> c.getMaterial().equals(mat))
                               .filter(c -> c.getColor().equals(col))
                               .collect(Collectors.toList());
    }
    
    public ArrayList<Carpet> selectionI(String col, String mat){
        ArrayList<Carpet> res = new ArrayList<Carpet>();
        for (Carpet c : carpets){
            if (c.getMaterial().equals(mat) && c.getColor().equals(col)){
                res.add(c);
            }
        }
        return res;
    }
    
    public void printShopI(){
        System.out.println("imperial" + address);
        Collections.sort(carpets);
        for(Carpet c : carpets){
            System.out.println(c);
        }
        Collections.shuffle(carpets);
    }
    
    public void printShopF(){
        System.out.println("Functional" + address);
        Collections.sort(carpets , Comparator.comparing((Carpet c) -> c.getMaterial())
                                             .thenComparing(c -> c.getPrice()));
        carpets.forEach(c -> System.out.println(c));
        Collections.shuffle(carpets);
    }
}
