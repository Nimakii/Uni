
/**
 * Write a description of class was here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Vegetable v1 = new Vegetable("carrots",true,200);
        Vegetable v2 = new Vegetable("carrots",false,50);
        Vegetable v3 = new Vegetable("tatoes",false,200);
        Vegetable v4 = new Vegetable("tatoes",true,75);
        Vegetable v5 = new Vegetable("greens",false,250);
        System.out.println(v1);
        System.out.println(v2);
        System.out.println(v3);
        System.out.println(v4);
        System.out.println(v5);
        Shop myShop = new Shop("JK");
        myShop.add(v1);
        myShop.add(v2);
        myShop.add(v3);
        myShop.add(v4);
        myShop.add(v5);
        System.out.println();
        System.out.println("Imperative");
        System.out.println(myShop.findI(false));
        System.out.println(myShop.organicI());
        myShop.printShopI();
        System.out.println();
        System.out.println("Functional");
        System.out.println(myShop.findF(false));
        System.out.println(myShop.organicF());
        myShop.printShopF();
    }
    public static void eksspg2Test(){
        Vegetable v1 = new Vegetable("carrots",true,2);
        Vegetable v2 = new Vegetable("carrots",false,50);
        Vegetable v3 = new Vegetable("tatoes",false,2);
        Vegetable v4 = new Vegetable("tatoes",true,75);
        Vegetable v5 = new Vegetable("greens",false,250);
        Shop myShop = new Shop("JK");
        myShop.add(v1);
        myShop.add(v2);
        myShop.add(v3);
        myShop.add(v4);
        myShop.add(v5);
        System.out.println(myShop.eksSpgTest());
    }
}
