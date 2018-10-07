import java.util.*;
import java.util.Optional;

/**
 * This class tests the Brick drivers license exam
 * Introduktion til Programmering, 2017
 *
 * @author Nikolaj Ignatieff Schwartzbach
 * @author Kristian Cordes-Andersen
 * @version 28/09/2017
 */
public class TestBrick2
{
	/**
	 * Run the main method from shell
	 */
	public static void main(String[] args){
		testBrick();
	}

	/** Labels for methods (gives less boilerplate code) */
	private static String[] labels = new String[]{"toString", "weight", "heaviest", "printLegoBox"};

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
		b1 = new Brick("Green", 25,25);
		b2 = new Brick("Blue", 20, 20);
		b3 = new Brick("Cyan", 10, 15);
		b4 = new Brick("Purple", 30, 10);
		b5 = new Brick("Blue", 25,5);

		// Create the LegoBox and add the bricks
		legoBox = new LegoBox("The Funky LegoBox");
		legoBox.add(b1);
		legoBox.add(b2);
		legoBox.add(b3);
		legoBox.add(b4);
		legoBox.add(b5);

		//Perform first three tests
		tests[0] = testToString();
		tests[1] = testWeight();
		tests[2] = testHeaviest();

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
        System.out.println("Test Brick-2");
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
	private static final String s1 = "25 Green (25 gram each)",
			s2 = "20 Blue (20 gram each)",
			s3 = "10 Cyan (15 gram each)",
			s4 = "30 Purple (10 gram each)",
			s5 = "25 Blue (5 gram each)";

	/**
	 * Tests that toString works as expected
	 * Will report errors to System.out
	 * @return whether the test was successful or not.
	 */
	private static boolean testToString(){
		boolean success = true;

		if(!(b1.toString()).equals(s1)){
			printError("b1.toString()", s1, b1);
			success = false;
		}
		if(!(b2.toString()).equals(s2)){
			printError("b2.toString()", s2, b2);
			success = false;
		}
		if(!(b3.toString()).equals(s3)){
			printError("b3.toString()", s3, b3);
			success = false;
		}
		if(!(b4.toString()).equals(s4)){
			printError("b4.toString()", s4, b4);
			success = false;
		}
		if(!(b5.toString()).equals(s5)){
			printError("b5.toString()", s5, b5);
			success = false;
		}
		return success;
	}

	/**
	 * Tests weight(String color)
	 */
	private static boolean testWeight(){

		String blueInput = "Blue";
		String purpleInput = "Purple";
		int expectedBlueWeight = 525;
		int expectedPurpleWeight = 300;

		boolean success = true;

		if (legoBox.weight(blueInput) != expectedBlueWeight) {
			printError("LegoBox.weight(" + blueInput + ")", expectedBlueWeight, legoBox.weight(blueInput));
			success = false;
		}


		if (legoBox.weight(purpleInput) != expectedPurpleWeight) {
			printError("LegoBox.weight(" + purpleInput + ")", expectedPurpleWeight, legoBox.weight(purpleInput));
			success = false;
		}


		return success;
	}

	/**
	 * Tests heaviest()
	 * @return whether the test was successful or not.
	 */
	private static boolean testHeaviest() {
		boolean success = true;
		Brick heaviest = unwrap(legoBox.heaviest());

		if (heaviest != b1) {
			printError("LegoBox.heaviest()", b3, heaviest);
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
		Brick[] correctResult = new Brick[]{b4, b5, b1, b2, b3};
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
					printError("a" + (i+1) + ".compareTo(a" + (i+1) + ")", 0, val);
					success = false;
				}

				//Test that mj.compareTo(mi) < 0 for i<j
				val = comps[j].compareTo(correctResult[i]);
				if(j != i && val >= 0){
					printError("a" + (j+1) + ".compareTo(a" + (i+1) + ")", "negative", val);
					success = false;
				}

				//Test that mi.compareTo(mj) > 0 for i<j
				val = comps[i].compareTo(correctResult[j]);
				if(j != i && val <= 0){
					printError("a" + (i+1) + ".compareTo(a" + (j+1) + ")", "positive", val);
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