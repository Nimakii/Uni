// Version: 2017103101
public class Augment {
    int size;
    // If you need any additional fields, you can add them here

    public static Augment combine(Augment left, Augment right, int key) {
        Augment res = new Augment();
        // Implement your method here to combine the augmented data from
        // the left and right child with the node's key
        res.size = left.size + right.size+1;
        return res;
    }

    public static Augment leaf() {
        Augment res = new Augment();
        // Implement your method here to return the augmented data of a leaf
        res.size = 0;
        return res;
    }
}
