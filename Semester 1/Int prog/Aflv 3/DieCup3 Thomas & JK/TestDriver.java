
/**
 * Opret klassen TestDriver og implementer nedenstående metode.
 * void test()
 * Metoden skal skabe et raflebæger med to terninger, foretage et kast med bægeret og dets to
 * terninger, for til sidste at udskrive antallet af øjne på terminalen ved hjælp af
 * System.out.println metoden, der blev beskrevet i en af forelæsningerne.
 * Hvis I foran metodens navn skriver public static i stedet for blot public, kan metoden kaldes
 * ved at højreklikke på den lysebrune boks, der repræsenterer TestDriver klassen – i stedet for
 * først at skulle skabe et TestDriver objekt og så kalde metoden ved at højreklikke på den røde
 * boks, der repræsenterer dette objekt. Herved sparer I jer selv for arbejdet med, efter hver
 * oversættelse (compilering), at skabe et TestDriver objekt, som I ikke har andet at bruge til end
 * kaldet af test metoden.
 * 
 *
 * @Thomas & JK
 * @V 0
 */
import java.util.ArrayList;
public class TestDriver
{
    private DieCup rafle;
    public static void opgave3(){
        ArrayList<Integer> newDies = new ArrayList<>();
        newDies.add(4);
        newDies.add(6);
        newDies.add(3);
        newDies.add(8);
        DieCup rafle = new DieCup(newDies);
        System.out.println(rafle.getEyes());
        testMultiple(50,newDies);
    }
    
    public void test4638(int noOfRolls){
        ArrayList<Integer> newDies = new ArrayList<>();
        newDies.add(4);
        newDies.add(6);
        newDies.add(3);
        newDies.add(8);
        DieCup rafle = new DieCup(newDies);
        testMultiple(noOfRolls,newDies);
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public static void test(ArrayList<Integer> noOfDies)
    {
        DieCup raflebaeger1;
        raflebaeger1 = new DieCup(noOfDies);
        raflebaeger1.roll();
        System.out.println("Raflebægeret har slået " + raflebaeger1.getEyes());
        
    }
    
    public static void testMultiple(int noOfRolls, ArrayList<Integer> noOfDies){
        DieCup raflebaeger;
        raflebaeger = new DieCup(noOfDies);
        int count;
        count = 0;
        int sum;
        sum = 0;
        double result;
        result = 0;
        
        while (noOfRolls > count){
            count = count +1;
            raflebaeger.roll();
            System.out.println("Throw nr " + count + ": " + raflebaeger.getEyes());
            sum = sum + raflebaeger.getEyes();
        }
        result = 1.0 * sum/noOfRolls;
        System.out.println("Avg nr of eyes: " + result);
    }
    
    public static void compareDieCups(ArrayList<Integer> rafle1noOfDies, ArrayList<Integer> rafle2noOfDies, int noOfRolls){
        DieCup rafleb1;
        DieCup rafleb2;
        int win1;
        int win2;
        int draw;
        int count;
        rafleb1 = new DieCup(rafle1noOfDies);
        rafleb2 = new DieCup(rafle2noOfDies);
        win1 = 0;
        win2 = 0;
        draw = 0;
        count = 0;
        while (count < noOfRolls){
            rafleb1.roll();
            rafleb2.roll();
            if (rafleb1.getEyes() > rafleb2.getEyes()){
                win1 = win1+1;
            }
            if (rafleb2.getEyes() > rafleb1.getEyes()){
                win2 = win2+1;
            }
            if (rafleb1.getEyes() == rafleb2.getEyes()){
                draw = draw+1;
            }
            count = count+1;
        }
        
        System.out.println("DieCup 1 with "+rafle1noOfDies+" dies rolled highest: "+win1+" times");
        System.out.println("DieCup 2 with "+rafle2noOfDies+" dies rolled highest: "+win2+" times");
        System.out.println("Same score in both: "+draw+" times");
    }
}
