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
    private String name;
    private List<Brick> bricks;
    
    public LegoBox(String name){
        this.name = name;
        bricks = new ArrayList<Brick>();
    }
    
    public void add(Brick b){ bricks.add(b);}
    
    public int area(String color){
        int res = 0;
        for (Brick b : bricks){
            if (b.getColor().equals(color)){
                res += b.getNumber()*b.getWidth()*b.getLength();
            }
        }
        return res;
    }
    
    public int areaF(String color){
        return bricks.stream().filter(b -> b.getColor().equals(color))
                              .mapToInt(b -> b.getWidth()*b.getLength())
                              .sum();
    }
    
    public Brick most(){
        Brick res = null;
        for (Brick b : bricks){
            if ( res == null || b.getNumber() > res.getNumber() ){
                res = b;
            }
        }
        return res;
    }
    
    public Optional<Brick> mostF(){
        return bricks.stream().max(Comparator.comparing(b -> b.getNumber()));
    }
    
    public void printLegoBox(){
        System.out.println("In the lego box known as " + this.name + " the following bricks can be found: ");
        Collections.sort(bricks);
        for (Brick b : bricks){
            System.out.println(b);
        }
        Collections.shuffle(bricks);
    }
    
    public void printLegoBoxF(){
        System.out.println("In the lego box known as " + this.name + " the following bricks can be found: ");
        Collections.sort(bricks, Comparator.comparing((Brick b) -> b.getColor()).thenComparing(b -> b.getNumber()));
        bricks.forEach(b -> System.out.println(b));
        Collections.shuffle(bricks);
    }
}
