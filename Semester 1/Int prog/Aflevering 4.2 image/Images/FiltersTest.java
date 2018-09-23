import java.nio.file.*;
import java.io.*;
import javax.imageio.*;
import java.util.function.*;
import java.util.*;
/**
 * This class contains methods for automatic testing of Images.
 * 
 * @author Nikolaj Ignatieff Schwartzbach
 * @version 2017-08-04
 **/
public class FiltersTest
{
    //Static variable to determine whether or not to display the expected/received images side-by-side for easy visual comparison.
    static boolean SIDE_BY_SIDE = true,
                   CLOSE_EXISTING = true;
    
    //The absolute change in brightness to use for testing brighten() and darken()
    protected static final int BRIGHTEN_AMOUNT = 50,
                               SIDE_BY_SIDE_OFFSET = 30;
    
    //The factor by which to resize images to use for testing resize()
    private static final double RESIZE_AMOUNT = 0.75,
                                NOISE_MARGIN = 0.1,
                                CONTRAST_AMOUNT = 1.5;
    
                                
    //Nice formatting to System.out
    protected static final String del = TestUtil.repeat("-",20)+"\n",
                                  imagePath = "picture.png";
    
    //Human-readable identifiers for each method being tested
    private static final String[] identifiers = new String[]{"brighten","darken","invert","mirror","flip","rotate","blur","noise","resize","rotateAC","increaseContrast"};
    @SuppressWarnings("unchecked")
    private static final Runnable[] methods = new Runnable[]{FiltersTest::testBrighten, FiltersTest::testDarken, FiltersTest::testInvert, FiltersTest::testMirror,
                                                             FiltersTest::testFlip, FiltersTest::testRotate, FiltersTest::testBlur, FiltersTest::testNoise,
                                                             FiltersTest::testResize, FiltersTest::testRotateAC, FiltersTest::testIncreaseContrast };
    
    private FiltersTest(){}
                                                       
