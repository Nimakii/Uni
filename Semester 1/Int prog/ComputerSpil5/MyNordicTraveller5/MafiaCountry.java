import java.util.*;

/**
 * A mafiacountry represents a country,but each time you visit a city inside the country you risk getting robbed! The mafiacountry has a name and is a
 * collection of cities and roads between them, all part of a game. 
 *
 *  @author Jens Kristian & Thomas  Vinther
 * @version Computergame 3
 */
public class MafiaCountry extends Country
{
    
    /**
     * Creates a new MafiaCountry with the name of the MafiaCountry and a network (a map) which links a city to the list of all roads starting in 
     * that particular city. 
     * @param name      name of the MafiaCountry
     * @param network   network of cities and roads within the MafiaCountry
     */
    public MafiaCountry(String name, Map<City,List<Road>> network)
    {
        super(name, network);
    }

    /**
     * When visiting a MafiaCountry you risk getting robbed, the risk and loss are decided by game settings, where the loss is a random integer
     * in an interval, but not more than the players wealth. If you do not get robbed, you get an integer bonus based on country's bonus 
     * method.
     * 
     * @param value the value used in calculations
     * @return      a random integer [0;value] or a negative integer from game decided by Game parameters  
     */ 
    @Override
    public int bonus(int value){
        if(getGame().getRandom().nextInt(100)+1 > getGame().getSettings().getRisk()){
            return super.bonus(value);
        }
        return -getGame().getLoss();
    }

}
