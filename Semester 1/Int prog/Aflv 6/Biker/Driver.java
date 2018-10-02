import java.util.*;
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Biker b1 = new Biker("Ozzy Osbourne",2,false);
        Biker b2 = new Biker("Thomas",20,true); 
        Biker b3 = new Biker("John",2,false); 
        Biker b4 = new Biker("JK",5,true); 
        Biker b5 = new Biker("2Pac",15,false);
        
        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);
        System.out.println(b4);
        System.out.println(b5);
        
        MotorcycleClub HA = new MotorcycleClub("Hells Angels");
        HA.add(b1);
        HA.add(b2);
        HA.add(b3);
        HA.add(b4);
        HA.add(b5);
        System.out.println(HA.leastRespectedBiker());
        HA.readyBikers(2).forEach(b -> System.out.println(b.getName()));
        System.out.println();
        HA.printMotorcycleClub();
    }
}
