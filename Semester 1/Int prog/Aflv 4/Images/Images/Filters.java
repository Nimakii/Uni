import java.util.*;

/**
 * This class implements a number of filters, i.e. methods that can be used to
 * manipulate Image objects, e.g. make the image darker or mirrored.
 * The filter methods operates on the image in the field (feltvariabel) image.
 * The filter methods change the original image and return the new image.
 *
 * @author Kurt Jensen
 * @version 2017-08-04
 **/
public class Filters
{

    private Image image;     // Image on which the filter methods operate
    
    /**
     * The constructor takes as input an instance of Image.
     * 
     * @param image   Image to apply filters to.
     */
    public Filters(Image image) {
        this.image = image;
    }
    
    /**
     * The constructor  creates an Image object from the file picture.jpg (in the project folder).
     * 
     * @param image   Image to apply filters to.
     */
    public Filters() {
        this.image = new Image("dog.png", true);
    }
    
    /**
     * This method brightens an image by adding some amount to the
     * value of all pixels in the image.
     * The title of the new image is prefixed 'brightenX-',
     * where X is the parametervalue.
     *
     * @param amount   Increase in value for each pixel.
     * @return   Brightened image.
     */
    public Image brighten(int amount) {
        String newName = "brighten"+amount+"-";
        for (Pixel p : image.getPixels()){
            p.setValue(p.getValue()+amount);
        }
        image.setTitle(newName+image.getTitle());
        image.updateCanvas();
        return image;
    }

    /**
     * This method darkens an image by subtracting some amount from the
     * value of all pixels in the image.
     * The title of the new image is prefixed 'darkenX-',
     * where X is the parametervalue.
     *
     * @param amount   Decrease in value for each pixel.
     * @return   Darkened image.
     */
    public Image darken(int amount) {
        String newName = "darken"+amount+"-";
        for (Pixel p : image.getPixels()){
            p.setValue(p.getValue()-amount);
        }
        image.setTitle(newName+image.getTitle());
        image.updateCanvas();
        return image;
    }

    /**
     * This method inverts an image by mapping each pixel value 'v' to '255-v'
     * such that white turns black and vice-versa.
     * The title of the new image is prefixed 'invert-'.
     *
     * @return   Inverted image.
     */
    public Image invert() {
        String newName = "invert-";
        for (Pixel p : image.getPixels()){
                p.setValue(255-p.getValue());
        }
        image.setTitle(newName+image.getTitle());
        image.updateCanvas();
        return image;
    }

    /**
     * This method mirrors an image across the vertical axis.
     * The value of pixel (i,j) in the new image is set to the value of pixel
     * (width-i-1, j) in the old image, where width is the width of the image.
     * The title of the new image is prefixed 'mirror-'.
     *
     * @return   Mirrored image.
     */
    public Image mirror() {
        String newName = "mirror-";
        int w = image.getWidth();
        int h = image.getHeight();
        Image mirrorImage = new Image(w,h,newName+image.getTitle());
        for (int x = 0; x<w;x++){
            for (int y = 0;y<h;y++){
                mirrorImage.getPixel(w-x-1,y).setValue(image.getPixel(x,y).getValue());
            }
        }
        mirrorImage.updateCanvas();
        return mirrorImage;
    }

    /**
     * This method flips an image across the horizontal axis.
     * The value of pixel (i,j) in the new image is set to the value of pixel
     * (i,height-j-1) in the old image, where height is the height of the image.
     * The title of the new image is prefixed 'flip-'.
     *
     * @return   Flipped image.
     */
    public Image flip() {
        String newName = "flip-";
        int w = image.getWidth();
        int h = image.getHeight();
        Image flipImage = new Image(w,h,newName+image.getTitle());
        for (int x = 0; x<w;x++){
            for (int y = 0;y<h;y++){
                flipImage.getPixel(x,h-y-1).setValue(image.getPixel(x,y).getValue());
            }
        }
        flipImage.updateCanvas();
        return flipImage;
    }

    /**
     * This method rotates an image 90 degrees clockwise.
     * The value of pixel (i,j) in the new image is set to the value of pixel
     * (j,width-i-1) in the old image, where width is the width of the new image.
     * The title of the new image is prefixed 'rotate-'.
     *
     * @return   Rotated image.
     */
    public Image rotate() {
        String newName = "rotate-";
        int w = image.getWidth();
        int h = image.getHeight();
        Image rotatedImage = new Image(h,w,newName+image.getTitle());
        for (int x = 0; x<w;x++){
            for (int y = 0;y<h;y++){
                rotatedImage.getPixel(y,x).setValue(image.getPixel(x,h-y-1).getValue());
            }
        }
        rotatedImage.updateCanvas();
        return rotatedImage;
    }

    /**
     * Auxillary method for blur.
     * This method computes the average value of the (up to nine) neighbouring pixels
     * of position (i,j) -- including pixel (i,j).
     *
     * @param i   Horizontal index.
     * @param j   Vertical index.
     * @return    Average pixel value.
     */
    private int average(int i, int j) {
        int avg = 0;
        int count = 0;
        for (Pixel p : image.getNeighbours(i,j)){
            avg += p.getValue();
            count++;
        }
        return avg/count;
    }

