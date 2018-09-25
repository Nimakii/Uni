import java.util.*;
/**
 * Write a description of class Band here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Band
{
    private ArrayList<Musician> members;
    private String name;
    
    public Band(String name){
        this.name = name;
        this.members = new ArrayList<Musician>();
    }
    
    public void add(Musician m){
        members.add(m);
    }
    
    public void remove(Musician m){
        members.remove(m);
    }
    
    public Integer skilledMusicians(int requiredSkill){
        Integer count = 0;
        for (Musician m : members){
            if (m.getSkill() >= requiredSkill){
                count ++;
            }
        }
        return count;
    }
    
    public Musician withInstrument(String instrument){
        for (Musician m : members){
            if (m.getInstrument().equals(instrument)){
                return m;
            }
        }
        return null;
    }
    
    public void printBand(){
        System.out.println("Bandet hedder " + name);
        Collections.sort(members);
        for (Musician m : members){
            System.out.println(m.toString());
        }
    }
}
