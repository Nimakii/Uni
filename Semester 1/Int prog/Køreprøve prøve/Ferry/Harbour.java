import java.util.*;
import java.util.stream.Collectors;
/**
 * Write a description of class Harbour here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Harbour
{
    private String name;
    private List<Ferry> furries;
    
    public Harbour(String name){
        this.name = name;
        this.furries = new ArrayList<Ferry>();
    }
    public void add(Ferry f){furries.add(f);}
    
    public ArrayList<Ferry> smallFerries(int maxLength){
        ArrayList<Ferry> res = new ArrayList<Ferry>();
        for(Ferry f : furries){
            if(f.getLength() < maxLength){
                res.add(f);
            }
        }
        return res;
    }
    
    public Ferry longFerry(String name){
        Ferry res = null;
        for(Ferry f : furries){
            if( f.getName().equals(name) && (res == null || f.getLength() > res.getLength())){
                res = f;
            }
        }
        return res;
    }
    
    public void printHarbour(){
        System.out.println("This harbour is called: " + name + " and has the following ferries:");
        Collections.sort(furries);
        for(Ferry f : furries){
            System.out.println(f);
        }
    }
    
    public List<Ferry> findFerries(int min, int max){
        return furries.stream().filter(f -> f.getWidth() > min && f.getWidth() < max)
                               .collect(Collectors.toList());
    }
    
    public int findLength(String name){
        return furries.stream().filter(f->f.getName().equals(name))
                               .mapToInt(f->f.getLength()).sum();
    }
}
