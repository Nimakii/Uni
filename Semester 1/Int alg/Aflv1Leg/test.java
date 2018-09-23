import java.util.ArrayList;
/**
 * Write a description of class test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class test
{
    // instance variables - replace the example below with your own

    /**
     * Constructor for objects of class test
     */
    public test()
    {

    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public static int sampleMethod()
    {
        double y = -5.5;
        int n = (int) Math.abs(y);
        return n;
    }
    public static int binSearch(double y, ArrayList<Double> X, int n, int m)
    { return 0;
    }
    
    public static int infSearchR(double y, ArrayList<Double> X)
    {
        if (y < X.get(1)){
            return 1;
        }
        int n = Math.max((int) Math.abs(y)+1, 1000);
        int i = 0;
        while (true){
            if (X.get(i*(n+1))<y && y<=X.get(n*(i+1))){
                return binSearch(y,X,i*n+1,n*(i+1));
            }
            i++;
        }
    }
}