    /**
     * Tests all 10 methods against reference images.
     * Will output for each method whether or not the test was successful.
     * 
     * If a test fails, only the first five discrepancies will be printed to the console (for each method).
     * 
     * Will not render side-by-side comparisons.
     * 
     * @return True, if all tests succeed, and false otherwise.
     */
    public static void testAll() {
        try{
            System.out.print(del+"PERFORMING ALL TESTS\n"+del);
        
            if(!TestUtil.pictureExists()){
                System.out.println("The file '"+imagePath+"' does not exist in the current folder. Aborting test...");
                return;
            }
            boolean[] success = new boolean[methods.length];
        
            SIDE_BY_SIDE = false;
            Arrays.stream(methods).forEach(m->m.run());
            
        } finally {
            SIDE_BY_SIDE = true;
        }
    }
    /**
     * Tests the implementation of brighten() against a reference image.
     * Will provide side-by-side view of expected/received images for easy visual comparison.
     * If the test fails, only the first five discrepancies will be printed to the console.
     * 
     * @return True, if the test succeeds, and false otherwise.
     */
    public static void testBrighten() {
        TestUtil.testMethod("brighten", "brighten"+BRIGHTEN_AMOUNT, f->f.brighten(BRIGHTEN_AMOUNT));
    }
    /**
     * Tests the implementation of darken() against a reference image.
     * Will provide side-by-side view of expected/received images for easy visual comparison.
     * If the test fails, only the first five discrepancies will be printed to the console.
     * 
     * @return True, if the test succeeds, and false otherwise.
     */
    public static void testDarken() {
        TestUtil.testMethod("darken", "darken"+BRIGHTEN_AMOUNT, f->f.darken(BRIGHTEN_AMOUNT));
    }
    /**
     * Tests the implementation of invert() against a reference image.
     * Will provide side-by-side view of expected/received images for easy visual comparison.
     * If the test fails, only the first five discrepancies will be printed to the console.
     * 
     * @return True, if the test succeeds, and false otherwise.
     */
    public static void testInvert() {
        TestUtil.testMethod("invert", Filters::invert);
    }
    /**
     * Tests the implementation of mirror() against a reference image.
     * Will provide side-by-side view of expected/received images for easy visual comparison.
     * If the test fails, only the first five discrepancies will be printed to the console.
     * 
     * @return True, if the test succeeds, and false otherwise.
     */
    public static void testMirror() {
        TestUtil.testMethod("mirror", Filters::mirror);
    }
    /**
     * Tests the implementation of flip() against a reference image.
     * Will provide side-by-side view of expected/received images for easy visual comparison.
     * If the test fails, only the first five discrepancies will be printed to the console.
     * 
     * @return True, if the test succeeds, and false otherwise.
     */
    public static void testFlip() {
        TestUtil.testMethod("flip", Filters::flip);
    }   
    /**
     * Tests the implementation of rotate() against a reference image.
     * Will provide side-by-side view of expected/received images for easy visual comparison.
     * If the test fails, only the first five discrepancies will be printed to the console.
     * 
     * @return True, if the test succeeds, and false otherwise.
     */
    public static void testRotate() {
        TestUtil.testMethod("rotate", Filters::rotate);
    }
    /**
     * Tests the implementation of blur() against a reference image.
     * Will provide side-by-side view of expected/received images for easy visual comparison.
     * If the test fails, only the first five discrepancies will be printed to the console.
     * 
     * @return True, if the test succeeds, and false otherwise.
     */
    public static void testBlur() {
        TestUtil.testMethod("blur", Filters::blur);
    }
    /**
     * Tests the implementation of noise() against a reference image.
     * Will provide side-by-side view of expected/received images for easy visual comparison.
     * If the test fails, only the first five discrepancies will be printed to the console.
     * 
     * Note: this test is probabilistic in nature.
     * Note: this test does NOT provide side-by-side comparison.
     * 
     * @return True, if the test succeeds, and false otherwise.
     */
    public static void testNoise(){
        if(SIDE_BY_SIDE){
            System.out.println("[NOTE]: testNoise() does not use a reference image (since noise is non-deterministic).\n"+
                               "        Instead different statistical tests are performed.\n");
        }
        
        TestUtil.disposeOld();
        System.out.print("Testing noise...");
        Image img = TestUtil.ref = new Image(imagePath,SIDE_BY_SIDE);
        Filters f = new Filters(TestUtil.NewImage(img,SIDE_BY_SIDE));
        Image noise = TestUtil.test = f.noise(5);
        
        if(!TestUtil.testNull("noise",noise))return;
        
        if(!TestUtil.testTitle("noise",img,noise,"noise5")){
            img.moveFrame(noise.getWidth()+SIDE_BY_SIDE_OFFSET,0);
            img.setTitle(img.getTitle()+" (Original)");
            noise.setTitle(noise.getTitle()+" (Your code)");
            return;
        }
        
            
        img.moveFrame(noise.getWidth()+SIDE_BY_SIDE_OFFSET,0);
        img.setTitle(img.getTitle()+" (Original)");
        noise.setTitle(noise.getTitle()+" (Your code)");
        //Aux variables
        int sum = 0;
        Set<Integer> diffs = new HashSet<>();
        Map<Integer, Integer> count = new HashMap<>();
        
        //Go through all pixels
        for(int x=0; x<img.getWidth(); x++){
            for(int y=0; y<img.getHeight(); y++){
                int diff = noise.getPixel(x,y).getValue() - img.getPixel(x,y).getValue();
                diffs.add(diff);
                sum+=diff;
                
                if(!count.containsKey(diff))
                    count.put(diff,1);
                else
                    count.put(diff,1+count.get(diff));
            }
        }
        
        //Constants
        int numPixels = img.getWidth() * img.getHeight();
        double avg = sum / (double)numPixels;
        
        //Test that each difference is used between numPixels/10 and numPixels/12 (expected = numPixels/11)
        for(int i : count.keySet())
            if(!(numPixels/12 <= count.get(i) && count.get(i) <= numPixels/10)){
                TestUtil.failTest("noise","The average number of pixels with a difference of "+i+" to the original pixels is out of range.","["+numPixels/12+", "+numPixels/10+"]",""+count.get(i));
                return;
            }
            
        if(diffs.size() != 11){
            TestUtil.failTest("noise","Not all difference values in range have been used. ",11,diffs.size());
            return;
        }
        
        if(!(avg >= -NOISE_MARGIN && NOISE_MARGIN >= avg)){
            TestUtil.failTest("noise","The average difference is out of range.","["+(-NOISE_MARGIN)+", "+NOISE_MARGIN+"]",""+avg);
            return;
        }
        
        System.out.println(" OK");
    }
    /**
     * Tests the implementation of resize() against a reference image.
     * Will provide side-by-side view of expected/received images for easy visual comparison.
     * If the test fails, only the first five discrepancies will be printed to the console.
     * 
     * @return True, if the test succeeds, and false otherwise.
     */
    public static void testResize() {
        TestUtil.testMethod("resize", "resize"+RESIZE_AMOUNT, f->f.resize(RESIZE_AMOUNT));
    }
    /**
     * Tests the implementation of rotateAC() against a reference image.
     * Will provide side-by-side view of expected/received images for easy visual comparison.
     * If the test fails, only the first five discrepancies will be printed to the console.
     * 
     * @return True, if the test succeeds, and false otherwise.
     */
    public static void testRotateAC() {
        TestUtil.testMethod("rotateAC", Filters::rotateAC);
    }
    
