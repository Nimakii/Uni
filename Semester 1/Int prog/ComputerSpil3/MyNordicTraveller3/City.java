import java.util.*;
/**
 * Represents a city that is visitable. 
 * 
 * A city has a name, a value and a country to which it belongs, by ariving in the city the player gets a bonus, that is subtracted 
 * from the city's value.  
 * 
 * @author  Jens Kristian & Thomas Vinther
 * @version Computergame 2
 */
public class City implements Comparable<City> {
    /** The name of the City */
    private String name;
    /**The value of the city*/
    private int value;
    /**The City's starting value*/
    private int initialValue;
    /** The Country in which the city resides */ 
    private Country country;
    /**
     * Creates a new City, with the given name, starting value of the city and the country in which it resides. 
     * @param name      the name of the city. 
     * @param value     the starting value of the city.
     * @param country   the Country in which the city resides. 
     */ 
    public City(String name, int value, Country country){
        this.name = name;
        this.value = value;
        this.initialValue = value;
        this.country = country;

    }

    /**
     * Returns the name of the city.
     * @return      city name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current value of the City
     * @return      current value of the city. 
     */
    public int getValue() {
        return value;
    }

    /**
     * Increases the current value of the city with the given amount.
     * @param amount      the amount we wish to increase the City's value.  
     */
    public void changeValue(int amount){
        value += amount;
    }
    
    /**
     * Reset the current value of the city to the initial starting value.
     */
    public void reset(){
        value = initialValue;
    }
    
    /**
     * Order two Cities according to their names. Calculates an integer, that is negativ if this City is less than the City we are comparing to,
     * zero if they are equal and positive if this city is greater than the parameter. 
     * 
     * @param city  city to be compared to this City.
     * @return      an integer determing the order of the cities.
     */
    public int compareTo(City city){
        return name.compareTo(city.name);
    }

    /**
     * Returns a reference to the Country in which the City resides.
     * @return      aountry in which the city resides. 
     */
    public Country getCountry(){
        return country;
    }
    
    /**
     * Returns an integer bonus between 0 and the value of the city based on the country in which the city resides, 
     * @return      an integer bonus between 0 and the value of the city
     */
    public int arrive(){
        int bonus = country.bonus(value);
        if(bonus<0){
            return bonus;       //If the bonus is negative we return it, without changing the value of the city.
        }
        value -= bonus;
        return bonus;
    }
    
    /**
     * Returns an integer bonus between 0 and the value of the city based on the country in which the city resides.
     * @return      an integer bonus between 0 and the value of the city 
     * @param p     a player that is arriving to the city
     */
    public int arrive(Player p){
        return arrive();
    }
    
    /**
     * This method overrides Object's equals method and compares two cities, using their names and country.
     * @return      whether or not the two objects are equal
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
        City other = (City) otherObject;
        return (this.name.equals(other.name) && this.country.equals(other.country) );
    }
    /**
     * This method overrides Object's hashCode method and gives the city a new one (an integer)
     * using the name of the city and the country in which it resides. 
     * @return a hashCode.
     */
    @Override
    public int hashCode(){
        return 89*name.hashCode()+83*country.hashCode();
    }
}
