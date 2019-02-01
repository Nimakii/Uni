// Version: 2017112101

public class Edge implements Comparable<Edge> {
    int from;
    int to;
    int weight;

    public Edge(int f, int t, int w) {
        from = f;
        to = t;
        weight = w;
    }
    
    public int getWeight(){
        return this.weight;
    }
    
    public void changeWeight(int change){
        this.weight += change;
    }
    
    public int compareTo(Edge e){
     return e.from-this.from;   
    }
}