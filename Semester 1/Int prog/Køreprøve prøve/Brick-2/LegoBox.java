import java.util.*;
import java.util.stream.Collectors;
/**
 * Write a description of class LegoBox here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class LegoBox
{
    private String owner;
    private List<Brick> bricks;
    
    public LegoBox(String owner){
        this.owner = owner;
        bricks = new ArrayList<Brick>();
    }
    
    public void add(Brick b){
        bricks.add(b);
    }
    
    public int weight(String color){
        int res = 0;
        for (Brick b : bricks){
            if (b.getColor().equals(color)){
                res += b.getNumber()*b.getWeight();
            }
        }
        return res;
    }
    
    public long weightF(String color){
        return bricks.stream().filter(b -> b.getColor().equals(color))
                              .mapToInt(b -> b.getNumber()*b.getWeight())
                              .sum();
    }
    
    public Optional<Brick> heaviestF(){
        return bricks.stream().max(Comparator.comparing(b -> b.getWeight()));
    }
    
    public Brick heaviest(){
        Brick res = null;
        for (Brick b : bricks){
            if ( res == null || b.getWeight() > res.getWeight()){
                res = b;
            }
        }
        return res;
    }
    
    public void printLegoBox(){
        System.out.println("The owner of this collection is: " + owner);
        Collections.sort(bricks);
        for (Brick b : bricks){
            System.out.println(b);
        }
        Collections.shuffle(bricks);
    }
    
    public void printLegoBoxF(){
        System.out.println("The owner of this collection is: " + owner);
        Collections.sort(bricks, Comparator.comparing((Brick b) -> -b.getNumber())
                                           .thenComparing((Brick b) -> b.getColor()));
        bricks.forEach(b -> System.out.println(b));
        Collections.shuffle(bricks);
    }
}
