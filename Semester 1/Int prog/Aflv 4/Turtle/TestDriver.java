
/**
 * Write a description of class TestDriver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TestDriver
{
    // instance variables - replace the example below with your own
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    
    public static void test()
    {
        Turtle turtle = new Turtle();
        turtle.triangle(50);
        turtle.move(100);
        turtle.square(50);
        turtle.move(100);
        turtle.turn(50);
        turtle.star(50);
        turtle.move(100);
        turtle.spiral(20,2.5);
        turtle.move(150);
        turtle.squares(5,75,10);
    }
    
    public static void test2(){
        Turtle turtle = new Turtle(100,100);
        turtle.turn(45);
        turtle.jumpTo(200,200);
        turtle.jump(100,10);
    }
}
