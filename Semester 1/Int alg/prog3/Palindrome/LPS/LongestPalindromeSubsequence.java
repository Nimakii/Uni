// Version: 2017113001
import java.io.*;
import java.util.*;
public class LongestPalindromeSubsequence {
    public ArrayList<Character> longestPalindromeSubsequence(ArrayList<Character> input) {
        ArrayList<Character> result = new ArrayList<>();

        int n = input.size();
        ArrayList<Character> Y = new ArrayList<Character>();
        for(int i = n-1; i>=0; i--){
            Y.add(input.get(i));
        }
        
        int[][] c = new int[n+1][n+1];
        int[][] b = new int[n][n];
        for(int i=0; i<=n;i++){
            c[i][0] = 0;
            c[0][i] = 0;
        }
        for(int i=1; i<=n; i++){
            for(int j=1; j<=n; j++){
                if(input.get(i-1).equals(Y.get(j-1))){
                    c[i][j] = c[i-1][j-1]+1;
                    b[i-1][j-1]=1;
                }
                else if(c[i-1][j] >= c[i][j-1]){
                    c[i][j] = c[i-1][j];
                    b[i-1][j-1]=2;
                }
                else {
                    c[i][j] = c[i][j-1];
                    b[i-1][j-1]=3;
                }
            }
        }

        return print(b,input,n,n,result);
    }

    public ArrayList<Character> print(int[][] b, ArrayList<Character> input, int i, int j
                                                ,ArrayList<Character> result){
        if(i == 0 || j == 0){ 
            return result;
        }
        if(b[i-1][j-1]==1){
            print(b,input,i-1,j-1,result);
            result.add(input.get(i-1));
        }
        else if(b[i-1][j-1]==2){
            print(b,input,i-1,j,result);
        }
        else {
            print(b,input,i,j-1,result);
        }
        return result;
    }

    public static void testAll() {
        clearTerminal();
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
    }

    public static void test1() {
        char[] input = { 'b', 'c', 'a', 'b', 'c', 'a', 'b', 'a' };
        char[] correctAnswer = { 'b', 'a', 'b', 'a', 'b' };

        try {
            checkPal("test1", input, correctAnswer);
        } catch (Exception e) {
            e.printStackTrace();
            outputFail("test1", "Exception: " + e);
            return;
        }
    }

    public static void test2() {
        char[] input = { 'a', 'i', 'b', 'o', 'h', 'p', 'h', 'o', 'b', 'i', 'a' };
        char[] correctAnswer = { 'a', 'i', 'b', 'o', 'h', 'p', 'h', 'o', 'b', 'i', 'a' };

        try {
            checkPal("test2", input, correctAnswer);
        } catch (Exception e) {
            e.printStackTrace();
            outputFail("test2", "Exception: " + e);
            return;
        }
    }

    public static void test3() {
        char[] input = { 'r', 'h', 'o', 't', 'a', 't', 'h', 'o', 'r', 'y' };
        char[] correctAnswer = { 'r', 'o', 't', 'a', 't', 'o', 'r' };

        try {
            checkPal("test3", input, correctAnswer);
        } catch (Exception e) {
            e.printStackTrace();
            outputFail("test3", "Exception: " + e);
            return;
        }
    }

    public static void test4() {
        char[] input = { 'r', 'a', 'h', 'a', 'n', 'n', 'a', 'h' };
        char[] correctAnswer = { 'h', 'a', 'n', 'n', 'a', 'h' };

        try {
            checkPal("test4", input, correctAnswer);
        } catch (Exception e) {
            e.printStackTrace();
            outputFail("test4", "Exception: " + e);
            return;
        }
    }

    public static void test5() {
        char[] input = { 'a', 'c', 'b', 'a', 'c' };
        char[] correctAnswer = { 'a', 'b', 'a' };

        try {
            checkPal("test5", input, correctAnswer);
        } catch (Exception e) {
            e.printStackTrace();
            outputFail("test5", "Exception: " + e);
            return;
        }
    }

    public static void test6() {
        char[] input = "aebecdceeccbadce".toCharArray();
        char[] correctAnswer = "abcceeccba".toCharArray();

        try {
            checkPal("test6", input, correctAnswer);
        } catch (Exception e) {
            e.printStackTrace();
            outputFail("test6", "Exception: " + e);
            return;
        }
    }

    public static void test7() {
        char[] input = "abcbda".toCharArray();
        char[] correctAnswer = "abcba".toCharArray();

        try {
            checkPal("test7", input, correctAnswer);
        } catch (Exception e) {
            e.printStackTrace();
            outputFail("test7", "Exception: " + e);
            return;
        }
    }

    private static void checkPal(String testName, char[] input, char[] correctAnswer) {
        ArrayList<Character> list = new ArrayList<>();
        for (char c : input) list.add(c);

        ArrayList<Character> answer = (
            new LongestPalindromeSubsequence().longestPalindromeSubsequence(list));

        if (answer.size() != correctAnswer.length) {
            outputFail(testName,
                       "Expected palindrome of length " + correctAnswer.length + ", got " + answer);
            return;
        }

        int j = 0;
        for (int i : input) {
            if (j < answer.size() && answer.get(j) == i) j++;
        }
        if (j < answer.size()) {
            outputFail(testName, "Not a subsequence of the input: " + answer);
            return;
        }

        for (int i = 0; i < answer.size()-1-i; i++)
            if (answer.get(i) != answer.get(answer.size()-1-i)) {
                outputFail(testName, "Not a palindrome: " + answer);
                return;
            }

        outputPass(testName);
    }

    private static void clearTerminal() {
        System.out.print('\u000C');
    }

    private static void outputPass(String testName) {
        System.out.println("[Pass " + testName + "]");
    }

    private static void outputFail(String testName, String message) {
        System.out.println("[FAIL " + testName + "] " + message);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testcases = sc.nextInt();
        if (testcases == 0) testAll();
        for (int t = 0; t < testcases; ++t) {
            int n = sc.nextInt();
            ArrayList<Character> list = new ArrayList<>();
            for (int i = 0; i < n; ++i) list.add(sc.next().charAt(0));
            LongestPalindromeSubsequence lis = new LongestPalindromeSubsequence();
            ArrayList<Character> pal = lis.longestPalindromeSubsequence(list);
            System.out.println(pal.size());
            for (int i = 0; i < pal.size(); i++)
                System.out.println(pal.get(i));
        }
    }
}