    /**
     * Tests the implementation of blur() against a reference image.
     * Will provide side-by-side view of expected/received images for easy visual comparison.
     * If the test fails, only the first five discrepancies will be printed to the console.
     * 
     * @return True, if the test succeeds, and false otherwise.
     */
    public static void testIncreaseContrast() {
        TestUtil.testMethod("increaseContrast","contrast"+CONTRAST_AMOUNT, f->f.increaseContrast(CONTRAST_AMOUNT));
    }
}
/**
 * This inner class contains auxiliary methods for automatic testing.
 * 
 * @author Nikolaj Ignatieff Schwartzbach
 * @version 2017-08-04
 */
class TestUtil{
    //Maximum number of errors to display per method.
    private static int MAX_ERRORS_PER_METHOD = 5;
    
    static Image ref = null, test = null;
    protected static void disposeOld(){
        if(!FiltersTest.CLOSE_EXISTING)return;
        if(ref != null) ref.close();
        if(test != null) test.close();
    }
    
    /**
     * Clones an image without showing the clone.
     * @param image The image to clone
     */
    protected static Image NewImage(Image image, boolean visible){
        if(visible) return new Image(image);
        Image clone = new Image(image.getWidth(), image.getHeight(), image.getTitle(), false);
        for(int x=0; x<image.getWidth(); x++) {
            for(int y=0; y<image.getHeight(); y++){
                clone.getPixel(x,y).setValue(image.getPixel(x,y).getValue());
            }
        }
        return clone;
    }
    
    /**
     * Truncates an integer to 8 bits.
     * That is, returns max(min(i, 255), 0)
     * 
     * @param i The integer to truncate.
     * 
     * @return An integer in the interval [0,255].
     */
    protected static int trunc(int i) {
        return i>255 ? 255 : (i<0 ? 0 : i);
    }
    /**
     * Tests a method. Performs the following steps:
     *  - Checks that the image is non-null
     *  - Checks that the title has been correctly changed
     *  - Checks that the dimensions of the image are correct
     *  - Checks that the pixel values are identical to the reference image.
     * 
     * Will print discrepancies to the console.
     * 
     * @param id    The human-readable identifier of the string
     * @param func  Reference to the function being tested.
     * 
     * @return True, if the method passes the test, and false otherwise.
     */
    protected static boolean testMethod(String id, Function<Filters, Image> func){
        return testMethod(id,id,func);
    }
    
