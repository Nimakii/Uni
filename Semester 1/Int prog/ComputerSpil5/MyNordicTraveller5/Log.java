import java.util.*;
import java.io.*;
/**
 * This class represents a saved replay of a game.
 *
 * @author Thomas D Vinther & Jens Kristian R Nielsen
 * @version CG5
 */
public class Log implements  Serializable
{
    private int seed;
    private Settings settings;
    private Map<Integer,String> choices;
    /**
     * Creates an empty Log with the settings used in the game, and the seed that the random used.
     * 
     * @param seed       the seed the game used
     * @param settings   the settings the game used
     */
    public Log(int seed, Settings settings){
        this.seed = seed;
        this.settings = settings;
        this.choices = new HashMap();
    }
    
    /**
     * This method returns the initial seed-value that was used in the game.
     * 
     * @return the integer seed value.
     */
    public int getSeed(){
        return seed;
    }
    
    /**
     * This method returns the settings that was used in the game.
     * 
     * @return the settings.
     */
    public Settings getSettings(){
        return settings;
    }
    
    /**
     * This method returns the name of the latest city chosen at time t, or null if no choice was made.
     * 
     * @param t     integer representing time t.
     * @return      the name of the latest chosen city at time t as a String, or null.
     */
    public String getChoice(int t){
        return choices.get(t);
    }
    
    /**
     * This method naively saves the name of a City at time t.
     * 
     * @param t the time we wish to save to.
     * @param c the City whos name we wish to save.
     */
    public void add(int t,City c){
        choices.put(t,c.getName());
    }

}
