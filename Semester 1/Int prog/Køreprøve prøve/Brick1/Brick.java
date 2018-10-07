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
    private int length;
    private int width;
    
    public Brick (String color, int number, int length, int width){
        this.color = color;
        this.number = number;
        this.length = length;
        this.width = width;
    }
    
    public String getColor(){ return color;}
    public int getNumber(){ return number;}
    public int getLength(){ return length;}
    public int getWidth(){ return width; }
    
    public String toString(){
        return (number + " " + color + " (" + length + " mm x " + width + " mm)");
    }
    
    public int compareTo(Brick b){
        if (this.color.equals(b.getColor())){
            return this.number - b.getNumber();
        }
        else {
            return this.color.compareTo(b.getColor());
        }
    }
}
