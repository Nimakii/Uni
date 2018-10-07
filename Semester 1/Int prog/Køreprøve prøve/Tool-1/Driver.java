
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Tool t1 = new Tool("screwdriver", true , 240);
        Tool t2 = new Tool("hammer", false , 35);
        Tool t3 = new Tool("dildo", true , 1240);
        Tool t4 = new Tool("dildo", false , 240);
        Tool t5 = new Tool("cork screw", false , 50);
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
        System.out.println(t4);
        System.out.println(t5);
        ToolBox myBox = new ToolBox("My toolbox");
        myBox.add(t1);
        myBox.add(t2);
        myBox.add(t3);
        myBox.add(t4);
        myBox.add(t5);
        System.out.println("Imperative:");
        for (Tool t : myBox.electricToolsI()){
            System.out.println(t);
        }
        System.out.println(myBox.priceI(false));
        myBox.printToolBoxI();
        System.out.println();
        System.out.println("Functional:");
        myBox.electricToolsF().forEach(t -> System.out.println(t));
        System.out.println(myBox.priceF(false));
        myBox.printToolBoxF();
    }
}
