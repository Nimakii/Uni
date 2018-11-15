
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * The test class PositionTest.
 *
 * @author  Jens Kristian Nielsen & Thomas Vinther
 * @version Computerspil 2
 */
public class PositionTest
{
    private Country country1;
    private City cityA, cityB;
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
        
        // Create Position
        pos = new Position(cityA,cityB,3);
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
        //pos = new Position(cityA,cityB,3);
        assertEquals(cityA,pos.getFrom());
        assertEquals(cityB,pos.getTo());
        assertEquals(3,pos.getDistance());
        assertEquals(3,pos.getTotal());
    }

    @Test
    public void hasArrived() {
        assertFalse(pos.hasArrived());
        pos.move(); //distance = 2
        assertFalse(pos.hasArrived());
        assertEquals(3,pos.getTotal());
        pos.move(); //distance = 1
        assertFalse(pos.hasArrived());
        pos.move(); //distance = 0
        assertTrue(pos.hasArrived());
        assertEquals(3,pos.getTotal());
        Position pos2 = new Position(cityA,cityA,0);
        assertTrue(pos2.hasArrived());
        Position pos3 = new Position(cityA,cityB,0);
        assertTrue(pos3.hasArrived());
    }

    @Test
    public void move() {
        assertEquals(3,pos.getDistance());
        assertTrue(pos.move());
        assertEquals(2,pos.getDistance());
        assertTrue(pos.move());
        assertEquals(3,pos.getTotal());
        assertEquals(1,pos.getDistance());
        assertTrue(pos.move());
        assertEquals(0,pos.getDistance());
        assertFalse(pos.move());
        assertFalse(-1 == pos.getDistance());
        assertEquals(0,pos.getDistance());
        assertFalse(pos.move());
        assertEquals(3,pos.getTotal());
        assertEquals(0,pos.getDistance());
        assertFalse(pos.move());
        assertFalse(-1 == pos.getDistance());
    }

    @Test
    public void turnAround() {
        assertEquals(3,pos.getDistance());
        assertTrue(pos.move());
        assertEquals(3,pos.getTotal());
        assertEquals(2,pos.getDistance());
        assertTrue(pos.move());
        assertEquals(1,pos.getDistance());
        pos.turnAround();
        assertEquals(cityA,pos.getTo());
        assertEquals(cityB,pos.getFrom());
        assertEquals(3,pos.getTotal());
        assertEquals(2,pos.getDistance());
        assertTrue(pos.move());
        assertEquals(1,pos.getDistance());
        pos.turnAround();
        assertEquals(cityB,pos.getTo());
        assertEquals(cityA,pos.getFrom());
        assertEquals(2,pos.getDistance());
        assertEquals(3,pos.getTotal());
        pos.move();
        pos.move();
        pos.move();
        assertTrue(pos.hasArrived());
        pos.turnAround();
        assertEquals(pos.getTotal(),pos.getDistance());
        assertFalse(pos.hasArrived());
        assertTrue(pos.move());
        assertEquals(2,pos.getDistance());
    }
}
