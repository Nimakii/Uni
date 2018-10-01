
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Animal a1 = new Animal("Elephant",17,24);
        Animal a2 = new Animal("Horse", 17,28);
        Animal a3 = new Animal("Emu", 20,15);
        Animal a4 = new Animal("Human", 30,30);
        Animal a5 = new Animal("Dog", 3,42);
        System.out.println("The Animals are:");
        System.out.println(a1.toString());
        System.out.println(a2.toString());
        System.out.println(a3.toString());
        System.out.println(a4.toString());
        System.out.println(a5.toString());
        Zoo givskud = new Zoo("Givskud zoo");
        givskud.add(a1);
        givskud.add(a2);
        givskud.add(a3);
        givskud.add(a4);
        givskud.add(a5);
        System.out.println();
        System.out.println("We test the animal counter");
        System.out.println("There are "+givskud.animals()+" animals in the zoo " + givskud.getName());
        System.out.println();
        System.out.println("The animal with the largest population is: "+givskud.largestPopulation().getName());
        System.out.println();
        givskud.printZoo();
    }
}
