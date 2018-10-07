import java.util.*;
/**
 * Write a description of class Kennel here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Kennel
{
    private String owner;
    private ArrayList<Dog> dogs;
    
    public Kennel(String owner){
        this.owner = owner;
        dogs = new ArrayList<Dog>();
    }
    
    public void add(Dog d){
        dogs.add(d);
    }
    
    public void remove(Dog d){
        dogs.remove(d);
    }
    
    public int totalAgeOfPurebred(){
        int result = 0;
        for (Dog d : dogs){
            if (d.getPurebred()){
                result += d.getAge();
            }
        }
        
        return result;
    }
    
    public Dog youngestOfBreed(String breed){
        Dog youngest = null;
        for (Dog d : dogs){
            if (youngest == null || (d.getBreed().equals(breed) && d.getAge() < youngest.getAge())){
                youngest = d;
            }
        }
        return youngest;
    }
    
    public void printKennel(){
        System.out.println("The kennel owned by " + this.owner + " has the following dogs:");
        Collections.sort(dogs);
        for (Dog d : dogs){
            System.out.println(d.toString());
        }
    }
}
