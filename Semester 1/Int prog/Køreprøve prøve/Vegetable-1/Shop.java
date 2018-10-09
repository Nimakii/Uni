import java.util.*;
import java.util.stream.Collectors;
/**
 * Write a description of class wasbox here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Shop
{
    private String name;
    private List<Vegetable> vegs;
    
    public Shop(String name){
        this.name = name;
        this.vegs = new ArrayList<Vegetable>();
    }
    public void add(Vegetable v){vegs.add(v);}
    
    public int totalPriceI(boolean organic){
        int res = 0;
        for (Vegetable v : vegs){
            if(v.getOrganic()){res += v.getPrice();}
        }
        return res;
    }
    public int totalPriceF(boolean organic){
        return vegs.stream().filter(v -> v.getOrganic()).mapToInt(v -> v.getPrice()).sum();
    }
    
    public boolean organicI(){
        int n = 0;
        int m = 0;
        for(Vegetable v : vegs){
            if(v.getOrganic()){n++;}
            else{m++;}
        }
        if(n> m){return true;}
        else{return false;}
    }
    public boolean organicF(){
        if( vegs.stream().filter(v -> v.getOrganic()).count() > 
            vegs.stream().filter(v -> !v.getOrganic()).count() ){
            return true;        
        }
        return false;
    }
    
    public void printShopI(){
        System.out.println(name);
        Collections.sort(vegs);
        for(Vegetable v : vegs) {
            System.out.println(v);
        }
        Collections.shuffle(vegs);
    }
    public void printShopF(){
        System.out.println(name);
        Collections.sort(vegs ,  Comparator.comparing((Vegetable v) -> v.getName())
                                           .thenComparing(v -> -v.getPrice()));
        vegs.forEach(v -> System.out.println(v));
        Collections.shuffle(vegs);
    }
}
