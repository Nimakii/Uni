
/**
 * Write a description of class Test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MedianTests
{
    public static void testWrong(){
        Median m = new Median();
        System.out.println("test123 giver resultatet " + m.medianWrong(1,2,3));
        System.out.println("test132 giver resultatet " + m.medianWrong(1,3,2));
        System.out.println("test213 giver resultatet " + m.medianWrong(2,1,3));
        System.out.println("test231 giver resultatet " + m.medianWrong(2,3,1));
        System.out.println("test312 giver resultatet " + m.medianWrong(3,1,2));
        System.out.println("test321 giver resultatet " + m.medianWrong(3,2,1));
    }
    
    public static void testCorrect(){
        Median m = new Median();
        System.out.println("test123 giver resultatet " + m.medianCorrect(1,2,3));
        System.out.println("test132 giver resultatet " + m.medianCorrect(1,3,2));
        System.out.println("test213 giver resultatet " + m.medianCorrect(2,1,3));
        System.out.println("test231 giver resultatet " + m.medianCorrect(2,3,1));
        System.out.println("test312 giver resultatet " + m.medianCorrect(3,1,2));
        System.out.println("test321 giver resultatet " + m.medianCorrect(3,2,1));
        System.out.println("test-1-1-1 giver resultatet " + m.medianCorrect(-1,-1,-1));
        System.out.println("test122 giver resultatet " + m.medianCorrect(1,2,2));
        System.out.println("test221 giver resultatet " + m.medianCorrect(2,2,1));
    }
}
