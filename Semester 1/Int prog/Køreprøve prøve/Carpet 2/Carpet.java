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
    private String material;
    private int price;
    
    public Carpet(String color, String material, int price){
        this.color = color;
        this.material = material;
        this.price = price;
    }
    
    public String toString(){
        return (color + " " + material + ": " + price + " kr.");
    }
    
    public String getColor(){return color;}
    public String getMaterial(){return material;}
    public int getPrice(){return price;}
    
    public int compareTo(Carpet c){
        if (this.material.equals(c.getMaterial())){
            return this.price - c.getPrice();
        }
        return this.material.compareTo(c.getMaterial());
    }
}
