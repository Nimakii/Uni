
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * The test class PositionTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
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
        game = new Game(0);
        game.getRandom().setSeed(0);
        Map<City, List<Road>> network1 = new HashMap<>();
        Map<City, List<Road>> network2 = new HashMap<>();

        // Create countries
        country1 = new Country("Country 1", network1);
        country2 = new Country("Country 2", network2);
        country1.setGame(game);
        country2.setGame(game);

        // Create Cities
        cityA = new City("City A", 80, country1);
        cityB = new City("City B", 60, country1);
        cityC = new City("City C", 40, country1);
        cityD = new City("City D", 100, country1);
        cityE = new City("City E", 50, country2);
        cityF = new City("City F", 90, country2);
        cityG = new City("City G", 70, country2);
        
        // Create position
        pos = new Position(cityA,cityB,3);
        
        // Create road lists
        List<Road> roadsA = new ArrayList<Road>(),
        roadsB = new ArrayList<>(),
        roadsC = new ArrayList<>(),
        roadsD = new ArrayList<>(),
        roadsE = new ArrayList<>(),
        roadsF = new ArrayList<>(),
        roadsG = new ArrayList<>();

        network1.put(cityA, roadsA);
        network1.put(cityB, roadsB);
        network1.put(cityC, roadsC);
        network1.put(cityD, roadsD);
        network2.put(cityE, roadsE);
        network2.put(cityF, roadsF);
        network2.put(cityG, roadsG);

        // Create roads
        country1.addRoads(cityA, cityB, 4);
        country1.addRoads(cityA, cityC, 3);
        country1.addRoads(cityA, cityD, 5);
        country1.addRoads(cityB, cityD, 2);
        country1.addRoads(cityC, cityD, 2);
        country1.addRoads(cityC, cityE, 4);
        country1.addRoads(cityD, cityF, 3);
        country2.addRoads(cityE, cityC, 4);
        country2.addRoads(cityE, cityF, 2);
        country2.addRoads(cityE, cityG, 5);
        country2.addRoads(cityF, cityD, 3);
        country2.addRoads(cityF, cityG, 6);
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
        assertFalse(pos.hasArrived());
        assertTrue(pos.move());
        assertEquals(2,pos.getDistance());
    }
}
