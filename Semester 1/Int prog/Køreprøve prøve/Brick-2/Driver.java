
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Brick b1 = new Brick("blue", 100, 10);
        Brick b2 = new Brick("blue", 75, 25);
        Brick b3 = new Brick("black", 80, 15);
        Brick b4 = new Brick("red", 50, 30);
        Brick b5 = new Brick("black", 90, 10);
        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);
        System.out.println(b4);
        System.out.println(b5);
        LegoBox box = new LegoBox("JK");
        box.add(b1);
        box.add(b2);
        box.add(b3);
        box.add(b4);
        box.add(b5);
        System.out.println(box.weight("blue") + " <- imperial   functional -> " + box.weightF("blue"));
        System.out.println(box.heaviest() + " <- imperial   functional -> " + box.heaviestF());
        System.out.println("imperial");
        box.printLegoBox();
        System.out.println("functional");
        box.printLegoBoxF();
    }
}
