
/**
 * Write a description of class test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class test
{
    public static void test(){
        Solver s = new Solver();
        for(int i = 1;i<11;i++){
            s.findAllSolutions(i);
        }
    }
    
    public static void test2(){
        Solver s = new Solver();
        s.findAllSolutions(16);
    }
    
    public static void test3(){
        Solver s = new Solver();
        s.findNoOfSolutions(5,12);
    }
}
