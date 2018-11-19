import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * The test class RoadTest.
 *
 * @author  Jens Kristian & Thomas Vinther
 * @version Computergame 2
 */
public class RoadTest
{
    private Game game;
    private Country country1, country2;
    private City cityA, cityB, cityC;

    /**
     * Default constructor for test class RoadTest
     */
    public RoadTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {

        // Create countries
        country1 = new Country("Country 1", null);
        country2 = new Country("Country 2", null);
        country1.setGame(game);
        country2.setGame(game);

        // Create Cities
        cityA = new City("City A", 80, country1);
        cityB = new City("City B", 60, country1);
        cityC = new City("City C", 40, country1);
        

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

    /**
     * Tests the Road constructor
     */
    @Test
    public void constructor(){
        Road road = new Road(cityA, cityB, 4);
        assertEquals(road.getFrom(), cityA);
        assertEquals(road.getTo(), cityB);
        
        /**Negative test*/
        Road roadTwo = new Road(cityA, cityB,100000000);
        assertFalse(roadTwo.getFrom().equals(cityB));
        assertFalse(roadTwo.getTo().equals(cityA));
        assertTrue(roadTwo.getLength()==100000000);

    }

    /**
     * Test the Roads compareTo method.
     */
    @Test
    public void compareTo() {
        Road road1 = new Road(cityA, cityB, 0);
        Road road2 = new Road(cityA, cityC, 0);
        Road road3 = new Road(cityB, cityA, 0);
        Road road4 = new Road(cityA, cityB, 3);
        
        /**Test reflexitivity */
        assertEquals(0,road1.compareTo(road1));
        
        /**Test transitivity of < */
        assertTrue(road1.compareTo(road2) < 0);
        assertTrue(road2.compareTo(road3) < 0);
        assertTrue(road1.compareTo(road3) < 0);
        
        /**Test transitivity of >*/
        assertTrue(road2.compareTo(road1) > 0);
        assertTrue(road3.compareTo(road2) > 0);
        assertTrue(road3.compareTo(road1) > 0);
        
        /** Test of antisymmetry*/
        assertTrue(road1.compareTo(road4)>=0);
        assertTrue(road1.compareTo(road4)<=0);
        assertEquals(0,road1.compareTo(road4));
        
        /**Test ofsymmetry */
        assertEquals(0,road1.compareTo(road4));
        assertEquals(0,road4.compareTo(road1)); 
        assertTrue(road1.compareTo(road4) == road4.compareTo(road1));
    }
    
    
}
