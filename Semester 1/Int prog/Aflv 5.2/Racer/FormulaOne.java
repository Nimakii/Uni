import java.util.*;
/**
 * Write a description of class FormulaOne here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class FormulaOne
{
    private String name;
    private ArrayList<Racer> racers;
    
    public FormulaOne(String name){
        this.name = name;
        this.racers = new ArrayList<Racer>();
    }
    
    public void add(Racer r){
        racers.add(r);
    }
    
    public int averageTopSpeed(){
        int avg = 0;
        for (Racer r : racers){
            avg +=r.getSpeed();
        }
        return (int)(avg/racers.size());
    }
    
    public Racer fastestRacer(){
        Racer result = new Racer("null",0,0);
        for (Racer r : racers){
            if (r.getSpeed() > result.getSpeed()){
                result = r;
            }
        }
        return result;
    }
    
    public void printFormulaOne(){
        Collections.sort(racers);
        System.out.println("In this formula one "+name+" we have the contenders:");
        for ( Racer r : racers){
            System.out.println(r.toString());
        }
    }
}
