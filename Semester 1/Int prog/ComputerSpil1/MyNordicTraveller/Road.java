import java.util.*;
/**
 * Write a description of class Road here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Road implements Comparable<Road>
{
    private City from;
    private City to;
    private int length;
    public Road(City from, City to, int length){
        this.from = from;
        this.to = to;
        this.length = length;
    }
    
    public City getFrom(){return from;}
    public City getTo(){return to;}
    public int getLength(){return length;}
    public int compareTo(Road r){
        if (this.from.getName().equals(r.getFrom().getName())){
            return this.to.getName().compareTo(r.getTo().getName());
        }
        return this.from.getName().compareTo(r.getFrom().getName());
    }
}
