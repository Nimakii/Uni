
/**
 * Write a description of class wasd here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Biker
{
    private String name;
    private int bulletWounds;
    private boolean hospitalized;
    
    public Biker(String name, int bulletWounds, boolean hosp){
        this.name = name;
        this.bulletWounds = bulletWounds;
        this.hospitalized = hosp;
    }
    
    public String getName(){
        return name;
    }
    
    public int getBW(){
        return bulletWounds;
    }
    
    public boolean getHosp(){
        return hospitalized;
    }
    
    public String toString(){
        return("Name: " + name + ", Wounds: " + bulletWounds + ", Hospitalized: " + hospitalized);
    }
}
