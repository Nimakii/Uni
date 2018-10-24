import java.util.*;
/**
 * Write a description of class was here.
 *
 * @author (your name)
 * @version (a version number or a date)
*/
public class Train implements Comparable<Train>
{
    private String departure;
    private String destination;
    private int price;
    
    public Train(String departure, String destination, int price){
        this.departure = departure;
        this.destination = destination;
        this.price = price;
    }
    
    public String toString(){
        return "From " + departure + " to " + destination + " for " + price + " DKK";
    }
    
    public String getDeparture(){
        return departure;
    }
    public String getDestination(){
        return destination;
    }
    public int getPrice(){
        return price;
    }
    
    public int compareTo(Train t){
        if(this.departure.equals(t.departure)){
            return this.price - t.price;
        }
        return this.departure.compareTo(t.departure);
    }
}
