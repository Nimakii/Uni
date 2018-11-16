
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * The test class PositionTest.
 *
 * @author  Jens Kristian & Thomas Vinther
 * @version Computergame 2
 */
public class PositionTest
{
    private Game game;
    private Country country1, country2;
    private City cityA, cityB, cityC, cityD, cityE, cityF, cityG;
    private Position pos;
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        Map<City, List<Road>> network1 = new HashMap<>();
        // Create countries
        country1 = new Country("Country 1", network1);
        // Create Cities
        cityA = new City("City A", 80, country1);
        cityB = new City("City B", 60, country1);
        //Create a new position
        pos = new Position(cityA, cityB, 3);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void constructor() {
        assertEquals(cityB,pos.getTo());
        assertEquals( cityA,pos.getFrom());
        assertEquals(pos.getDistance(),3);
        assertEquals(pos.getTotal(),3);

    }

    @Test
    public void hasArrived() {
        assertFalse(pos.hasArrived());
        pos.move(); //distance 2
        assertFalse(pos.hasArrived());
        pos.move();//distance 1
        assertFalse(pos.hasArrived());
        pos.move();//Distance 0
        assertTrue(pos.hasArrived()); 
        Position pos2 = new Position(cityA,cityA,0);
        assertTrue(pos2.hasArrived());
        Position pos3 = new Position(cityB,cityA,0);
        assertTrue(pos3.hasArrived());
    }

    @Test
    public void move() {
        assertEquals(3,pos.getDistance());
        assertTrue(pos.move());//Distance 2
        assertEquals(2,pos.getDistance());
        assertTrue(pos.move()); //Distance 1
        assertEquals(1,pos.getDistance());
        assertTrue(pos.move()); //Distance 0
        assertEquals(0,pos.getDistance());
        assertFalse(pos.move());
        assertFalse(-1==pos.getDistance());
        assertEquals(0,pos.getDistance());

    }

    @Test
    public void turnAround() {
        pos.move();                         //Distance 2
        pos.turnAround();                   //from b, to a, distance 1
        assertEquals(cityB,pos.getFrom());  
        assertEquals(cityA,pos.getTo());    
        assertEquals(1,pos.getDistance());  
        pos.turnAround();                   
        assertEquals(cityA,pos.getFrom());  
        assertEquals(cityB,pos.getTo());
        assertEquals(2,pos.getDistance());
        pos.move();                         
        pos.turnAround();                   
        assertEquals(cityB, pos.getFrom()); 
        assertEquals(cityA,pos.getTo());
        assertEquals(2,pos.getDistance());
        pos.turnAround();                   
        pos.move();                         
        assertEquals(cityA,pos.getFrom());  
        assertEquals(cityB,pos.getTo());
        assertEquals(0,pos.getDistance());
        assertTrue(pos.hasArrived());       
        pos.turnAround();                   //From b, to a
        assertEquals(cityA,pos.getTo());    
        assertEquals(cityB,pos.getFrom());
        assertEquals(pos.getTotal(),pos.getDistance());
    }

  
}

