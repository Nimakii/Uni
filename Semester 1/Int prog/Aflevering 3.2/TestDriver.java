import java.util.ArrayList;
/**
 * Lav en beskrivelse af klassen TestDriver her.
 * 
 * @author (dit navn her)
 * @version (versions nummer eller dato her)
 */
public class TestDriver
{
    private ArrayList<Integer> newDies;

    /**
     * Konstruktør for objekter af klassen TestDriver
     */
    public TestDriver()
    {
        newDies = new ArrayList<>();
        
               
    }
    
    /**
     * Creates an ArrayList containing each digit of the given digits. 
     */
    public ArrayList<Integer> createArrayList(int digits){
        int shorter = digits;
        int print = 0;
        ArrayList<Integer> arrayDigits = new ArrayList<>();
        while(shorter > 0){
            print = shorter % 10;
            arrayDigits.add(print);
            shorter = shorter/10;
            
        }
        return arrayDigits;
    }
    /**
     * Creates two cups, first with #noOfDies, second with #noOfDiesTwo. Roll the dies #noOfRolls and compare the cups. 
     */
    public void compareDieCups(int dc1, int dc2, int noOfRolls)
    {
        ArrayList<Integer> dc1Array = new ArrayList<>();
        ArrayList<Integer> dc2Array = new ArrayList<>();
        dc1Array = createArrayList(dc1);
        dc2Array = createArrayList(dc2);
        DieCup cup1 = new DieCup(dc1Array);    //Creates a diecup
        DieCup cup2 = new DieCup(dc2Array);    //Creates a diecup
        int count = 0;                      //Counter used for the while loop
        int cup1wins = 0;                   //Counter used to keep track of diecup 1 wins
        int cup2wins = 0;                   //Counter used to keep track of diecup 2 wins
        int sameScore = 0;                  //Counter used to keep track of ties

        while(noOfRolls > count){
            count ++;
            cup1.roll();
            cup2.roll();
            if(cup1.getEyes()>cup2.getEyes())
            {
                cup1wins = cup1wins +1;
            }
            if(cup2.getEyes()>cup1.getEyes())
            {
                cup2wins = cup2wins + 1; 
            }
            if(cup1.getEyes()==cup2.getEyes())
            {
                sameScore = sameScore +1;
            }

        }
        System.out.println("Diecup 1 with "+dc1Array+" sides is highest: "+cup1wins+" no of times"); 
        System.out.println("Diecup 2 with "+dc2Array+" sides is highest: "+cup2wins+" no of times");
        System.out.println("Same score in both: "+sameScore+" no of times");
    }
    
    /**
     * Creates a diecup with 4 dice, with sides 4, 6, 3 and 8. Throw the dies noOfRolls times, with #noOfDies. 
     * Print every roll and calculate the average.
     */
    public static void test4638(int noOfRolls){
        ArrayList<Integer> newDies = new ArrayList<>();
        newDies.add(4);
        newDies.add(6);
        newDies.add(3);
        newDies.add(8);
        DieCup cup1 = new DieCup(newDies);
        int count = 0;
        int sum = 0;

        while(noOfRolls > count ){
            count = count+ 1;
            cup1.roll();
            System.out.println("Throw no. "+count+": "+cup1.getEyes()); 
            sum = sum + cup1.getEyes(); 

        }
        System.out.println("Average no of eyes: "+1.0*sum/noOfRolls);

    }

    
}
