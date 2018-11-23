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
     * Creates a new Road Object, that begins in a city 'from' and ends in a city 'to', with an integer distance between them. 
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
     * Order two Roads according to the city they come from, if they originate in the same city we compare based upon the city they go to.
     * Calculates an integer, that is negativ if this road is less than the road we are comparing to, zero if they are equal and positive if
     * this road is greater than the road parameter. 
     * @param other road to be compared to this road.
     * @return      an integer determing the order of the roads.
     */
    public int compareTo(Road other) {
        if(this.from.equals(other.from)){
            return this.to.compareTo(other.to);
        }
        return this.from.compareTo(other.from);
    }
}