    /**
     * Tests a method. Performs the following steps:
     *  - Checks that the image is non-null
     *  - Checks that the title has been correctly changed
     *  - Checks that the dimensions of the image are correct
     *  - Checks that the pixel values are identical to the reference image.
     * 
     * Will print discrepancies to the console.
     * 
     * @param id        The human-readable identifier of the string
     * @param newTitle  The expected new title of the image.
     * @param func      Reference to the function being tested.
     * 
     * @return True, if the method passes the test, and false otherwise.
     */
    protected static boolean testMethod(String id, String newTitle, Function<Filters, Image> func){
        if(!TestUtil.pictureExists()) {
            TestUtil.failTest(id, "Unable to perform test without '"+FiltersTest.imagePath+"'. Aborting...");
            return false;
        }
        System.out.print("Testing "+id+"...");
        
        if(ref!=null)  ref.close();
        if(test!=null) test.close();
        
        Image picture = new Image(FiltersTest.imagePath, false);
        Filters f = new Filters(TestUtil.NewImage(picture, false));
        
        Image out = test = func.apply(f);
        if(FiltersTest.SIDE_BY_SIDE)
            out.showImage();
            
        if(!testNull(id,out) || !testTitle(id,picture,out,newTitle) || !testImageContents(id, out, id+".png")) {
            if(FiltersTest.SIDE_BY_SIDE)
                print("\nThe test for "+id+" failed...",0);
            return false;
        }
        
        System.out.println(" OK");
        return true;
    }
    /**
     * Tests that an image is non-null. 
     * Will print to the console if it is null.
     * 
     * @param id    The human-readable identifier for the method.
     * @param out   The Image to test.
     * 
     * @return True, if the image is non-null, and false otherwise.
     */
    protected static boolean testNull(String id, Image out) {
        if(out == null){
            TestUtil.failTest(id, "The method "+id+" returns null.");
            return false;
        }
        return true;
    }
    /**
     * Tests that an image has the expected title.
     * Will print to the console if the title is wrong.
     * 
     * @param id        The human-readable identifier for the method.
     * @param in        The reference Image.
     * @param out       The Image to test.
     * @param newTitle  The new title of the Image.
     * 
     * @return True, if the image is non-null, and false otherwise.
     */
    protected static boolean testTitle(String id, Image in, Image out, String newTitle) {
        if(!out.getTitle().equals(newTitle+"-"+in.getTitle())){
            TestUtil.failTest(id, "Invalid name for the returned image in "+id, newTitle+"-"+in.getTitle(), out.getTitle());
            return false;
        }
        return true;
    }
    /**
     * Tests whether or not the input image exists in the local file system.
     * 
     * @return True, if the image exists, and false otherwise.
     */
    protected static boolean pictureExists(){
        if(!Files.exists(Paths.get(FiltersTest.imagePath))) {
            return false;
        }
        return true;
    }
    /**
     * Tests that the values of all pixels matches that of the reference image.
     * Starts by checking that the image has the correct dimensions, and if yes, tests all pixels.
     * 
     * Will print to the console if the dimensions are wrong / if discrepancies are found.
     * Only the five first discrepancies are printed to the console.
     * 
     * @param id    The human-readable identifier for the method.
     * @param img   The Image to test.
     * @param path  The path of the reference image.
     * 
     * @return True, if the image has correct dimensions and pixel values, and false otherwise.
     */
    protected static boolean testImageContents(String id, Image img, String path){
        Image ref = TestUtil.ref = new Image("tests/"+path, FiltersTest.SIDE_BY_SIDE);
        ref.setTitle(ref.getTitle() + " (Reference image)");
        img.setTitle(img.getTitle() + " (Your code)");
        ref.moveFrame(img.getWidth()+FiltersTest.SIDE_BY_SIDE_OFFSET, 0);
        if(img.getWidth() != ref.getWidth()) {
            failTest(id,"The width of the new image does not match the width of the reference.", ref.getWidth(), img.getWidth());
            return false;
        }
        if(img.getHeight() != ref.getHeight()) {
            failTest(id,"The height of the new image does not match the width of the reference.", ref.getHeight(), img.getHeight());
            return false;
        }
        boolean success=true;
        int fails = 0;
        for(int x=0; x < img.getWidth(); x++) {
            for(int y=0; y < img.getHeight(); y++) {
                int rec = img.getPixel(x,y).getValue(),
                exp = ref.getPixel(x,y).getValue();
                if(rec != exp) {
                    if(success)
                        print("Test failed for "+id+"...",1);
                    failTest(id,"The new image has an incorrect value at (x: "+x+", y: "+y+").", exp, rec);
                    success = false;
                    if(fails++ >= MAX_ERRORS_PER_METHOD) {
                        print("Only displaying first "+MAX_ERRORS_PER_METHOD+" discrepancies. There may be more incorrect values", 1);
                        return false;}
                }
            }
        }
        return success;
    }
    
    /**
     * This method is invoked when a test is failed.
     * Will print to the console using identation = 1.
     * 
     * @param test  The human-readable identifier of the test.
     * @param msg   The message.
     */
    protected static void failTest(String test, String msg){
        System.out.println();
        print(msg,1);
    }
    /**
     * This method is invoked when a test is failed.
     * Will notify the user of the expected value and the received value.
     * Will print to the console using identation = 1.
     * 
     * @param test  The human-readable identifier of the test.
     * @param msg   The message.
     * @param exp   Expected value.
     * @param rec   Received value.
     */
    protected static void failTest(String id, String msg, String exp, String rec) {
        failTest(id,msg);
        print("Expected: "+exp,2);
        print("Received: "+rec,2);
    }
    /**
     * This method is invoked when a test is failed.
     * Will notify the user of the expected value and the received value.
     * Will print to the console using identation = 1.
     * 
     * @param test  The human-readable identifier of the test.
     * @param msg   The message.
     * @param exp   Expected value.
     * @param rec   Received value.
     */
    protected static void failTest(String id, String msg, int exp, int rec) {
        failTest(id, msg, ""+exp, ""+rec);
    }
    
    /**
     * Prints to System.out using a given indentation level (number of tabs as prefix).
     * 
     * @param s     The string to print
     * @param depth The indentation level (no. of tab characters as prefix).
     */
    protected static void print(String s, int depth){
        System.out.println(repeat("\t",depth)+s);
    }
    
    /**
     * Repeats a string n times and returns the concatenation.
     * 
     * @param s The string to repeat.
     * @param n Number of repetitions.
     */
    protected static String repeat(String s, int n){
        return String.join("", Collections.nCopies(n, s));
    }
    
}
