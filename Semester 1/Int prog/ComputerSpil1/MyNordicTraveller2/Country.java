import java.util.*;
/**
 * Write a description of class Country here.
 *
 * @author Thomas D. Vinther
 * @version (a version number or a date)
 */
public class Country
{
    private String name;
    private Map<City,List<Road>> network;
    private Game game;

    /**
     * Constructor for objects of class Country
     */
    public Country(String name, Map<City,List<Road>> network)
    {
        this.name = name;
        this.network = network;
    }

    public String getName(){
        return name;   
    }

    public Map<City,List<Road>> getNetwork(){
        return network;
    }

    public List<Road> getRoads(City c){
        if(network.containsKey(c)){
            return network.get(c);
        }
        List<Road> res = new ArrayList<>();
        return res;
    }

    public List<City> getCities(){
        List<City> res = new ArrayList<>(network.keySet());
        Collections.sort(res);
        return res;
    }

    public City getCity(String name){
        for(City c : getCities()){
            if(name.equals(c.getName())){
                return c;
            }
        }
        return null;
    }

    public void reset(){
        for(City c : getCities()){
            c.reset();   
        }
    }

    public int bonus(int value){
        if(value>0 && game!=null){
            Random random = game.getRandom();
            return random.nextInt(value+1);
        }
        return 0;
    }

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
     * Returns the city's position. No matter if in country or not. 
     */
    public Position position(City city){
        return new Position(city,city,0);
    }

    public Position readyToTravel(City from, City to){
        for(Road r : getRoads(from)){
            if(r.getTo().equals(to)){
                return new Position(from,to,r.getLength());
            }
        }
        return position(from);
    }

    public Game getGame(){
        return game;
    }

    public void setGame(Game game){
        game = game;
    }

}
