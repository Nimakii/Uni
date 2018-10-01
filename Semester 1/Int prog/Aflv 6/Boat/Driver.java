
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Boat b1 = new Boat("Maria", 400000, false);
        Boat b2 = new Boat("Speedy", 20000, true);
        Boat b3 = new Boat("Nimse", 400000, true);
        Boat b4 = new Boat("Nellie", 35000, false);
        Boat b5 = new Boat("Pop-eye", 1000000, true);
        
        System.out.println(b1.toString());
        System.out.println(b2.toString());
        System.out.println(b3.toString());
        System.out.println(b4.toString());
        System.out.println(b5.toString());
        
        Marina juelsminde = new Marina("Juelsminde lystbådehavn");
        juelsminde.add(b1);
        juelsminde.add(b2);
        juelsminde.add(b3);
        juelsminde.add(b4);
        juelsminde.add(b5);
        
        System.out.println();
        System.out.println("We test cheaperThanAndForSale with paramater 500 000");
        for (Boat b : juelsminde.cheaperThanAndForSale(500000)){
            System.out.println(b.toString());
        }
        System.out.println();
        System.out.println("The most expensive boat is: " + juelsminde.mostValuable().getName());
        System.out.println();
        juelsminde.printBoats();
    }
}
