
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
    Carpet c1 = new Carpet("black", "wool", 1250);
    Carpet c2 = new Carpet("blue", "wool", 1300);
    Carpet c3 = new Carpet("black", "peas", 120);
    Carpet c4 = new Carpet("blue", "peas", 1400);
    Carpet c5 = new Carpet("grey", "linnen", 5000);
    System.out.println(c1);
    System.out.println(c2);
    System.out.println(c3);
    System.out.println(c4);
    System.out.println(c5);
    Shop myShop = new Shop("Spobjergvej 29");
    myShop.add(c1);
    myShop.add(c2);
    myShop.add(c3);
    myShop.add(c4);
    myShop.add(c5);
    System.out.println(myShop.totalPriceI("wool") + "<- imperial  functional ->" 
                        + myShop.totalPriceF("wool"));
    System.out.println("Imperial");
    for (Carpet C : myShop.selectionI("black","wool")){
        System.out.println(C);
    }
    System.out.println("Functional");
    myShop.selectionF("black","wool").forEach(c -> System.out.println(c));
    myShop.printShopI();
    myShop.printShopF();
}
}
