import java.util.*;
/**
 * Write a description of class City here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class City implements Comparable<City>
{
    private String name;
    private int value;
    private int initialValue;
    private Country country;
    
    public City(String name, int value, Country country){
        this.name           = name;
        this.value          = value;
        this.initialValue   = value;
        this.country        = country;
    }
    
    public String getName()             {return this.name;                          }
    public int getValue()               {return this.value;                         }
    public void changeValue(int amount) {this.value += amount;                      }
    public void reset()                 {this.value = this.initialValue;            }
    public int compareTo(City c)        {return this.name.compareTo(c.getName());   }
    public Country getCountry()         {return this.country;                       }
    
    public int arrive(){
        int bonus   = country.bonus(this.value);
        this.value -= bonus;
        return bonus;
    }
}