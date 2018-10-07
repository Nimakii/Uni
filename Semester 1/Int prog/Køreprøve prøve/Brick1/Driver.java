
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Brick b1 = new Brick("Green", 25,10, 20);
        Brick b2 = new Brick("Blue", 20, 20,5);
        Brick b3 = new Brick("Purple", 30, 10,5);
	Brick b4 = new Brick("Blue", 25,5,5);
	Brick b5 = new Brick("Cyan", 10, 10,15);
        
        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);
        System.out.println(b4);
        System.out.println(b5);
        
        LegoBox myBox = new LegoBox("The box!");
        myBox.add(b1);
        myBox.add(b2);
        myBox.add(b3);
        myBox.add(b4);
        myBox.add(b5);
        System.out.println(myBox.area("Blue") + "<-imperial    functional->" + myBox.areaF("red"));
        System.out.println(myBox.area("Purple"));
        System.out.println(myBox.most() + "<-imperial    functional->" + myBox.mostF());
        myBox.printLegoBox();
        myBox.printLegoBoxF();
    }
}
