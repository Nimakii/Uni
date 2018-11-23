
/**
 * A BorderCity, a city, that also charges toll if the player arrives to the BorderCity from another country than 
 * BorderCity's country. The city contains a name, a value and a country. 
 *
 * @author Jens Kristian & Thomas Vinther
 * @version Computergame 3
 */
public class BorderCity extends City
{

    /**
     * Creates a new BorderCity with a name, a value and a country in which it resides.
     */
    public BorderCity(String name, int value, Country country )
    {
        super(name, value, country);
    }

    /** 
     * This method overrides the arrive method in City, as the player will have to pay toll, if they come from a different country.
     * The toll is a random percentage of the players total capital, based on the game settings, and is added to the city's value.
     * If the player arrives to the borderCity from within the same country, they do not have to pay the toll.
     * 
     * @return a bonus from 0 to the value of the city, subtracted a percentage of the players wealth. 
     * @param p the player arriving to the borderCity. 
     */
    @Override   
    public int arrive(Player p){
        int bonus = super.arrive(); 
        int toll = 0;
        if(!p.getCountryFrom().equals(getCountry())){
            toll = getCountry().getGame().getSettings().getTollToBePaid()*p.getMoney()/100;
            changeValue(toll);
        }
        return bonus - toll;
    }

}
