/**
 * Represents a players position in the game.
 * 
 * Containing a city from, a city to, the distance at which the player is at between the two cities and the total distance between the two cities. 
 * A player can move between cities, and turn around. 
 *
 * @author Jens Kristian & Thomas Vinther
 * @version Computergame 2
 */
public class Position
{
    /** The city the player comes from */
    private City from;
    /** The city the player is going to*/
    private City to;
    /**Remaining distance */
    private int distance;
    /** Total distance between the two cities*/
    private int total; 

    /**
     * Creates a a new Position, with a city 'from', a city 'to' and the distance at which the player is at. 
     * @param from      City the player came from.
     * @param to        City the player is going to.
     * @param distance  The distance between the two cities where the player is. 
     */
    public Position(City from, City to, int distance)
    {
        this.from = from; 
        this.to = to;
        this.distance = distance;
        this.total = distance;
    }

    /**
     * Returns a reference to the city the player came from
     * @return      from city.
     */
    public City getFrom(){
        return from;
    }

    /**
     * Returns a reference to the city the player is travelling to. 
     * @return      to city. 
     */
    public City getTo(){
        return to;
    }

    /**
     * Returns the remaining distance between the two cities.
     * @return      remaining distance.    
     */
    public int getDistance(){
        return distance;
    }

    /**
     * Returns the total distance between the from and to cities.
     * @return      total distance. 
     */
    public int getTotal(){
        return total;
    }
    
    /**
     * Returns whether or not the remaining distance is 0. 
     * @return      true if distance is 0.
     */
    public boolean hasArrived(){
        return (distance == 0);
    }
    
    /**
     * Moves the player if the remaning distance is greater than 0
     * @return      true if the player is moved.
     */
    public boolean move(){
        if(distance > 0){
            distance--;
            return true;
        }
        return false;
    }

    /**
     * Turns the player around without moving the player. 
     */
    public void turnAround(){
        City temp = from;
        from = to;
        to = temp;
        distance = total - distance;
    }

}
