import java.util.*;
/**
 * A mafiacountry represents a country,but each time you visit a city inside the country you risk getting robbed! The mafiacountry has a name and is a
 * collection of cities and roads between them, all part of a game. 
 *
 *  @author Jens Kristian & Thomas  Vinther
 *  @version Computergame 3
 */
public class MafiaCountry extends Country
{

    /**
     * Creates a new mafiaCountry with the name of the mafia ountry and a network (a map) which links a city to the list of all roads starting in 
     * that particular city. 
     * @param name      name of the mafia country
     * @param network   network of cities and roads within the mafia country
     */
    public MafiaCountry(String name, Map<City,List<Road>> network)
    {
        super(name, network);
    }

    /**
     * When a player visits a city in a mafia country they are robbed with chance determined by the games settings
     * if they are robbed we return a loss determined by the getLoss method in the Game class
     * if they are not robbed we return the bonus as if the mafia country is a regular country.
     * On average we get (1-risk)/100*E[random(0,value)]+risk/100*E[random(minRoberry,maxRobbery)]
     * P(X = super.bonus(value)) = 1-risk/100
     * P(X=-getGame().getLoss()) = risk/100
     * 
     * @param value the value used in calculations
     * @return      A positive integer in [
     */
    @Override 
    public int bonus(int value){
        if(getGame().getRandom().nextInt(100)+1 > getGame().getSettings().getRisk()){
            return super.bonus(value);
        }
        return -getGame().getLoss();
    }

}
