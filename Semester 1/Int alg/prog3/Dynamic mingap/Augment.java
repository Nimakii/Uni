// Version: 2017103101
public class Augment {
    public int minGap;
    public int max;
    public int min;
    // If you need any additional fields, you can add them here

    public static Augment combine(Augment left, Augment right, int key) {
        Augment res = new Augment();
        // Implement your method here to combine the augmented data from
        // the left and right child with the node's key
        res.minGap = Math.min(Math.min(left.minGap , right.minGap)
                             ,Math.min(Math.abs(key-left.max) , Math.abs(right.min-key)));
        res.max = Math.max(key,right.max);
        res.min = Math.min(key,left.min);
        return res;
    }

    public static Augment leaf() {
        Augment res = new Augment();
        // Implement your method here to return the augmented data of a leaf
        res.minGap = 2000000001; //infty
        res.min = 2000000001;
        res.max = -2000000001;
        return res;
    }
}
