
/**
 * Write a description of class test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class test
{
    public static void test(){
        Dodgeball b = new Dodgeball();
        b.addPlayer(1);
        b.addPlayer(7);
        b.addPlayer(20);
        b.print();
        System.out.println(b.throwBall(18));
        b.print();
    }
}
