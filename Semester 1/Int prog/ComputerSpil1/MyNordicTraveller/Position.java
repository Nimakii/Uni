
/**
 * Write a description of class Position here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Position
{
    private City from;
    private City to;
    private int distance;
    private int total;
    public Position(City from, City to, int distance){
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.total = distance;
    }
    
    public City getFrom(){return from;}
    public City getTo(){return to;}
    public int getDistance(){return distance;}
    public int getTotal(){return total;}
    public boolean hasArrived(){return distance == 0;}
    
    public boolean move(){
        if(distance > 0){
            distance--;
            return true;
        }
        return false;
    }
    public void turnAround(){
        City save = this.from;
        this.from = this.to;
        this.to = save;
        this.distance = this.total-this.distance;
    }
}
