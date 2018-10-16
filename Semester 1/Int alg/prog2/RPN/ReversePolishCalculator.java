// Version: 2017100201
import java.io.*;
import java.util.*;
public class ReversePolishCalculator {
    private Stack<Integer> myStack;
    
    public ReversePolishCalculator(){
        myStack = new Stack<Integer>();
    }
    
    public void push(int n) {
        myStack.push(n);
    }

    public void plus() {
        // Implement your code here to pop two elements and push their sum
        //int a = myStack.pop();
        //int b = myStack.pop();
        //myStack.push(a+b);
        myStack.push(myStack.pop()+myStack.pop());
    }

    public void minus() {
        // Implement your code here to pop two elements and push their difference
        int a = myStack.pop();
        int b = myStack.pop();
        myStack.push(b-a);
    }

    public void times() {
        // Implement your code here to pop two elements and push their product
        int a = myStack.pop();
        int b = myStack.pop();
        myStack.push(a*b);
    }

    public int read() {
        // Implement your code here to read the top element from the stack (without removing it)
        return myStack.peek();
    }
}
