import java.util.*;
/**
 * Represents a city that is visitable. 
 * A city has a name, a value and a country to which it belongs.
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
     * Creates a new City Object. 
     * @param name      The name of the city. 
     * @param value     The staring value of the city.
     * @param country   The Country in which the city resides. 
     */ 
    public City(String name, int value, Country country){
        this.name = name;
        this.value = value;
        this.initialValue = value;
        this.country = country;

    }

    /**
     * Returns the name of the city.
     * @return      City name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current value of the City
     * @return      Current value of the city. 
     */
    public int getValue() {
        return value;
    }

    /**
     * Reduces the current value of the city with the given amount.
     * @param amount      The amount we wish to reduce the City's value.  
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
     * Ranks two Cities according to their names. 
     * @param       City to be compared to this City.
     * @return      ************************************************************************
     */
    public int compareTo(City city){
        return name.compareTo(city.name);
    }

    /**
     * Returns a reference to the Country in which the City resides.
     * @return      Country in which the city resides. 
     */
    public Country getCountry(){
        return country;
    }
    
    /**
     * Returns a bonus based upon the current value of the city.
     * @return      An integer bonus based on the current value of city. 
     */
    public int arrive(){
        int bonus = country.bonus(value);
        value -= bonus;
        return bonus;
    }
}
