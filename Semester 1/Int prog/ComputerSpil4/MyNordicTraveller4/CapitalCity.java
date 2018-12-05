
/**
 * A Capitalcity, that is a BorderCity and a City. Not only does a capitalcity charge toll, the player also spends money in the capitalcity,
 * the spending in the city is transferred from the player to the city. It also contains a name, a value and a country in which it resides. 
 *
 * @author Jens Kristian & Thomas Vinther
 * @version Computergame 3
 */
public class CapitalCity extends BorderCity
{

    /**
     * Creates a new CapitalCity, with the given name, starting value of the city and the country in which it resides. 
     * @param name      the name of the city. 
     * @param value     the starting value of the city.
     * @param country   the Country in which the city resides. 
     */
    public CapitalCity(String name, int value, Country country)
    {
        super(name,value,country);
    }

    /**
     * This method calculates a bonus for visitng the city based on the country, a toll to be paid and how much money the players spends in the city.
     * How much money the player spends in the city, depends on the value of the city, how much toll to be paid and the bonus for visiting the city.
     * The spending has the range 1 to players wealth entering the city +(bonus for visiting the city+toll to be paid)
     * 
     * @return integer in the range of the (bonus + toll)-spending note that the toll is not positive. 
     * @param p player that visits the CapitalCity
     */
    @Override
    public int arrive(Player p){
        int bonusAndToll = super.arrive(p);             //calculate the toll and bonus for the player arriving
        int spending = getCountry().getGame().getRandom().nextInt(p.getMoney()+1+bonusAndToll); //Calculate how much money the player spends in the capital.
        changeValue(spending);                          //Add the spent money to the city. 
        return bonusAndToll-spending;                                           
    }

}
