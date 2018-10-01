import java.util.*;
/**
 * Write a description of class Zoo here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Zoo
{
    private String name;
    private ArrayList<Animal> animalList;
    
    public Zoo(String name){
        this.name = name;
        this.animalList = new ArrayList<Animal>();
    }
    
    public void add(Animal a){
        animalList.add(a);
    }
    
    public String getName(){
        return name;
    }
    
    public int animals(){
        int res = 0;
        for (Animal a : animalList){
            if (a.getFem()>0 && a.getMa()>0){
                res += a.getFem();
                res += a.getMa();
            }
        }
        return res;
    }
    
    public Animal largestPopulation(){
        Animal res = new Animal("",0,0);
        Animal check = new Animal("",0,0);
        for (Animal a : animalList){
            if (a.getFem() + a.getMa() > res.getFem() + res.getMa()){
                res = a;
            }
        }
        if (res.equals(check)){
            return null;
        }
        return res;
    }
    
    public void printZoo(){
        System.out.println("The renowned zoo "+this.name+" contains the following animals");
        Collections.sort(animalList);
        for (Animal a : animalList){
            System.out.println(a.toString());
        }
    }
}
