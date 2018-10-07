import java.util.*;
import java.util.Optional;

/**
 * This class tests the Brick drivers license exam
 * Introduktion til Programmering, 2017
 *
 * @author Nikolaj Ignatieff Schwartzbach
 * @author Kristian Cordes-Andersen
 * @version 11/12/2017
 */
public class TestBrick1
{
	/**
	 * Run the main method from shell
	 */
	public static void main(String[] args){
		testBrick();
	}

	/** Labels for methods (gives less boilerplate code) */
	private static String[] labels = new String[]{"toString", "area", "most", "printLegoBox"};

	/** Brick objects */
	private static Brick b1,b2,b3,b4,b5;

	/** LegoBox */
	private static LegoBox legoBox;

	/**
	 * Tests the Brick class
	 */
	public static void testBrick() {
		boolean[] tests = new boolean[labels.length];

		// Five well-chosen examples
		b1 = new Brick("Green", 25,10, 20);
		b2 = new Brick("Blue", 20, 20,5);
		b3 = new Brick("Purple", 30, 10,5);
		b4 = new Brick("Blue", 25,5,5);
		b5 = new Brick("Cyan", 10, 10,15);

		// Create the LegoBox and add the bricks
		legoBox = new LegoBox("The Funky LegoBox");
		legoBox.add(b1);
		legoBox.add(b2);
		legoBox.add(b3);
		legoBox.add(b4);
		legoBox.add(b5);

		//Perform first three tests
		tests[0] = testToString();
		tests[1] = testArea();
		tests[2] = testMost();

		
        //Test printLegoBox
        boolean comparable = false;
        try{
            Comparable<Brick> b = (Comparable<Brick>) b1;
            comparable = true;
            tests[3] = testPrintLegoBox();
        } catch(ClassCastException e){
            System.out.println("compareTo has not been implemented.");
        }

        //Print results
        boolean success = true;
        System.out.println("Test Brick-1");
        for(int i=0; i<tests.length; i++){
            if(i==3&&!comparable)
                System.out.println("* "+labels[i]+": Not implemented.");
            else
            System.out.println("* "
                    + labels[i]
                    + ": Test "
                    + (tests[i] ? "successful!":"failed...")
                    + (i==3 ? " (here we only test that compareTo is correct)":""));

            success &= tests[i];
        }
        //Print overall
        System.out.println("Test "+(success?"successful!":"failed...")+"");
	}


	/** Expected result of toString() for every Brick */
	private static final String s1 = "25 Green (10 mm x 20 mm)",
			s2 = "20 Blue (20 mm x 5 mm)",
			s3 = "30 Purple (10 mm x 5 mm)",
			s4 = "25 Blue (5 mm x 5 mm)",
			s5 = "10 Cyan (10 mm x 15 mm)";

	/**
	 * Tests that toString works as expected.
	 * toString() is automatically called by Java on the objects hence we need not specify it.
	 * Will report errors to System.out
	 * @return whether the test was successful or not.
	 */
	private static boolean testToString(){
		boolean success = true;
		
		if (!(b1.toString()).equals(s1)) {
			printError("b1.toString()", s1, b1);
			success = false;
		}

		if (!(b2.toString()).equals(s2)) {
			printError("b2.toString()", s2, b2);
			success = false;
		}

		if (!(b3.toString()).equals(s3)) {
			printError("b3.toString()", s3, b3);
			success = false;
		}

		if (!(b4.toString()).equals(s4)) {
			printError("b4.toString()", s4, b4);
			success = false;
		}

		if (!(b5.toString()).equals(s5)) {
			printError("b5.toString()", s5, b5);
			success = false;
		}
		return success;
	}

	/**
	 * Tests area(String color)
	 * @return whether the test was successful or not.
	 */
	private static boolean testArea(){
		
		String blueInput = "Blue";
		String purpleInput = "Purple";
		int expectedBlueArea = 2000 + 25 * 25;
		int expectedPurpleArea = 1500;

		boolean success = true;

		if (legoBox.area(blueInput) != expectedBlueArea) {
			printError("legoBox.area(" + blueInput + ")", expectedBlueArea, legoBox.area(blueInput));
			success = false;
		}

		if (legoBox.area(purpleInput) != expectedPurpleArea) {
			printError("legoBox.area(" + purpleInput + ")", expectedPurpleArea, legoBox.area(purpleInput));
			success = false;
		}


		return success;
	}

	/**
	 * Tests most()
	 * @return whether the test was successful or not.
	 */
	private static boolean testMost() {
		boolean success = true;
		Brick most = unwrap(legoBox.most());

		if (most != b3) {
			printError("legoBox.most()", b3, most);
			success = false;
		}

		return success;
	}

	/**
	 * Tests the natural ordering of the Brick class
	 * Will report errors to System.out
	 * @return whether the test was successful or not.
	 */
	private static boolean testPrintLegoBox(){
		/* Expected sorting of the bricks */
		Brick[] correctResult = new Brick[]{b2, b4, b5, b1, b3};
        Comparable<Brick>[] comps = getComparables(correctResult);

		boolean success = true;

		// Holds the result of comparing i with j.
		int val;

		//For every mi
		for(int i = 0; i < correctResult.length; i++) {
			//For every mj, where j<=i
			for(int j = 0; j <= i; j++){

				//Test that mj.compareTo(mj) == 0
				val = comps[i].compareTo(correctResult[i]);
				if(j == i && val != 0){
					printError("b" + (i+1) + ".compareTo(b" + (i+1) + ")", 0, val);
					success = false;
				}

				//Test that mj.compareTo(mi) < 0 for i<j
				val = comps[j].compareTo(correctResult[i]);
				if(j != i && val >= 0){
					printError("b" + (j+1) + ".compareTo(b" + (i+1) + ")", "negative", val);
					success = false;
				}

				//Test that mi.compareTo(mj) > 0 for i<j
				val = comps[i].compareTo(correctResult[j]);
				if(j != i && val <= 0){
					printError("b" + (i+1) + ".compareTo(b"+(j+1)+ ")", "positive", val);
					success = false;
				}
			}
		}

		return success;
	}

	/**
	 * Formats the error message to be printed.
	 * Prints to System.err
	 * @param msg the message (usually the method name)
	 * @param exp the expected value
	 * @param rec the received value
	 */
	private static void printError(String msg, Object exp, Object rec) {
		System.err.println("Error in "+msg+"\n\tExpected: "+exp+"\n\tReceived: "+rec);
	}

	/**
	 * Unwraps a given Optional to the Animal-object.
	 * This is done to allow for handling assignments written using functional programming.
	 * @param object the object returned by the students method
	 * @return the unwrapped object of type Animal, null if object is not an Animal or Optional<Animal>
	 */
	private static Brick unwrap(Object object) {
		if (object instanceof Brick) {
			return (Brick) object;
		} else if (object instanceof Optional) {
			if ((((Optional<Brick>) object).isPresent()))
				return ((Optional<Brick>) object).get();
		}
		return null;
	}

    /**
     * This method converts an array of objects into an array of their corresponding Comparables.
     * If T does not implement Comparable<T>, this method will throw an error.
     * This function is used to ensure our test always compiles, even in absense of the method compareTo.
     * @param input An array of class T
     * @return An array of Comparable<T>, if T implements Comparable<T>; otherwise an error.
     */ 
    private static <T> Comparable<T>[] getComparables(T[] input){
        return new ArrayList<T>(Arrays.asList(input))
                  .stream()
                  .map(o -> (Comparable<T>) o)
                  .toArray(Comparable[]::new);

    }
}