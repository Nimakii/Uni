
/**
 * Write a description of class Driver here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Driver
{
    public static void exam(){
        Train t1 = new Train("Aarhus","Odense",150);
        Train t2 = new Train("Odense","Aarhus",155);
        Train t3 = new Train("Aarhus","Vejle",160);
        Train t4 = new Train("Vejle","Odense",100);
        Train t5 = new Train("Aarhus","Copenhagen",15);
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
        System.out.println(t4);
        System.out.println(t5);
        TrainStation mySt = new TrainStation("Aarhus");
        mySt.add(t1);
        mySt.add(t2);
        mySt.add(t3);
        mySt.add(t4);
        mySt.add(t5);
        System.out.println("My station has " + mySt.connectingTrains() + " connecting trains");
        System.out.println("The cheapest train to Odense is: " + mySt.cheapTrainTo("Odense"));
        mySt.printTrainStation();
        System.out.println();
        mySt.trainsFrom("Odense").forEach(t -> System.out.println(t));
        System.out.println("The cheapest train from Aarhus to Odense is: " + mySt.cheapTrain("Odense"));
    }
}
