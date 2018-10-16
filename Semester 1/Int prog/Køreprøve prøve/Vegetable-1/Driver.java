
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Vegetable v1 = new Vegetable("Carrots",true,200);
        Vegetable v2 = new Vegetable("Carrots",false,100);
        Vegetable v3 = new Vegetable("Tatoes",true,125);
        Vegetable v4 = new Vegetable("Tatoes",false,50);
        Vegetable v5 = new Vegetable("Fries",false,220);
        System.out.println(v1);
        System.out.println(v2);
        System.out.println(v3);
        System.out.println(v4);
        System.out.println(v5);
        Shop myShop = new Shop("JK's store");
        myShop.add(v1);
        myShop.add(v2);
        myShop.add(v3);
        myShop.add(v4);
        myShop.add(v5);
        System.out.println();
        System.out.println("Imperative:");
        System.out.println(myShop.totalPriceI(true));
        System.out.println(myShop.organicI());
        myShop.printShopI();
        
        System.out.println();
        System.out.println("Functional:");
        System.out.println(myShop.totalPriceF(true));
        System.out.println(myShop.organicF());
        myShop.printShopF();
    }
}
