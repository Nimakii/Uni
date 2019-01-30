import java.util.*;
/**
 * Write a description of class eksamensLeg here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class EksamensLeg
{
    private HashSet<Die> dies;
    public EksamensLeg(){
        this.dies = new HashSet<>();
        Die d1 = new Die(6);
        dies.add(d1);
        dies.forEach(d -> d.roll());
        List<Integer> dieS = new ArrayList<>();
        dieS.add(3);
        dieS.add(5);
        dieS.forEach(i -> dies.add(new Die(i)));
        //dies.forEach(d->System.out.println(d));
    }
    
    public int fuckU(){
        return dies.stream()
                   .map((Die r) -> r.getEyes())
                   .reduce(0,(count,r) -> count+r);
    }
}
