// Version: 2017100201
import java.io.*;
import java.util.*;

public class Median {
    // Add your private fields here
    private RedBlackTree<Integer> tree;
    
    public Median(){
        tree = new RedBlackTree<Integer>();
    }

    public void add(int x) {
        // Implement your method to add x to the data structure
        tree.insert(x);
    }

    public int median() {
        // Implement your method to return the median of the numbers added so far
        
        ArrayList<Integer> res = new ArrayList<Integer>();
        RedBlackNode<Integer> nilz = new RedBlackNode<Integer>();
        while(!(tree.getRoot() == nilz)){
            RedBlackNode<Integer> minz = tree.treeMinimum(tree.getRoot());
            res.add(minz.getKey());
            tree.remove(minz);
        }
        return res.get(res.size()/2);
    }
}
