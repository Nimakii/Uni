import java.util.*;
/**
 * Represents a road (between two cities).
 * A road is one-directional. This means that we may have a road from
 * City A to City B, without having a road from City B to City A
 *
 * @author Jens Kristian Nielsen & Thomas Vinther
 * @version computerspil 2
 */
public class Road implements Comparable<Road>
{
    /** References to the citiets connected by this road */
    private City from, to;
    
    /** length of this road */
    private int length;
    
    /**
     * Creates a new    Road object.
     * @param from      City where this Road starts.
     * @param to        City where this Road ends.
     * @param length    Length of this Road object.
     */
    public Road(City from, City to, int length){
        this.from   = from;
        this.to     = to;
        this.length = length;
    }
    
    /**
     * Returns a reference to the City where this Roads starts.
     * @return      from City.
     */
    public City getFrom(){
        return from;
    }
    
    /**
     * Returns a reference to the City where this Road ends.
     * @return      to City.
     */
    public City getTo(){
        return to;
    }
    
    /**
     * Returns the length of this Road.
     * @return      length int.
     */
    public int getLength(){
        return length;
    }
    
    /**
     * Compares two Road objects and ranks them based upon their origin city,
     * in case of a tie we rank based upon the destination city.
     * @return      int
     */
    public int compareTo(Road r){
        if(this.from.equals(r.from)){
            return this.to.compareTo(r.to);
        }
        return from.compareTo(r.from);
    }
}
