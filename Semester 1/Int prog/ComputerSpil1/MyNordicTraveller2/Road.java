import java.util.*;
/**
 * Represents a road (between two cities).
 * A Road is one-directional. This means we may have a road form City A
 * to City B, wihtout having a road from City B to City A. 
 * 
 * @author  Jens Kristian & Thomas Vinther
 * @version Computergame 2
 */
public class Road implements Comparable<Road> {
    /** References to the cities connected by this Road */
    private City from;
    private City to;
    /** Length of this Road.*/
    private int length;

    /**
     * Creates a new Road Object.
     * @param from      City where this Road starts.
     * @param to        City where this Road ends. 
     * @param length    Length of this road object. 
     */
    public Road(City from, City to, int length ){
        this.from = from;
        this.to = to;
        this.length = length;
    }

    /**
     * Returns a reference to the City where this Road starts.
     * @return      from city.
     */
    public City getFrom() {
        return from;
    }

    /**
     * Returns a reference to the City where this Road ends.
     * @return      to city.
     */
    public City getTo() {
        return to;
    }

    /**
     * Returns the length of this Road.
     * @return      Length of road.
     */
    public int getLength() {
        return length;
    }

    /**
     * Compares two Road objects and ranks them based upon their origin city, 
     * in case of a tie we rank based upon the destination city. 
     * @return  Integer value.
     */

    public int compareTo(Road other) {
        if(this.from.equals(other.from)){
            return this.to.compareTo(other.to);
        }
        return this.from.compareTo(other.from);
    }
}
