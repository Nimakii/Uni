import java.util.*;
/**
 * Write a description of class wasd here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Boat implements Comparable<Boat>
{
    private String name;
    private int value;
    private boolean forSale;
    
    public Boat(String name, int value, boolean forSale){
        this.name = name;
        this.value = value;
        this.forSale = forSale;
    }
    
    public String toString(){
        if (forSale){
            return(name + ": " + value + " kr., for sale");
        } else{
            return(name + ": " + value + " kr., not for sale");
        }
    }
    
    public int getValue(){
        return value;
    }
    
    public boolean getSale(){
        return forSale;
    }
    
    public String getName(){
        return name;
    }
    
    public int compareTo(Boat b){
        if (this.value == b.getValue()){
            return this.name.compareTo(b.getName());
        }
        else{
            return -(this.value - b.getValue());
        }
    }
}
