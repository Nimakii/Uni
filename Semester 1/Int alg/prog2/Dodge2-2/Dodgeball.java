// Version: 2017100201
import java.io.*;
import java.util.*;
public class Dodgeball {
    // Add any private fields you might need here
    private TreeMap<Integer,Integer> tree;
    private int nodeCount;
    public Dodgeball(){
        tree = new TreeMap<Integer,Integer>();
        nodeCount = 0;
    }
    
    public void addPlayer(int x) {
        // Implement your code here to add a player to the line
        tree.put(x,nodeCount);
        nodeCount++;
    }

    public int throwBall(int x) {
        int distance = 0;
        if(tree.containsKey(x)){
            tree.remove(x);
            return distance;
        } else {
            if (tree.firstKey()>x){
                distance = Math.abs(tree.firstKey()-x);
                tree.remove(tree.firstKey());
                addPlayer(x);
                return distance;
            } else if (tree.lastKey()<x){
                distance = Math.abs(tree.lastKey()-x);
                tree.remove(tree.lastKey());
                addPlayer(x);
                return distance;
            } else if(Math.abs(tree.lowerKey(x)-x) <= Math.abs(tree.higherKey(x)-x)){
                distance = Math.abs(tree.lowerKey(x)-x);
                tree.remove(tree.lowerKey(x));
                addPlayer(x);
                return distance;
            } else{
                distance = Math.abs(tree.higherKey(x)-x);
                tree.remove(tree.higherKey(x));
                addPlayer(x);
                return distance;
            }
        }
        // Implement your code here to update the line of players and return the distance
    }
    public void tb(int x){
        tree.remove(x);
    }
    
    public void print(){
        System.out.println();
        tree.keySet().forEach(k -> System.out.print(k+" "));
    }
}