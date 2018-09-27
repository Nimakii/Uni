
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Racer r1 = new Racer("BMW",2012,180);
        Racer r2 = new Racer("BMW",2013,190);
        Racer r3 = new Racer("Ferrari",2012,170);
        Racer r4 = new Racer("Ferrari",2013,200);
        Racer r5 = new Racer("Citroen",2014,250);
        
        System.out.println("toString() tests");
        System.out.println(r1.toString());
        System.out.println(r2.toString());
        System.out.println(r3.toString());
        System.out.println(r4.toString());
        System.out.println(r5.toString());
        
        System.out.println();
        System.out.println("ArrayList test");
        FormulaOne f1 = new FormulaOne("Navn");
        f1.add(r1);
        f1.add(r2);
        f1.add(r3);
        f1.add(r4);
        f1.add(r5);
        System.out.println("The average top speed is: " + f1.averageTopSpeed());
        System.out.println("The fastest racer is: " + f1.fastestRacer().getName());
        System.out.println();
        f1.printFormulaOne();
    }
}
