// Import of Java classes to handle input/output, images, and graphical user interface
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.image.WritableRaster;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.WindowEvent;

// Import of ArrayList
import java.util.ArrayList;

/**
 * Model of an image  
 * 
 * @author Nikolaj Schwartzbach
 * @version 2017-08-04
 */
public class Image {    

    ////////// FIELDS (feltvariabler) ////////////

    private int width, height;       // width and height of image
    private String title;            // title of image
    private Pixel[][] pixels;        // array for storing the Pixels of the image 

    // Other fields (used for Graphical User Interface)       
    private java.awt.Image img;
    private JFrame window;
    private Canvas canvas;

    /////////////  COMSTRUCTORS  ////////////////

    /**
     * This constructor creates a new Image object based on an existing file. 
     * The file format must be JPG, PNG, BMP or GIF.
     * The title of the image is taken from the filename.
     * 
     * @param filename  File to be loaded. 
     * @param visible   Whether or not to display the Image using Java AWT.
     */
    public Image(String filename, boolean visible) {
        try {
            BufferedImage input = ImageIO.read(new File(filename));
            width = input.getWidth();
            height = input.getHeight();
            title = filename;
            pixels = new Pixel[width][height];
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    Color rgb = new Color(input.getRGB(i,j));
                    int value = (int)Math.round(0.2126*rgb.getRed() + 0.7152*rgb.getGreen() + 0.0722*rgb.getBlue()); // sRGB luminance
                    pixels[i][j] = new Pixel(value);
                }
            }
        }
        catch(Exception e) {
            width = 100;
            height = 100;
            pixels = new Pixel[width][height];
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    pixels[i][j] = new Pixel(0);
                }
            }
        }
        if(visible)
            showImage();
    }

    public Image(String file){
        this(file, true);
    }

    /**
     * This onstructor creates a new black image.
     * 
     * @param width      Width of image.
     * @param height     Height of image.
     * @param title      Title of image.
     * @param visible    Whether or not to display the Image using Java AWT.
     */
    public Image(int width, int height, String title, boolean visible) {
        this.width = width;
        this.height = height;
        this.title = title;
        pixels = new Pixel[width][height];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                pixels[i][j] = new Pixel(0);
            }
        }

        if(visible)
            showImage();
    }

    /**
     * This onstructor creates a new black image.
     * 
     * @param width      Width of image.
     * @param height     Height of image.
     * @param title      Title of image.
     */
    public Image(int width, int height, String title) {
        this(width,height,title,FiltersTest.SIDE_BY_SIDE);
    }

    /**
     * This constructor creates a copy of another Image object.
     * Copies all pixels as well.
     * 
     * @param image    Image object to copy.
     * @param visible  Whether or not to display the Image using Java AWT.
     */
    public Image(Image image){
        this(image.getWidth(), image.getHeight(), image.getTitle());
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                pixels[i][j] = new Pixel(image.getPixel(i,j).getValue());
            }
        }
    }

    protected void showImage(){
        if(window!=null)return;
        window = new JFrame(title);
        canvas = new Canvas();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(canvas);
        window.pack();
        window.setVisible(true);   
        window.setAlwaysOnTop(true);
        window.setResizable(false);
        new Thread(()->{try{Thread.sleep(5);window.setAlwaysOnTop(false);}catch(InterruptedException e){}}).start();
    }

    public void moveFrame(int dx, int dy){
        if(window==null)return;
        Point p = window.getLocationOnScreen();
        window.setLocation((int)(p.getX()+dx), (int)(p.getY()+dy));
    }

    ///////////SIMPLE GET AND SET METHODS ///////////////////

    /**
     * Returns the width of the image.
     * 
     * @return    Width of image.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the image.
     * 
     * @return    Height of image.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the title of the image.
     * 
     * @return    Title of image.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the image.
     * 
     * @param title    New title.
     */
    public void setTitle(String title){
        if(window != null)
            window.setTitle(title);

        this.title = title;
    }

    ///////////// MORE COMPLEX MERTHODS ////////////////////

    /**
     * Returns the pixel in position (i,j).
     * If the position is outside the image, an exception is thrown. 
     * 
     * @param i    Horizontal index.
     * @param j    Vertical index.
     */
    public Pixel getPixel(int i, int j) {
        return pixels[i][j];
    }

    /**
     * Returns an arraylist with all the pixels in the image.
     * 
     * @return    ArrayList with all pixels in image.
     */
    public ArrayList<Pixel> getPixels() {
        ArrayList<Pixel> all = new ArrayList<Pixel>();
        for(int i = 0; i < width; i++) { 
            for(int j = 0; j < height; j++) {
                all.add(pixels[i][j]);
            }
        }
        return all;
    }  

    /**
     * Returns an arraylist with the (up to nine) direct neighbours of the pixel
     * in position (i,j).
     * Pixel (i,j) is also included.
     * 
     * @param i    Horizontal index.
     * @param j    Vertical index.
     */
    public ArrayList<Pixel> getNeighbours(int i, int j) {
        ArrayList<Pixel> result = new ArrayList<Pixel>();
        for(int x = i - 1; x <= i + 1; x ++) {
            for(int y  = j - 1; y <= j + 1; y++) {
                addPixelToList(x, y, result);
            }
        }
        return result;
    }

    /**
     * Informs the image that one or more of its pixels has changed 
     * value, and that it is time to repaint the canvas.
     */
    public void updateCanvas() {
        if(window==null)return;
        Runnable r = new Runnable() {
                @Override
                public void run() {
                    canvas.rebuildPicture();
                    canvas.repaint();
                }
            };
        SwingUtilities.invokeLater(r);
    }

    //////////////// SPECIAL METHODS ////////////////

    /**
     * Adds pixel (i,j) to list, if the pixel is inside the canvas
     * 
     * @param i    Horizontal index.
     * @param j    Vertical index.
     * @param list   Arraylist to be updated.       
     */
    private void addPixelToList(int i, int j, ArrayList<Pixel> list) {
        if(i >= 0 && j >= 0 && i < width && j < height) {
            list.add(pixels[i][j]);
        }
    }
    
    /**
     * Closes this Image, if showing
     */
    protected void close(){
        if(window != null){
            window.setVisible(false);
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        }
    }

    ///////// INNER PRIVATE CLASS TO HANDLE THE CANVAS IN THE GRAPHICAL USER INTERFACE ////////

    private class Canvas extends JPanel {
        private Canvas() {
            setPreferredSize(new Dimension(width, height));
        }

        private void rebuildPicture() {
            int pix[] = new int[width*height];
            int tmp = 0;
            for(int j = 0; j < height; j++) {
                for(int i = 0; i < width; i++) {
                    int val = pixels[i][j].getValue();
                    // alpha, red, green, blue
                    pix[tmp++] = (255 << 24) | (val << 16) | (val << 8) | val;     
                }
            }
            img = createImage(new MemoryImageSource(width, height, pix, 0, width));
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);         
            if (img == null) rebuildPicture();
            g.drawImage(img, 0, 0, this);
        }
    }

}
