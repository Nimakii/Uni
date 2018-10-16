import java.util.*;
/**
 * Write a description of class Vegetable here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Vegetable implements Comparable<Vegetable>
{
    private String name;
    private boolean organic;
    private int number;
    
    public Vegetable(String name, boolean organic, int number){
        this.name=name;
        this.organic = organic;
        this.number = number;
    }
    public String toString(){
        String test = organic?"organic":"non-organic";
        return number + " " + test + " " + name;
    }
    public String getName(){return name;}
    public boolean getOrganic(){return organic;}
    public int getNumber(){return number;}
    
    public int compareTo(Vegetable v){
        if(this.name.equals(v.name)){
            return -(this.number-v.number);
        }
        return this.name.compareTo(v.name);
    }
}
