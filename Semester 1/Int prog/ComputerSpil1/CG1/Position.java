
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
    private int  distance;
    private int  total;
    public Position(City from, City to, int distance){
        this.from     = from;
        this.to       = to;
        this.distance = distance;
        this.total    = distance;
    }
    
    public City getFrom()       {return this.from;}
    public City getTo()         {return this.to;}
    public int getDistance()    {return this.distance;}
    public int getTotal()       {return this.total;}
    public boolean hasArrived() {return this.distance == 0;}
    
    public boolean move(){
        if(this.distance > 0){
            this.distance--;
            return true;
        }
        return false;
    }
    public void turnAround(){
        City save     = this.from;
        this.from     = this.to;
        this.to       = save;
        this.distance = this.total-this.distance;
    }
}
