

/**
 * This class models a DieCup (raflebæger)
 * 
 * @author Kurt Jensen
 * @version 2017-05-01
 **/
import java.util.ArrayList;
public class DieCup {
    private ArrayList<Die> dies;
    private int maxEyes; //take a wild guess
    private int currentRoll;
    
    /**
     * Constructor for DieCup objects
     */
    public DieCup(ArrayList<Integer> newDies) {
        int noOfDies = newDies.size();
        if (noOfDies < 1){
            System.out.println("en passende besked");
        }
        dies = new ArrayList<>();
        for (int i : newDies){
            Die d1 = new Die(i);
            dies.add(d1);
        }
        roll();
    }
    
     /**
     * Obtain a new number of eyes for both dies
     */
    public void roll() {
        currentRoll = 0;
        for (Die d : dies){
            d.roll();
            currentRoll += d.getEyes();
        }
        maxEyes = Math.max(currentRoll , maxEyes);
    }
    
    /**
     * Return the sum of the number of eyes shown by the two dies
     */
    public int getEyes() {
        return currentRoll;
    }
    
    /**
     * Return zhe maximum numberr of eyes EVER rolled
     */
    public int maxEyes(){
        return maxEyes;
    }
    
    /**
     * Reset the max eyes variable
     */
    public void resetMaxEyes(){
        maxEyes = 0;
    }
  
    public void mulitpleRolls(int noOfRolls){
        int count;
        count = 0;
        int sum;
        sum = 0;
        double result;
        result = 0;
        
        while (noOfRolls > count){
            count = count +1;
            roll();
            System.out.println("Throw nr " + count + ": " + getEyes());
            sum = sum + getEyes();
        }
        result = 1.0 * sum/noOfRolls;
        System.out.println("Avg nr of eyes: " + result);
    }
}
