// import class for generation of random numbers
import java.util.Random;

/**
 * This class models a Die (terning)
 * 
 * @author Kurt Jensen
 * @version 2017-01-05
 */
public class Die {
    private Random random;   //used for generation of random numbers
    private int eyes;        //number of eyes shown

    /**
     * Constructor for Die objects
     */
    public Die() {
        random = new Random();
        roll();
    }

    /**
     * Obtain a new number of eyes for this die
     */
    public void roll() {
        eyes = random.nextInt(6)+1;
    }

    /**
     * Return the number of eyes shown by this die
     */
    public int getEyes() {
        return eyes;
    }
}
