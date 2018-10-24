// Version: 2017100201
import java.io.*;
import java.util.*;

public class Median {
    // Add your private fields here
    private TreeMap<Integer,Integer> treeLeast;
    private TreeMap<Integer,Integer> treeHigh;
    private int median;
    private int count;
    private boolean high;
    public Median(){
        treeLeast= new TreeMap<>();
        treeHigh= new TreeMap<>();
        this.median=0;
        this.count=0;
        this.high=true;
    }

    public void add(int x) {

        // Implement your method to add x to the data structure
        if(x>median){
            treeHigh.put(x,count);
            count++;
            if(treeLeast.size()<treeHigh.size()){
                int y = treeHigh.firstKey();   
                treeHigh.remove(y);
                treeLeast.put(y,count);
                count++;
            }
            high=true;
        }
        else{
            treeLeast.put(x,count);
            count++;
            if(treeLeast.size()>treeHigh.size()){
                int y = treeLeast.lastKey();   
                treeLeast.remove(y);
                treeHigh.put(y,count);
                count++;
            }
            high=false;
        }
        if(treeLeast.size()<=treeHigh.size()){
            median = treeHigh.firstKey();
        }
        else{
            median=treeLeast.lastKey();
        }
    }

    public int median() {
        return median;
    }
}