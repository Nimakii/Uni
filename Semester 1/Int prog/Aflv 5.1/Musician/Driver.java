
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Musician m1 = new Musician("Børge", "harpe", 3);
        Musician m2 = new Musician("Svend Åge", "kat", 4);
        Musician m3 = new Musician("Johanne","stemmebånd",3);
        Musician m4 = new Musician("Kaja","kød fløjte",100);
        Musician m5 = new Musician("Simon","stemmebånd",1);
        
        System.out.println(m1.toString());
        System.out.println(m2.toString());
        System.out.println(m3.toString());
        System.out.println(m4.toString());
        System.out.println(m5.toString());
        
        Band MalkDeKoijn = new Band("Malk");
        MalkDeKoijn.add(m1);
        MalkDeKoijn.add(m2);
        MalkDeKoijn.add(m3);
        MalkDeKoijn.add(m4);
        MalkDeKoijn.add(m5);
        
        System.out.println();
        System.out.println("Der er " + MalkDeKoijn.skilledMusicians(4) + " Musikere med skill level højere end 4");
        
        System.out.println();
        if (MalkDeKoijn.withInstrument("Banjo") == null){
            System.out.println("Ingen medlemmer af bandet spiller Banjo");
        }
        else{
            System.out.println(MalkDeKoijn.withInstrument("Banjo").getName() + " spiller Banjo");
        }
        
        System.out.println();
        MalkDeKoijn.printBand();
    }
}
