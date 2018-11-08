/**
 * Class to be debugged and tested
 * 
 * @author Kurt Jensen
 * @version Oct 2017-10-17
 */
public class Median {

    /** 
     * Returns the median of the three parameters (i.e. the numerical middle).
     * The method contains a bug, and hence it sometimes returns a wrong result
     */
    public static int medianWrong(int x, int y, int z) {
        int m = z;
        if(y < z) {
            if (x < y) { m = y; }
            else {
                if (x < z) { m = y; }
            }
        }
        else {
            if(x > y) { m = y; }
            else {
                if (x > z) { m = x; }
            }
        }
        return m;
    }

    /** 
     * Returns the median of the three parameters (i.e. the numerical middle).
     */
    public static int medianCorrect(int x, int y, int z) {
        int m = z;
        if(y < z) {
            if (x < y) { m = y; }
            else {
                if (x < z) { m = x; }
            }
        }
        else {
            if(x > y) { m = y; }
            else {
                if (x > z) { m = x; }
            }
        }
        return m;
    }

    public static int medianWrongPrintTest(int x, int y, int z) {
        int m = z;
        if(y < z) {
            if (x < y) { m = y; 
                System.out.println("The error is in line 53");}
            else {
                if (x < z) { m = y; 
                    System.out.println("The error is in line 56");}
                else {System.out.println("The error is after line 56");}
            }
        }
        else {
            if(x > y) { m = y; 
                System.out.println("The error is in line 62");}
            else {
                if (x > z) { m = x; 
                    System.out.println("The error is in line 65");}
                else {System.out.println("The error is after line 65");}
            }
        }
        return m;
    }
}
