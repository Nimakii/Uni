// Version: 2017100201
import java.io.*;
import java.util.*;

public class Median {
    private PriorityQueue<Integer> queue;
    
    public Median(){
        queue = new PriorityQueue<>();
    }

    public void add(int x) {
        // Implement your method to add x to the data structure
        queue.add(x);
    }

    public int median() {
        // Implement your method to return the median of the numbers added so far
        PriorityQueue<Integer> weasel = new PriorityQueue<Integer>(queue);
        int initial = queue.size();
        //while(weasel.size()>(initial+1)/2){
        //    weasel.poll();   
        //}
        return Array.getInt(queue.toArray(), queue.size()/2);
    }
}
