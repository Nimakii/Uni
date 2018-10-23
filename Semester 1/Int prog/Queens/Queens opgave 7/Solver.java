/**
 * This class attempts to solve the n queen problem
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
    private boolean showSolutions; //false as standard falue
    private long queenCount;
    private long legalCount;
    private long legalTime;

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
        queenCount = 0;
        legalCount = 0;
        legalTime = 0;
        long preTime = System.currentTimeMillis(); 
        System.out.println("************************************************");
        System.out.println("Solutions for " + noOfQueens + " queens");
        System.out.println();
        positionQueens(0);
        long nowTime = System.currentTimeMillis();
        System.out.println();
        System.out.println("A total of " + noOfSolutions + 
            " solutions were found in "+(nowTime-preTime)+" ms");
        System.out.println("************************************************");
        System.out.println(queenCount + " recursive calls used. " 
            + legalCount +" legal calls used");
        System.out.println(legalTime + " time spent in legal calls " + (double) legalTime/(nowTime-preTime) + " as a %");
    }

    /**
     * puts a queen in a legal position in the paramater row
     *
     * @param  row, the row we make a play in
     * @return void
     */
    private void positionQueens(int row)
    {
        queenCount++;
        if (row == 0){
            for(int i=0; i<noOfQueens;i++){
                queens[0]=i;
                positionQueens(1); //there is no need to check if a move is legal in the bottom row, since all moves here are.
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
                    break; //when we find a solution we needn't check the rest of the top row, saving us some time.
                }
            }
        }
    }

    /**
     * Is it legal to make a move in the row,col position?
     *
     * @param  row
     * @param  col
     * @return  boolean, is this move legal?
     */
    private boolean legal(int row, int col)
    {
        legalCount++;
        long startTime = System.currentTimeMillis();
        int down = row-1;
        int count = 1;
        while(down>=0){
            if(queens[down] == col || 
            queens[down] == col-count || 
            queens[down] == col+count){
                legalTime += System.currentTimeMillis()-startTime;
                return false;
            }
            count++;
            down--;
        }
        legalTime += System.currentTimeMillis()-startTime;
        return true;
    }

    /**
     * Do you want to see the solutions printed ?
     * 
     * @param set, set the showSolutions variable to set
     */
    public void setShowSolutions(boolean set){
        this.showSolutions = set;
    }

    /**
     * prints the solutions we have found
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
     * Finds solutions for each number of queens in the interval [min,max]
     * 
     * @param min smallest number
     * @param max highest number
     */
    public void findNoOfSolutions(int min, int max){
        long duration = 1;
        System.out.println("************************************************");
        System.out.println("Queens     Solutions      Time(ms)  Solutions/ms         queenCalls    queenCalls*noOfQ       legalCalls");
        for(int i = min; i<=max;i++){
            long preTime = System.currentTimeMillis();
            this.noOfQueens = i;
            queens = new int[noOfQueens];
            noOfSolutions = 0;
            queenCount = 0;
            legalCount = 0;
            legalTime = 0;
            positionQueens(0);
            long endTime = System.currentTimeMillis();
            duration = Math.max(endTime-preTime,1); //division by 0 is bad
            System.out.format("   %3d  %,12d     %,8d      %,8d   %,15d   %,15d  %,15d   %n", 
                noOfQueens, noOfSolutions, duration, noOfSolutions/duration, queenCount, noOfQueens*queenCount, legalCount);
            System.out.println(legalTime + " time spent in legal calls " + (double) legalTime/duration + " as a %");
        }
        System.out.println("************************************************");
    }

    public static void testSolver(){
        Solver s = new Solver();
        s.setShowSolutions(true);
        s.findAllSolutions(1);
        System.out.println();
        s.findAllSolutions(2);
        System.out.println();
        s.findAllSolutions(6);
        System.out.println();
        s.setShowSolutions(false);
        s.findNoOfSolutions(1,14);
    }
}
