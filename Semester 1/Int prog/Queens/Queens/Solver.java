import java.lang.*;
/**
 * This class solves the n queen problem
 *
 * @author JKT 
 * @version 1
 */
public class Solver
{
    // instance variables - replace the example below with your own
    private int noOfQueens;
    private int[] queens;
    private int noOfSolutions;
    private boolean showSolutions;

    /**
     * Finds solutions to n queen problem
     *
     * @param  noOfQueens, the number of queens
     * @return void
     */
    public void findAllSolutions(int noOfQueens)
    {
        this.noOfQueens = noOfQueens;
        queens = new int[noOfQueens];
        noOfSolutions = 0;
        long startTime = System.currentTimeMillis(); //Used to calculate the starting time of the procedure.
        System.out.println("******************************************************");
        System.out.println("Solutions for " + noOfQueens + " queens");
        System.out.println();
        positionQueens(0);
        System.out.println();
        long endTime = System.currentTimeMillis();//Used to calculate the ending time of the procedure.
        System.out.println("A total of " + noOfSolutions + " solutions were found in "+(endTime-startTime)+ " ms");
        System.out.println("******************************************************");
    }

    /**
     * This method finds the number of solutions in the interval [min;max] and prints the solutions. 
     * 
     * @param min The interval starting point of the desired solutions.
     * @param max The interval ending point of the desired solutions. 
     */
    public void findNoOfSolutions(int min, int max){
        System.out.println("******************************************************");
        System.out.println("Queens       Solutions       Time(ms)      Solution/ms");
        for(int i = min; i<=max; i++){
            long duration = System.currentTimeMillis(); //Used to calculate the time for each procedure
            this.noOfQueens=i;
            queens = new int[noOfQueens];
            noOfSolutions = 0;
            positionQueens(0);
            duration=System.currentTimeMillis()-duration;
            duration = Math.max(duration,1);
            System.out.format("   %3d     %,12d    %,8d     %,8d %n",noOfQueens,noOfSolutions, duration,noOfSolutions/duration);
        }
        System.out.println("******************************************************");

    }

    /**
     * Puts a queen in a legal position in the paramater row
     *
     * @param  row, the row we make a play in
     * @return void
     */
    private void positionQueens(int row){
        if (row == 0){
            for(int i=0; i<noOfQueens;i++){
                queens[0]=i;
                positionQueens(1);
            }
        }
        if (row > 0 && row < noOfQueens-1){
            for(int i=0; i<noOfQueens;i++){
                if(legal(row,i)){
                    queens[row]=i;
                    positionQueens(row+1);
                }
            }
        }
        if (row == noOfQueens-1){
            for(int i=0; i<noOfQueens;i++){
                if(legal(row,i)){
                    queens[row]=i;
                    printSolution();
                    noOfSolutions++;
                }
            }
        }
    }

    /**
     * Is it legal to insert a queen in the row,col position?
     *
     * @param  row
     * @param  col
     * @return  boolean, is this move legal?
     */
    private boolean legal(int row, int col)
    {
        
        int down = row-1;
        int count = 1;
        while(down>=0){
            if(queens[down] == col || 
            queens[down] == col-count || 
            queens[down] == col+count){
                return false;
            }
            count++;
            down--;
        }
        return true;
    }

    /**
     * Prints the solutions we have found
     */
    private void printSolution()
    {
        if(showSolutions){
            for(int i = 0;i<queens.length;i++){
                System.out.print(convert(i,queens[i])+" ");
            }
            System.out.println();
        }
    }

    /**
     * Converts the row,col structure from {0,..,n-1}^2 to {a,..,n'th letter}x{1,..,n}
     *
     * @param  row
     * @param  col
     * @return "col,row"
     */
    private String convert(int row, int col)
    {
        char first = (char)(97+col);
        Integer second = row+1;
        return first +""+ second;
    }

    /**
     * Mutator method, decides if the solutions are printed or not. 
     */
    private void setShowSolution(boolean y){
        this.showSolutions=y;
    }

    /**
     * A test method that creates a new Solver object, makes three successive calls to the method findAllSolutions 
     * with paramteres 1, 2 and 6 and lastly one call to findNoOfSolutions(1,12)
     */
    public static void testSolver(){
        Solver s = new Solver();
        s.setShowSolution(true);
        s.findAllSolutions(1);
        System.out.println();
        s.findAllSolutions(2);
        System.out.println();
        s.findAllSolutions(6);
        System.out.println();
        s.setShowSolution(false);
        s.findNoOfSolutions(1,12);

    }
}