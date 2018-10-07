
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Tool t1 = new Tool("hammer", 240, 150);
        Tool t2 = new Tool("hammer time", 180, 1000000);
        Tool t3 = new Tool("tool", 50, 200);
        Tool t4 = new Tool("screw", 5, 15);
        Tool t5 = new Tool("screwdriver", 40, 200);
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
        System.out.println(t4);
        System.out.println(t5);
        ToolBox myBox = new ToolBox("JK");
        myBox.add(t1);
        myBox.add(t2);
        myBox.add(t3);
        myBox.add(t4);
        myBox.add(t5);
        System.out.println();
        System.out.println("Imperative");
        for (Tool t : myBox.heavyToolsI(200)){
            System.out.println(t);
        }
        System.out.println("light tool: " + myBox.lightToolI());
        myBox.printToolBoxI();
        
        System.out.println();
        System.out.println("Functional");
        myBox.heavyToolsF(200).forEach(t -> System.out.println(t));
        System.out.println("light tool: " + myBox.lightToolF());
        myBox.printToolBoxF();
        
    }
}
