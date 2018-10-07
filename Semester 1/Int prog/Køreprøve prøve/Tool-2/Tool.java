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
    private int price;
    private int weight;
    
    public Tool(String name, int price, int weight){
        this.name = name;
        this.price = price;
        this.weight = weight;
    }
    
    public String getName(){return name;}
    public int getPrice(){return price;}
    public int getWeight(){return weight;}
    
    public String toString(){
        return (name + ": " + price + " kr., " + weight + " gram");
    }
    
    public int compareTo(Tool t){
        if(this.weight == t.getWeight()){
            return this.price - t.getPrice();
        }
        return this.weight - t.getWeight();
    }
}
