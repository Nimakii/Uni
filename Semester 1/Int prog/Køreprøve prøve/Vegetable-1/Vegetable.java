import java.util.*;
/**
 * Write a description of class was here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Vegetable implements Comparable<Vegetable>
{
    private String name;
    private boolean organic;
    private int price;
    
    public Vegetable(String name, boolean organic, int price){
        this.name = name;
        this.organic = organic;
        this.price = price;
    }
    
    public String toString(){
        String test = organic?"Organic ":"Non-organic ";
        return (test + name +", " + price + " cents");
    }
    
    public String getName(){
        return name;
    }
    public boolean getOrganic(){return organic;}
    public int getPrice(){return price;}
    
    public int compareTo(Vegetable v){
        if (this.name.compareTo(v.name) == 0){
            return -(this.price - v.price);
        }
        return this.name.compareTo(v.name);
    }
}
