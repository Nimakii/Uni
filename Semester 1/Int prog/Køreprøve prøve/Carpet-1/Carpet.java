import java.util.*;
/**
 * Write a description of class was here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Carpet implements Comparable<Carpet>
{
    private String color;
    private int length;
    private int price;
    
    public Carpet(String color, int length, int price){
        this.color = color;
        this.length = length;
        this.price = price;
    }
    
    public String toString(){
        return ( color + " carpet, " + length + " meter of " + price + " kr.");
    }
    
    public String getColor(){return color;}
    public int getLength(){return length;}
    public int getPrice(){return price;}
    
    public int compareTo(Carpet c){
        if (this.price/this.length == c.getPrice()/c.getLength()){
            return this.length - c.getLength();
        }
        else{
            return this.price/this.length - c.getPrice()/c.getLength();
        }
    }
}
