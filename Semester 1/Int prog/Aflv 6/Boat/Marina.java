import java.util.*;
/**
 * Write a description of class Marina here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Marina
{
    private String name;
    private ArrayList<Boat> boats;
    
    public Marina(String name){
        this.name = name;
        boats = new ArrayList<Boat>();
    }
    
    public void add(Boat b){
        boats.add(b);
    }
    
    public ArrayList<Boat> cheaperThanAndForSale(int t){
        ArrayList<Boat> res = new ArrayList<Boat>();
        for (Boat b : boats){
            if (b.getSale() && b.getValue()<t){
                res.add(b);
            }
        }
        return res;
    }
    
    public String getName(){
        return name;
    }
    
    public Boat mostValuable(){
        if (boats.get(0) != null){
            Boat res = new Boat("",0,false);
            for (Boat b : boats){
                if (b.getValue() > res.getValue()){
                    res = b;
                }
            }
            return res;
        } 
        else{
            return null; 
        }
    }
    
    public void printBoats(){
        System.out.println("In the marina that goes by the name: " + name );
        System.out.println("The following boats are docked");
        Collections.sort(boats);
        for (Boat b : boats){
            System.out.println(b.toString());
        }
    }
}
