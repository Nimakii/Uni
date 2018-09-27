import java.util.*;
/**
 * Write a description of class Animal here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Animal implements Comparable<Animal>
{
    private String name;
    private int females;
    private int males;
    
    public Animal(String name, int females, int males){
        this.name = name;
        this.females = females;
        this.males = males;
    }
    
    public String toString(){
        return(name +": " + females + " females and " + males + "males");
    }
    
    public int getFem(){
        return females;
    }
    
    public int getMa(){
        return males;
    }
    
    public String getName(){
        return name;
    }
    
    public int compareTo(Animal a){
        if (this.females != a.getFem()){
            return -(this.females - a.getFem());
        }
        return -(this.males - a.getMa());
    }
}
