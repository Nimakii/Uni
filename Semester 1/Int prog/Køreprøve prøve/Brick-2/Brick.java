import java.util.*;
/**
 * Write a description of class Brick here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Brick implements Comparable<Brick>
{
    private String color;
    private int number;
    private int weight;
    
    public Brick(String color, int number, int weight){
        this.color = color;
        this.number = number;
        this.weight = weight;
    }
    
    public String toString(){
        return (number + " " + color + " (" + weight + " gram each)");
    }
    
    public int getNumber(){ return number;}
    public int getWeight(){ return weight;}
    public String getColor(){ return color;}
    
    public int compareTo(Brick b){
        if (this.number == b.getNumber()){
            return this.color.compareTo(b.getColor());
        }
        return b.getNumber()-this.number;
    }
}
