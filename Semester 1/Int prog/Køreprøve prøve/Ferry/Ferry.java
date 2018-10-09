import java.util.*;
/**
 * Write a description of class was here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Ferry implements Comparable<Ferry>
{
    private String name;
    private int length;
    private int width;
    
    public Ferry(String name, int length, int width){
        this.name = name;
        this.length = length;
        this.width = width;
    }
    
    public String toString(){
        return name + " " + length + " x " + width + " meter";
    }
    
    public String getName(){return name;}
    public int getLength(){return length;}
    public int getWidth(){return width;}
    
    public int compareTo(Ferry f){
        if(this.name.equals(f.name)){
            if(this.length == f.length){
                return this.width - f.width;
            }
            return this.length - f.length;
        }
        return this.name.compareTo(f.name);
    }
}