    /**
     * This method blurs an image.
     * Each pixel (x,y) is mapped to the average value of the neighbouring pixels. 
     * The title of the new image is prefixed 'blur-'.
     *
     * @return   Blurred image.
     */
    public Image blur() {
        String newName = "blur-";
        int w = image.getWidth();
        int h = image.getHeight();
        Image blurredImage = new Image(w,h,newName+image.getTitle());
        for (int x = 0; x<w;x++){
            for (int y = 0;y<h;y++){
                blurredImage.getPixel(x,y).setValue(average(x,y));
            }
        }
        blurredImage.updateCanvas();
        return blurredImage;
    }
   
    /**
     * This method adds noise to an image.
     * The value of pixel (i,j) is set to a random value in the interval
     * [v-amount, v+amount], where v is the old value and amount the parameter.
     * The title of the new image is prefixed 'noiseX-'.
     *
     * @param amount   Maximal amount of noise to add.
     * @return  Noisy image. 
     */
    public Image noise(int amount) {
        String newName = "noise"+amount+"-";
        Random rando = new Random();
        int w = image.getWidth();
        int h = image.getHeight();
        Image noiseImage = new Image(w,h,newName+image.getTitle());
        for (int x = 0; x<w;x++){
            for (int y = 0;y<h;y++){
                int noise = (rando.nextInt(2*amount+1)-amount); //rando.nextInt(2*amount+1) returns something from [0,2*amount], now -amount makes it [-amount,amount]
                noiseImage.getPixel(x,y).setValue(image.getPixel(x,y).getValue()+noise);
            }
        }
        noiseImage.updateCanvas();
        return noiseImage;
    }

    /**
     * This method resizes an image by some factor.
     * The size of the new image becomes with*factor x hiehgt*factor, where
     * width and heigt are the width and height of the old image.
     * The value of pixel (i,j) in the new image is set to the value of pixel
     * (i/factor,j/factor) in the old image, where factor is the parameter.
     * This produces a new image of size (width*factor, height*factor).
     * The title of the new image is prefixed 'factorX-'.
     *
     * @param factor   Resize factor.
     * @return   Resized image.
     */
    public Image resize(double factor) {
        String newName = "resize"+factor+"-";
        int w = (int) (image.getWidth()*factor);
        int h = (int) (image.getHeight()*factor);
        Image resizedImage = new Image(w,h,newName+image.getTitle());
        for (int x = 0; x<w;x++){
            for (int y = 0;y<h;y++){
                resizedImage.getPixel(x,y).setValue(image.getPixel((int) (x/factor)
                ,(int) (y/factor)).getValue());
            }
        }
        resizedImage.updateCanvas();
        return resizedImage;
    }
    
    /**
     * This method rotates an image 90 degrees anti-clockwise.
     * The value of pixel (i,j) in the new image is set to the value of pixel
     *  in the old image, where width is the width of the new image.
     * The title of the new image is prefixed 'rotateAC-'.
     * 
     * Nah fam, just rotate 3 times
     *
     * @return   Rotated image.
     */
    public Image rotateAC() {
        String newName = "rotateAC-";
        int w = image.getWidth();
        int h = image.getHeight();
        Image rotated1Image = new Image(h,w,newName+image.getTitle());
        for (int x = 0; x<w;x++){ //rotate
            for (int y = 0;y<h;y++){
                rotated1Image.getPixel(y,x).setValue(image.getPixel(x,h-y-1).getValue());
            }
        }
        w = rotated1Image.getWidth();
        h = rotated1Image.getHeight();
        Image rotated2Image = new Image(h,w,newName+image.getTitle());
        for (int x = 0; x<w;x++){ //rotate
            for (int y = 0;y<h;y++){
                rotated2Image.getPixel(y,x).setValue(rotated1Image.getPixel(x,h-y-1).getValue());
            }
        }
        w = rotated2Image.getWidth();
        h = rotated2Image.getHeight();
        Image rotatedACImage = new Image(h,w,newName+image.getTitle());
        for (int x = 0; x<w;x++){ //rotate
            for (int y = 0;y<h;y++){
                rotatedACImage.getPixel(y,x).setValue(rotated2Image.getPixel(x,h-y-1).getValue());
            }
        }
        rotatedACImage.updateCanvas();
        return rotatedACImage;
    }
    
    /**
     * This image increases the contrast of an image by some amount.
     * 
     * @param amount    The amount by which to increase contrast
     */
    public Image increaseContrast(double amount) {
        double p = Math.exp(-amount);                     //p
        String newName = "contrast"+amount+"-";
        int w = image.getWidth();
        int h = image.getHeight();
        Image contrastedImage = new Image(w,h,newName+image.getTitle());
        for (int x = 0; x<w;x++){
            for (int y = 0;y<h;y++){
                double input = image.getPixel(x,y).getValue();          //input
                double calc = ((2*input)/255)-1;                        //x
                double sign = Math.signum(calc);                        //signum(x)
                double absCalc = 0;                                     //y=|x|
                if(sign>0)  {absCalc =   calc;}                         //y=x hvis x>0
                else        {absCalc = - calc;}                         //y=-x hvis x<0
                double calc3 = Math.pow(absCalc,p);                     //y'=y^p
                double calc4 = sign*calc3;                              //x'=signum(x)y'
                int finalCalc = (int) (((calc4+1)/2)*255);              //input'
                contrastedImage.getPixel(x,y).setValue(finalCalc);
            }
        }
        contrastedImage.updateCanvas();
        return contrastedImage;
    }

}
