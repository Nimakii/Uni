// Version: 2017100201
import java.io.*;
import java.util.*;

public class Median {
    // Add your private fields here
    private TreeMap<Integer,Integer> tree;
    private int nodeCount;
    public Median(){
        tree = new TreeMap<Integer,Integer>();
        nodeCount = 0;
    }
    
    public void add(int x) {
        // Implement your method to add x to the data structure
        tree.put(x,nodeCount);
        nodeCount++;
    }

    public int median() {
        // Implement your method to return the median of the numbers added so far
        return 0;
    }
}