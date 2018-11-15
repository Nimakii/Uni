import java.util.*;
import java.util.Random;
/**
 * A Contry is a collection of cities and roads between them, all part of a game.
 *
 * @author Jens Kristian Nielsen & Thomas Vinther
 * @version (a version number or a date)
 */
public class Country
{
    /** The name of the Country.*/
    private String name;
    
    /** A map containing the Cities of the country, mapped to the List of roads originating
        in the City.*/
    private Map<City,List<Road>> network;
    
    /** A pointer to the game world we are playing in. */
    private Game game;
    
    /**
     * Creates a new    Country object.
     * @param name      Name of the Country.
     * @param network   Network of cities and roads within the country.
     */
    public Country(String name, Map<City,List<Road>> network){
        this.name    = name;
        this.network = network;
    }
    
    /**
     * Returns a reference to the Game that we are playing.
     * 
     * @return      a pointer to the Game this country is a part of.
     */
    public Game getGame(){
        return game;
    }
    
    /**
     * Sets the game this country is a part of.
     * 
     * @param      The game we wish this country to partake in.
     */
    public void setGame(Game game){
        this.game = game;
    }
    
    /**
     * Returns the name of the country.
     * 
     * @return     the countrys name.
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns the infrastructure network of the Country, a Map that maps a City to a List of
     * Road objects.
     * 
     * @return      the network field variable.
     */
    public Map<City,List<Road>> getNetwork(){
        return network;
    }

    /**
     * Returns the list of Roads starting in the given City, if and only if the City lies
     * within the borders of the Country.
     * 
     * @param c     The City whos list of Roads we wish to get.
     * @return      The list of roads starting in City c.
     */
    public List<Road> getRoads(City c){
        if(this.network.containsKey(c)){
            return this.network.get(c);
        }
        List<Road> res = new ArrayList<Road>();
        return res;
    }

    /**
     * Returns the list of cities within the country.
     * 
     * @return      List of cities.
     */
    public List<City> getCities(){
        //List<City> res = (ArrayList) network.keySet();
        List<City> res = new ArrayList<>(network.keySet());
        Collections.sort(res);
        return res;
    }

    /**
     * Returns a City with the given name, or null if no such city exists within the Country.
     * 
     * @param name  Name we wish to search for.
     * @return      City by name name.
     */
    public City getCity(String name){
        if (getCities().stream().filter( c -> c.getName().equals(name))
                                .findFirst()
                                .isPresent()){
            return getCities().stream().filter( c -> c.getName().equals(name))
                                       .findFirst()
                                       .get();
        }
        return null;
    }

    /**
     * Resets all cities within the Country.
     */
    public void reset(){
        getCities().forEach((City c) -> c.reset());
    }

    /**
     * Calculates a random integer based upon the Random implemented in the Game Class.
     * 
     * @param value the value used in calculations.
     * @return      a random value based upon the value paramater and the implementation of
     *              random in the Game class.
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
     * @param City a        The first City.
     * @param City b        The second City.
     * @param int length    The potential distance between them.
     */
    public void addRoads(City a, City b, int length){
        if(network.containsKey(a)){
            Road res = new Road(a,b,length);
            List<Road> roadRes = getRoads(a);
            roadRes.add(res);
        }
        if(network.containsKey(b)){
            Road res = new Road(b,a,length);
            List<Road> roadRes = getRoads(b);
            roadRes.add(res);
        }
    }

    /**
     * This method creates a Position object with the paramaters city, city and 0. To represent
     * the position of the city.
     * 
     * @param City city     The City we wish to create a position.
     * @return Position     the position of the city.
     */
    public Position position(City city){
        return new Position(city,city,0);
    }

    /**
     * We wish to check if there is a road from the City from, to the City to, and if there is 
     * we return a Position object representing our readiness to travel. If we get a
     * Position(from,to,length) object we have begun the journey from from to to, and have
     * taken 0 steps.
     * If we get a Position(from,from,0) object we cannot travel from from to to.
     * 
     * @param City from     The City we wish to travel from
     * @param City to       The City we wish to travel to
     * @return Position     The position object that represents our new position
     */
    public Position readyToTravel(City from, City to){
        Optional<Road> check = getRoads(from).stream()
                                             .filter((Road r) -> r.getTo().equals(to))
                                             .findFirst();
        if(check.isPresent()){
            return new Position(from,to,check.get().getLength());
        }
        else {
            return position(from);
        }
    }
}
