import java.util.*;
import java.util.Random;
/**
 * A Country is a collection of cities and roads between them, all part of a game.
 *
 * @author Jens Kristian & Thomas  Vinther
 * @version Computergame 3
 */
public class Country
{
    /** The name of the Country*/
    private String name;

    /** A map containing the Cities of the country, mapped to the List of roads originating
    in the City.*/
    private Map<City,List<Road>> network;

    /** A pointer to the game world we are playing in. */
    private Game game;

    /**
     * Creates a new Country with the name of the Country and a network (a map) which links a city to the list of all roads starting in 
     * that particular city. 
     * @param name      name of the Country
     * @param network   network of cities and roads within the country
     */
    public Country(String name, Map<City,List<Road>> network)
    {
        this.name = name;
        this.network = network;
    }

    /**
     * Returns the name of the country.
     * @return          the countrys name
     */
    public String getName(){
        return name;   
    }

    /**
     * Returns the infrastructure network of the Country, a Map that maps a City to a List of
     * roads. I.e. the map links a city to the list of all roads starting in that particular city. 
     * @return      the network field variable
     */
    public Map<City,List<Road>> getNetwork(){
        return network;
    }

    /**
     * Returns the list of Roads starting in the given City, if and only if the City lies
     * within the borders of the Country.
     * If the country does not contain the city, an empty list is returned.
     * @param c     the City whos list of Roads we wish to get
     * @return      the list of roads starting in City c
     */
    public List<Road> getRoads(City c){
        if(network.containsKey(c)){
            return network.get(c);
        }
        List<Road> res = new ArrayList<>();
        return res;
    }

    /**
     * Returns a sorted list of cities within the country.
     * 
     * @return      list of cities in the country.
     */
    public List<City> getCities(){
        List<City> res = new ArrayList<>(network.keySet());
        Collections.sort(res);
        return res;
    }

    /**
     * Returns a City with the given name, or null if no such city exists within the Country.
     * 
     * @param name  name we wish to search for.
     * @return      city with the name "name".
     */
    public City getCity(String name){
        for(City c : getCities()){
            if(name.equals(c.getName())){
                return c;
            }
        }
        return null;
    }

    /**
     * Resets all cities within the Country.
     */
    public void reset(){
        for(City c : getCities()){
            c.reset();   
        }
    }

    /**
     * Calculates a random integer between 0 and value based upon the Random implemented in the Game Class.
     * 
     * @param value the value used in calculations.
     * @return      a random integer between 0 and value. 
     */
    public int bonus(int value){
        if(value > 0){
            return game.getRandom().nextInt(value+1);
        }
        return 0;
    }

    /**
     * Adds roads starting in the given two cities, if and only if they lie within this Country
     * and ending in the other City. With distance length.
     * 
     * @param City a        the first City.
     * @param City b        the second City.
     * @param int length    the potential distance between them.
     */
    public void addRoads(City a, City b, int length){
        if(network.containsKey(a)){
            Road res = new Road(a,b,length);    //New road
            List<Road> roadRes = getRoads(a);   //Gets the list with roads from a
            roadRes.add(res);                   //adds the new road to the city

        }
        if(network.containsKey(b)){
            Road res = new Road(b,a,length);
            List<Road> roadRes = getRoads(b);
            roadRes.add(res);

        }
    }

    /**
     * This method creates a Position with the paramaters city, city and 0. To represent
     * the position of the city.
     * 
     * @param city      the City we wish to create a position.
     * @return          the position of the city.
     */
    public Position position(City city){
        return new Position(city,city,0);
    }

    /**
     * We wish to check if there is a road from the City 'from', to the City 'to', if this is the case 
     * we return a Position object representing our readiness to travel. If we get a
     * Position(from,to,length) object we have begun the journey from 'from' to 'to', and have
     * taken 0 steps, as a road exsists between 'from' and 'to'. 
     * If we get a Position(from,from,0) object we cannot travel from 'from' to 'to', as there was no such road. 
     * 
     * @param from      the City we wish to travel from
     * @param to        the City we wish to travel to
     * @return          the position object that represents our new position
     */
    public Position readyToTravel(City from, City to){
        for(Road r : getRoads(from)){
            if(r.getTo().equals(to)){
                return new Position(from,to,r.getLength());
            }
        }
        return position(from);
    }

     /**
     * Returns the Game that we are playing.
     * 
     * @return      a pointer to the Game this country is a part of.
     */
    public Game getGame(){
        return game;
    }

    /**
     * Sets the game this country is a part of.
     * 
     * @param game    the game we wish this country to partake in.
     */
    public void setGame(Game game){
        this.game = game;
    }
    
    /**
     * This method overrides Object's equals method and compares two countries.
     * @return whether or not the two objects are equal
     */
    @Override
    public boolean equals(Object otherObject){
        if(this == otherObject){
            return true;
        }
        if(otherObject==null){
            return false;
        }
        if( getClass()!=otherObject.getClass()){
            return false;
        }
        Country other = (Country) otherObject;
        return (this.name.equals(other.name));
    }
    /**
     * This method overrides Object's hashCode method. 
     * @return a hashCode.
     */
    @Override
    public int hashCode(){
        return 97*name.hashCode();
    }

}
