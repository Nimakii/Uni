import java.util.*;
/**
 * Write a description of class was here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Tool implements Comparable<Tool>
{
    private String name;
    private boolean electric;
    private int price;
    
    public Tool(String name, boolean electric, int price){
        this.name = name;
        this.electric = electric;
        this.price = price;
    }
    
    public String toString(){
        String test = electric?"Electric":"Manual"; 
        return( test + " " + name+ ": " + price + " kr.");
    }
    
    public int compareTo(Tool t){
        if (this.price == t.getPrice()){
            return this.name.compareTo(t.getName());
        }
        return this.price - t.getPrice();
    }
    
    public String getName(){return name;}
    public boolean getElectric(){return electric;}
    public int getPrice(){return price;}
}
