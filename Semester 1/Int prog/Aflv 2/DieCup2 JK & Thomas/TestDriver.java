
/**
 * Opret klassen TestDriver og implementer nedenst�ende metode.
 * void test()
 * Metoden skal skabe et rafleb�ger med to terninger, foretage et kast med b�geret og dets to
 * terninger, for til sidste at udskrive antallet af �jne p� terminalen ved hj�lp af
 * System.out.println metoden, der blev beskrevet i en af forel�sningerne.
 * Hvis I foran metodens navn skriver public static i stedet for blot public, kan metoden kaldes
 * ved at h�jreklikke p� den lysebrune boks, der repr�senterer TestDriver klassen � i stedet for
 * f�rst at skulle skabe et TestDriver objekt og s� kalde metoden ved at h�jreklikke p� den r�de
 * boks, der repr�senterer dette objekt. Herved sparer I jer selv for arbejdet med, efter hver
 * overs�ttelse (compilering), at skabe et TestDriver objekt, som I ikke har andet at bruge til end
 * kaldet af test metoden.
 * 
 *
 * @Thomas & JK
 * @V 0
 */
public class TestDriver
{

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public static void test(int sides1, int sides2)
    {
        DieCup raflebaeger1;
        raflebaeger1 = new DieCup(sides1,sides2);
        raflebaeger1.roll();
        System.out.println("Rafleb�geret har sl�et " + raflebaeger1.getEyes());
        
    }
    
    public static void testMultiple(int noOfRolls, int sides1, int sides2){
        DieCup raflebaeger;
        raflebaeger = new DieCup(sides1,sides2);
        int count;
        count = 0;
        int sum;
        sum = 0;
        double result;
        result = 0;
        
        while (noOfRolls > count){
            count = count +1;
            raflebaeger.roll();
            System.out.println("Throw nr " + count + ": " + raflebaeger.getEyes());
            sum = sum + raflebaeger.getEyes();
        }
        result = 1.0 * sum/noOfRolls;
        System.out.println("Avg nr of eyes: " + result);
    }
    
    public static void compareDieCups(int s1, int s2, int s3, int s4, int noOfRolls){
        DieCup rafleb1;
        DieCup rafleb2;
        int win1;
        int win2;
        int draw;
        int count;
        rafleb1 = new DieCup(s1,s2);
        rafleb2 = new DieCup(s3,s4);
        win1 = 0;
        win2 = 0;
        draw = 0;
        count = 0;
        while (count < noOfRolls){
            rafleb1.roll();
            rafleb2.roll();
            if (rafleb1.getEyes() > rafleb2.getEyes()){
                win1 = win1+1;
            }
            if (rafleb2.getEyes() > rafleb1.getEyes()){
                win2 = win2+1;
            }
            if (rafleb1.getEyes() == rafleb2.getEyes()){
                draw = draw+1;
            }
            count = count+1;
        }
        
        System.out.println("DieCup 1 with "+s1+" and "+s2+" sides is highest: "+win1+" times");
        System.out.println("DieCup 2 with "+s3+" and "+s4+" sides is highest: "+win2+" times");
        System.out.println("Same score in both: "+draw+" times");
    }
}
