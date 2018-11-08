import java.util.*;
import java.util.stream.Collectors;
/**
 * Write a description of class wasd here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Shop
{
    private String owner;
    private List<Vegetable> vegs;
    
    public Shop(String owner){
        this.owner=owner;
        this.vegs = new ArrayList<Vegetable>();
    }
    public void add(Vegetable v){
        vegs.add(v);
    }
    
    public Vegetable findI(boolean organic){
        Vegetable res = null;
        for (Vegetable v : vegs){
            if (v.getOrganic() == organic){
               res = v; 
            }
        }
        return res;
    }
    public Optional<Vegetable> findF(boolean organic){
        return vegs.stream().filter(v -> v.getOrganic() == organic)
                            .min(Comparator.comparing(v -> v.getName()));
    }
    
    public boolean organicI(){
        int n = 0;
        int m = 0;
        for (Vegetable v : vegs){
            if(v.getOrganic()){
                n += v.getNumber();
            }
            else{
                m += v.getNumber();
            }
        }
        return (n>m)?true:false;
    }
    public boolean organicF(){
        int n = vegs.stream().filter(v->v.getOrganic()).mapToInt(v->v.getNumber()).sum();
        int m = vegs.stream().filter(v->!v.getOrganic()).mapToInt(v->v.getNumber()).sum();
        
        return (n>m)?true:false;
    }
    
    public void printShopI(){
        System.out.println(owner);
        Collections.sort(vegs);
        for(Vegetable v : vegs){
            System.out.println(v);
        }
        Collections.shuffle(vegs);
    }
    public void printShopF(){
        System.out.println(owner);
        Collections.sort(vegs , Comparator.comparing((Vegetable v) -> v.getName())
                                          .thenComparing(v -> -v.getNumber()));
        vegs.forEach(v -> System.out.println(v));
        Collections.shuffle(vegs);
    }
    
    public Integer eksSpgTest(){
        return vegs.stream()
                   .map(v -> v.getNumber())
                   .filter(v -> v>2)
                   .reduce(0, (total,v)->total+v);
    }
}
