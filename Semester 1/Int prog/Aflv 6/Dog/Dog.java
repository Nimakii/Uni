import java.util.*;
/**
 * Write a description of class Dog here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Dog implements Comparable<Dog>
{
    private String name;
    private String breed;
    private boolean purebred;
    private int age;
    
    public Dog(String name, String breed, boolean purebred, int age){
        this.name = name;
        this.breed = breed;
        this.purebred = purebred;
        this.age = age;
    }
    
    public String toString(){
        if (purebred){
            return(age + " year old purebred " + breed + " named " + name); 
        }
        else{
            return(age + " year old not purebred " + breed + " named " + name);
        }
    }
    
    public String getName(){
        return name;
    }
    
    public String getBreed(){
        return breed;
    }
    
    public boolean getPurebred(){
        return purebred;
    }
    
    public int getAge(){
        return age;
    }
    
    public int compareTo(Dog d){
        if (!this.breed.equals(d.getBreed())){
            return this.breed.compareTo(d.getBreed());
        }
        else{
            return this.age - d.age;
        }
    }
}
