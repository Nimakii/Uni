import java.util.*;
/**
 * Write a description of class test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class test
{
    public static void testMe(){
        Dodgeball d = new Dodgeball();
        d.addPlayer(2);
        d.addPlayer(1);
        d.print();
        System.out.println();
        d.addPlayer(2);
        d.print();
        System.out.println();
        d.addPlayer(4);
        d.print();
        System.out.println();
        d.addPlayer(3);
        d.print();
        System.out.println(binS(-3,d.getP(),0,4));
        System.out.println(binS(8,d.getP(),0,4));
        System.out.println(binS(1,d.getP(),0,4));
    }
    
    public static int binS(Integer x, ArrayList<Integer> input, Integer p, Integer r){
        int low = p;
        int high = Math.max(p,r+1);
        while (low < high){
            int mid = (low + high)/2;
            if(x <= input.get(mid)){
                high = mid;
            }
            else{
                low = mid +1;
            }
        }
        return high;
    }
}
