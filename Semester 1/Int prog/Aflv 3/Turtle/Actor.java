/**
 * Import Color
 */
import java.awt.Color;

/**
 * An Actor object is an object which can move around on the screen using Turtle Graphics.
 * The class is abstract so it is not possible to instantiate it directly.
 * 
 * Instead the Turtle class can be instantiated.
 * 
 * @author (Nikolaj Ignatieff Schwartzbach) .
 * @version 2017-01-05
 */
abstract class Actor
{
    //---------------------\\
    //---FIELD VARIABLES---\\
    //---------------------\\

    /** Object reference to the Canvas. */
    private Canvas canvas;
    
    /** Position and rotation. */
    private double x, y, theta;
    
    /** Whether the Turtle should paint or not. */
    private boolean penDown;
    
    /** The current color of the pen of this Turtle object. */
    private Color color;
    
    /** The current index of the color for this Turtle object. */
    private int colorInteger;
    
    //----------------------\\
    //---  CONSTRUCTORS  ---\\
    //----------------------\\
    
    /**
     * Creates a new Actor object in a Canvas, at a position (x,y) with angle theta (radians).
     * @param   w       The Canvas object to add this Actor to.
     * @param   x       The x-coordinate of this Actor.
     * @param   y       The y-coordinate of this Actor.
     * @param   theta   The angle (in radians) of this Actor.
     */
    public Actor(int x, int y, double theta) {
       this.canvas       = Canvas.getCanvas();
       this.x            = x;
       this.y            = y;
       this.theta        = theta;
       this.penDown      = true;
       this.color        = Color.BLACK;
       this.colorInteger = 0;       
    }
    
    
    //---------------------\\
    //---    GETTERS    ---\\
    //---------------------\\
    
    /**
     * Checks whether or not this Actor is currently drawing.
     * @return  A boolean representing whether or not this Actor is currently drawing.
     */
    public boolean isPenDown() {
        return penDown;
    }
    
    /**
     * Returns the x-coordinate (in pixels) of this Actor. Will not necessarily be an integer-value.
     * @return  The x-coordinate (in pixels) of this Actor.
     */
    public double getX() {
        return x;
    }
    
    /**
     * Returns the y-coordinate (in pixels) of this Actor. Will not necessarily be an integer-value.
     * @return  The x-coordinate (in pixels) of this Actor.
     */
    public double getY() {
        return y;
    }
    
    /**
     * Returns the angle that this Actor is facing (in degrees). Will always be in the interval [0, 360).
     * @return  The angle in degrees of this Actor.
     */
    public double getAngle() {
        return theta * 180/Math.PI;
    }
        
    /**
     * Returns the current color of this Actor (as an integer to be used in setColor()).
     * @return  An integer representing the current color of this Actor.
     */
    public int getColor() {
        return colorInteger;
    }    
    
    /**
     * Returns the current Color object of this Actor.
     * @return  A Color object representing the color that this Actor draws with.
     */
    public Color getColorInstance() {
        return color;
    }
    
    /**
     * Returns the Canvas.
     * @return  A Canvas object representing the canvas in which this Actor resides.
     */
    public Canvas getCanvas() {
        return canvas;
    }
    
    //---------------------\\
    //---    SETTERS    ---\\
    //---------------------\\
    
    /**
     * Adds this Actor to the Canvas.
     */
    public void addToCanvas() {
       canvas.addActor(this);
       canvas.update();
    }
    
    /**
     * Instructs this Actor to stop drawing.
     */
    public void penUp() {
        penDown = false;
        canvas.update();
    }
    
    /**
     * Instructs this Actor to start drawing.
     */
    public void penDown() {
        penDown = true;
        canvas.update();
    }
    
    /**
     * Instructs this Actor to rotate a specified number of degrees (clockwise).
     * @param   degrees     The number of degrees this Actor rotates.
     */
    public void turn(double degrees) {
        turnTo(getAngle() + degrees);
    }
    
    /**
     * Sets the rotation of this Actor to a specified number of degrees.
     * @param   degrees     The new rotation (in degrees) of this Actor.
     */
    public void turnTo(double degrees) {
        theta = (degrees * Math.PI/180.0) % (2*Math.PI);
        while(theta < 0) {
            theta += 2*Math.PI;
        }
        canvas.update();
    }
    
    /**
     * Moves this Actor to a specified position. Will draw if penDown.
     * @param   newX    The new x-coordinate (in pixels) of this Actor.
     * @param   newY    The new y-coordinate (in pixels) of this Actor.
     */
    public void moveTo(double newX, double newY) {
        if(penDown) {
            canvas.drawLine(x, y, newX, newY, color);
        }
            
        this.x = newX;
        this.y = newY;
        
        canvas.update();
    }
    
    /**
     * Moves this Actor a specified number of units in the direction it is facing. Will draw if distance > 0.
     * @param   distance    The distance for this Actor to travel (in pixels).
     */
    public void move(double distance) {
        double dx = distance * Math.cos(theta);
        double dy = distance * Math.sin(theta);
        moveTo(x + dx, y + dy);
    }
    
    /**
     * Sets the color to the specified color.
     * Allowed values are:
     *  - "black"
     *  - "red"
     *  - "blue"
     *  - "yellow"
     *  - "green"
     *  - "magenta"
     *  - any hex color, that is "#RRGGBB"
     *  
     *  Will change the color to Black if an invalid String is chosen.
     *  
     *  @param  col     A String representing the new color of this Actor.
     */
    public void setColor(String col) {
        switch(col) {
            case "black":
                color = Color.BLACK;
                colorInteger = 0;
                break;
            case "red":
                color = Color.decode("#AD2A1A");
                colorInteger = 1;
                break;
            case "blue":
                color = Color.decode("#107896");
                colorInteger = 2;
                break;
            case "yellow":
                color = Color.decode("#BCA136");
                colorInteger = 3;
                break;
            case "green":
                color = Color.decode("#568203");
                colorInteger = 4;
                break;
            case "magenta":
                color = Color.MAGENTA;
                colorInteger = 5;
                break;
            default:
                color = Color.BLACK;
                colorInteger = 0;
                break;
        }
        if(col.startsWith("#")){
            color = Color.decode(col);
            colorInteger = -1;
        }
        canvas.update();
    }
    
    /**
     * Sets the color to c, 0 <= c < 6
     * 0: black,  1: red,  2: blue,  3: yellow,  4: green,  5: magenta
     * 
     * Will set the color to Black if an integer outside the interval [0, 5] is chosen.
     * 
     * @param   i   The new color of this Actor.
     */
    public void setColor(int i) {
        switch(i) {
            case 0:
                setColor("black");
                break;
            case 1:
                setColor("red");
                break;
            case 2:
                setColor("blue");
                break;
            case 3:
                setColor("yellow");
                break;
            case 4:
                setColor("green");
                break;
            case 5:
                setColor("magenta");
                break;
            default:
                setColor("black");
                break;
        }
    }
    
}
