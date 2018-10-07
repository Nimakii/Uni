
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Carpet c1 = new Carpet("grey",3,250);
        Carpet c2 = new Carpet("grey",2,125);
        Carpet c3 = new Carpet("blue",4,300);
        Carpet c4 = new Carpet("grey",3,225);
        Carpet c5 = new Carpet("blue",3,275);
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);
        System.out.println(c4);
        System.out.println(c5);
        Shop myShop = new Shop("Khaleds tæpper");
        myShop.add(c1);
        myShop.add(c2);
        myShop.add(c3);
        myShop.add(c4);
        myShop.add(c5);
        System.out.println(myShop.selectionI("blue") + "<- imperial");
        System.out.println("functional ->" + myShop.selectionF("blue"));
        System.out.println(myShop.cheapestI("grey") + "<- imperial  functional ->" + myShop.cheapestF("grey"));
        myShop.printShopI();
        myShop.printShopF();
    }
}
