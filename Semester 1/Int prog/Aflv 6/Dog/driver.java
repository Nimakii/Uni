
/**
 * Write a description of class driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class driver
{
    public static void exam(){
        Dog d1 = new Dog("Fido","Schnauzer",true, 14);
        Dog d2 = new Dog("Nellie", "Schnauzer", true, 7);
        Dog d3 = new Dog("Ollie", "Gadekryds", false, 4);
        Dog d4 = new Dog("Caesar", "Pavelion", true, 0);
        Dog d5 = new Dog("Lola", "Dalmation", true, 6);
        
        System.out.println(d1.toString());
        System.out.println(d2.toString());
        System.out.println(d3.toString());
        System.out.println(d4.toString());
        System.out.println(d5.toString());
        
        Kennel myKennel = new Kennel("JK's funhouse");
        myKennel.add(d1);
        myKennel.add(d2);
        myKennel.add(d3);
        myKennel.add(d4);
        myKennel.add(d5);
        System.out.println();
        System.out.println("The kennels purebred dogs have a total age of " + myKennel.totalAgeOfPurebred());
        System.out.println();
        System.out.println("The youngest Schnauzer is " + myKennel.youngestOfBreed("Schnauzer").getName());
        System.out.println();
        myKennel.printKennel();
    }
}
