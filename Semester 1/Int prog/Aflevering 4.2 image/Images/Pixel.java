/**
 * Models a single greyscale pixel in an image
 * 
 * @author Nikolaj Schwartzbach
 * @version 2016.12.08
 */
public class Pixel {

    private int value;    // The value (grey tone) of the pixel

    /**
     * Constructor for Pixel objects.
     * 
     * @param value    Initial value of the pixel.
     */
    public Pixel(int value) {
        this.value = value;
    }

    /**
     * Returns the value in the pixel.
     * 
     * @return   Value of pixel.
     */
    public int getValue()  {
        return this.value;
    }

    /**
     * Sets the value of the pixel to the value specified by the parameter.
     * If the specified value is less than 0, the value is it set to 0.
     * If the specified value is larger than 255, the value is it set to 255.
     * 
     * @param value    New value of  pixel.
     */      
    public void setValue(int value) {
        if(value < 0) {
            this.value = 0;
        } else if(value > 255) {
            this.value = 255;
        } else {
            this.value = value;
        }
    }

}
