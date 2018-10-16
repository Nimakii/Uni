// Version: 2017100201
import java.io.*;
import java.util.*;
public class Dodgeball {
    // Add any private fields you might need here
    private ArrayList<Integer> line;
    private ArrayList<Integer> balls;
    
    public Dodgeball(){
        this.line = new ArrayList<Integer>();
        this.balls = new ArrayList<Integer>();
    }
    
    public int binS(Integer x, ArrayList<Integer> input, Integer p, Integer r){
        int low = p;
        int high = Math.max(p,r+1);
        while (low < high){
            int mid = (low + high)/2;
            if(x <= input.get(mid)){
                high = mid;
            }
            else{
                low = mid +1;
            }
        }
        return high;
    }
    public ArrayList<Integer> getP(){
        int abe = 0;
        return line;
    }
    
    public void addPlayer(int x) {
        if(line.isEmpty()){
            line.add(x);
        }
        else{
            Integer index = binS(x, line, 0 , line.size()-1);
            line.add(index, x);
        }
    }
    
    public void print(){
        line.forEach(l -> System.out.println(l));
    }

    public int throwBall(int x) {
        // Implement your code here to update the line of players and return the distance
        
        int play = binS(x,line,0,line.size()-1);
        /**System.out.print("play:"+play+" ");
        line.forEach(l->System.out.print(l+" "));
        System.out.println();**/
        if(play == line.size()){
            int res= x-line.get(line.size()-1);
            line.set(line.size()-1,x);
            return res;
        }
        else if(play==0){
            if(line.get(0)==x){
             line.remove(0);
             return 0;
            }
            //If the ball value is less than the first value at index 0
            else{
                int res = line.get(0)-x;
                line.set(0,x);
                return res;
            }
            
        }
        else if (play>0 && play < line.size()){
            if(line.get(play)==x){
                line.remove(play);
                return 0;
            }
            else if(Math.abs(line.get(play-1)-x)<=Math.abs(line.get(play)-x)) {
                int res = line.get(play-1)-x;
                line.set(play-1,x);
                return Math.abs(res);
                
            }
            else{
                int res = x-line.get(play);
                line.set(play,x);
                return Math.abs(res);
            }
            
            
        }
        
        return 42;
    }
}
