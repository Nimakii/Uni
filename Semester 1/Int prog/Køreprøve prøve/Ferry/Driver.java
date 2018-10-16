
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Ferry f1 = new Ferry("Nautilus",150,75);
        Ferry f2 = new Ferry("Nautilus",150,50);
        Ferry f3 = new Ferry("ferrari",250,25);
        Ferry f4 = new Ferry("ferrari",225,35);
        Ferry f5 = new Ferry("Honda",175,65);
        System.out.println(f1);
        System.out.println(f2);
        System.out.println(f3);
        System.out.println(f4);
        System.out.println(f5);
        Harbour aa = new Harbour("Aarhus havn");
        aa.add(f1);
        aa.add(f2);
        aa.add(f3);
        aa.add(f4);
        aa.add(f5);
        System.out.println("SmallFerries test:");
        for(Ferry f : aa.smallFerries(201)){
            System.out.println(f);
        }
        System.out.println("LongFerry test:");
        System.out.println(aa.longFerry("Nautilus"));
        System.out.println("sort test");
        aa.printHarbour();
        System.out.println("findFerries test:");
        aa.findFerries(30,60).forEach(f-> System.out.println(f));
        System.out.println("findLength test:");
        System.out.println(aa.findLength("ferrari"));
    }
}
