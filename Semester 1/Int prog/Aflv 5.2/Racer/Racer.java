import java.util.*;
/**
 * Write a description of class Racer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Racer implements Comparable<Racer>
{
    private String name;
    private int year;
    private int topSpeed;
    
    public Racer (String name, int year, int topSpeed){
        this.name = name;
        this.year = year;
        this.topSpeed = topSpeed;
    }
    
    public String getName(){
        return name;
    }
    
    public int getYear(){
        return year;
    }
    
    public String toString(){
        return(name +"-" + year + ", Top speed: " + topSpeed+ "km/h");
    }
    
    public int getSpeed(){
        return topSpeed;
    }
    
    public int compareTo(Racer r){
        if (this.year != r.getYear()){
            return this.year-r.getYear();
        }
        return this.name.compareTo(r.getName());
    }
}
