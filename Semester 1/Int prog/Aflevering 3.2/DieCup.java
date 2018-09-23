import java.util.ArrayList;

/**
 * This class models a DieCup (raflebæger)
 * 
 * @author Kurt Jensen
 * @version 13/9-2018 TDV
 **/
public class DieCup {
    private ArrayList<Die> dies;
    private int maxEyes; //Highest eyes
    private int eyesRolled; //Used to return the current rolled number of eyes
    /**
     * Constructor for DieCup objects, choose how many dies should be in the cup.
     */
    public DieCup(ArrayList<Integer> newDies)  {
       dies = new ArrayList<>();
       
       if(newDies.size() >0){
           for(int i : newDies){
               Die d1 = new Die(i); 
               dies.add(d1);
               
           }   
       }
       else{
           System.out.println("Passende besked, der skal mindst være en terning");
       }
    }

    /**
     * Obtain a new number of eyes for all dies
     */
    public void roll() {
        eyesRolled=0;
        for(Die die : dies){
            die.roll();           
            eyesRolled += die.getEyes();
            
            
        }
        if(eyesRolled > maxEyes){
                maxEyes = eyesRolled;
        }
        
    }

    /**
     * Return the sum of the number of eyes shown by the number of dies in the cup.
     */
    public int getEyes() {
        return eyesRolled;
    }

    /**
     * Return the highest numbers of eyes rolled 
     */
    public int getMaxEyes() {
        return maxEyes;
    }

    /**
     * Reset the variable maxEyes
     */
    public void resetMaxEyes(){
        maxEyes = 0;
    }

    

    
}
