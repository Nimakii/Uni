/**
 * This class models a DieCup (raflebæger)
 * 
 * @author Kurt Jensen
 * @version 2017-05-01
 **/
public class DieCup {
    private Die d1;   //first die
    private Die d2;   //second die
    private int maxEyes; //take a wild guess
    
    /**
     * Constructor for DieCup objects
     */
    public DieCup(int d1eyes, int d2eyes) {
        d1 = new Die(d1eyes);
        d2 = new Die(d2eyes);
        maxEyes = d1.getEyes()+d2.getEyes();
    }
    
     /**
     * Obtain a new number of eyes for both dies
     */
    public void roll() {
        d1.roll();
        d2.roll();
        if (getEyes() > maxEyes){
            maxEyes =  getEyes();
        }
    }
    
    /**
     * Return the sum of the number of eyes shown by the two dies
     */
    public int getEyes() {
        return d1.getEyes() + d2.getEyes();
    }
    
    /**
     * Return zhe maximum numberr of eyes EVER rolled
     */
    public int maxEyes(){
        return maxEyes;
    }
    
    /**
     * Reset the max eyes variable
     */
    public void resetMaxEyes(){
        maxEyes = 0;
    }
    
    /**
     * Opgave 4
     */
    public void opgave4(){
        System.out.println("Vi benytter at slag med 2 terninger er uafhængige hændelser");
        System.out.println("P(d1=n , d2=m) = P(d1=n) P(d2=m) = 1/6*1/6 = 1/36");
        System.out.println("P(d1+d2=3) = P(d1=1,d2=2)+P(d1=2,d2=1) = 1/36+1/36 = 2/36");
        System.out.println("P(d1+d2=4) = P(d1=1,d2=3)+P(d1=2,d2=2)+P(d1=3,d2=3) = 1/36+1/36+1/36 = 3/36");
        System.out.println("P(d1+d2=5) = P(d1=1,d2=4)+P(d1=2,d2=3)+P(d1=3,d2=2)+P(d1=4,d2=1) = 1/36+1/36+1/36+1/36 = 4/36");
        System.out.println("P(d1+d2=6) = P(d1=1,d2=5)+P(d1=2,d2=4)+P(d1=3,d2=3)+P(d1=4,d2=2)+P(d1=5,d2=1) = 1/36+1/36+1/36+1/36+1/36 = 5/36");
        System.out.println("P(d1+d2=7) = P(d1=1,d2=6)+P(d1=2,d2=5)+P(d1=3,d2=4)+P(d1=4,d2=3)+P(d1=5,d2=2)+P(d1=6,d2=1) = 1/36+1/36+1/36+1/36+1/36+1/36 = 1/6");
        System.out.println("P(d1+d2=8) = P(d1=2,d2=6)+P(d1=2,d2=5)+P(d1=3,d2=4)+P(d1=4,d2=3)+P(d1=5,d2=2) = 1/36+1/36+1/36+1/36+1/36 = 5/36");
        System.out.println("P(d1+d2=9) = P(d1=3,d2=6)+P(d1=4,d2=5)+P(d1=5,d2=4)+P(d1=6,d2=3) = 1/36+1/36+1/36+1/36 = 4/36");
        System.out.println("P(d1+d2=10) = P(d1=4,d2=6)+P(d1=5,d2=5)+P(d1=6,d2=4) = 1/36+1/36+1/36 = 3/36");
        System.out.println("P(d1+d2=11) = P(d1=5,d2=6)+P(d1=6,d2=5) = 1/36+1/36 = 2/36");
        System.out.println("For at beregne middelværdien benyttes formlen");
        System.out.println("E[X] = sum_x x*P(X=x)");
        System.out.println("Vi beregner");
        System.out.println("E[X] = (2*1+3*2+4*3+5*4+6*5+7*6+8*5+9*4+10*3+11*2+12*1)/36 = 7");
        System.out.println("Som ønsket.");
        mulitpleRolls(20);
    }
    
    public void mulitpleRolls(int noOfRolls){
        int count;
        count = 0;
        int sum;
        sum = 0;
        double result;
        result = 0;
        
        while (noOfRolls > count){
            count = count +1;
            roll();
            System.out.println("Throw nr " + count + ": " + getEyes());
            sum = sum + getEyes();
        }
        result = 1.0 * sum/noOfRolls;
        System.out.println("Avg nr of eyes: " + result);
    }
}
