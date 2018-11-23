
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
/**
 * The test class CityTest.
 *
 * @author  Jens Kristian & Thomas Vinther
 * @version Computergame 2
 */
public class CityTest
{
    private Game game;
    private Country country1, country2;
    private City cityA, cityB, cityC, cityD, cityE, cityF, cityG;

    /**
     * Default constructor for test class CityTest
     */
    public CityTest()
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

    /**
     * Tests the changeValue method
     */
    @Test
    public void changeValue(){
        assertEquals(80,cityA.getValue());
        cityA.changeValue(-20);
        assertEquals(60,cityA.getValue());
        cityA.changeValue(20);
        assertEquals(80,cityA.getValue());
        cityA.changeValue(-80);
        assertEquals(0,cityA.getValue());
        cityA.changeValue(-Integer.MAX_VALUE);
        assertEquals(-Integer.MAX_VALUE,cityA.getValue());
        cityA.changeValue(2*Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE,cityA.getValue());
    }

    @Test
    public void arrive(){
        for(int i=0; i<10000; i++) { // Try different seeds
            game.getRandom().setSeed(i);    // Set seed
            int bonus = country1.bonus(80); // Remember bonus
            game.getRandom().setSeed(i);    // Reset seed
            int arrive = cityA.arrive();    // Same bonus
            assertEquals(bonus, arrive);
            assertEquals(80-bonus,cityA.getValue());
            cityA.reset(); 
        }
        for(int i=0; i<10000; i++) { // Try different seeds
            game.getRandom().setSeed(i);    // Set seed
            int bonus = country2.bonus(50); // Remember bonus
            game.getRandom().setSeed(i);    // Reset seed
            int arrive = cityE.arrive();    // Same bonus
            assertEquals(bonus, arrive);
            assertEquals(50-bonus,cityE.getValue());
            cityE.reset(); 
        }

    }

    @Test
    public void constructor(){
        assertEquals("City A",cityA.getName());
        assertEquals(80,cityA.getValue());
        assertEquals(country1,cityA.getCountry());
    }

    @Test
    public void reset(){
        assertEquals(80,cityA.getValue());
        cityA.changeValue(30);
        assertEquals(110,cityA.getValue());
        cityA.reset();
        assertEquals(80,cityA.getValue());
    }

    @Test
    public void compareTo(){
   
        /**Test of reflexivity x = x*/
        assertEquals(0,cityA.compareTo(cityA));  
        
        /**Test of transitivity of < a<b & b<c => a<c*/
        assertTrue(cityA.compareTo(cityB) < 0);
        assertTrue(cityB.compareTo(cityC) < 0);
        assertTrue(cityA.compareTo(cityC) < 0);
        
        /**Test of transitivity of > */
        assertTrue(cityC.compareTo(cityB) > 0);
        assertTrue(cityB.compareTo(cityA) > 0);
        assertTrue(cityC.compareTo(cityA) > 0);
        
        /**Test of antisymmetry a<=b & b<=a => a=b*/
        City cityK = new City("City A", 50, country1);
        assertTrue(cityA.compareTo(cityK) >= 0);
        assertTrue(cityA.compareTo(cityK) <= 0);
        assertEquals(0,cityA.compareTo(cityK));
        
        /**Test of symmetry a=b <=> b=a*/
        assertEquals(0,cityA.compareTo(cityK));
        assertEquals(0,cityK.compareTo(cityA));
        assertTrue(cityA.compareTo(cityK) == cityK.compareTo(cityA));
    }
    
        @Test
    public void equals(){
        City cityA2 = new City("City A", 90, country1);
        City cityA3 = new City("City A", 70, country1);
        
        /**Test of reflexivity x = x*/
        assertTrue(cityA.equals(cityA));  
        assertTrue(cityB.equals(cityB));
        
        /**Test of transitivity*/
        assertTrue(cityA.equals(cityA2));
        assertTrue(cityA2.equals(cityA3));
        assertTrue(cityA.equals(cityA3));
              
        /**Test of symmetry*/
        assertTrue(cityA.equals(cityA2) && cityA2.equals(cityA));
        assertTrue(cityA3.equals(cityA) && cityA.equals(cityA3));
        
        /**Test not null */
        assertFalse(cityA.equals(null));
        assertFalse(cityB.equals(null));
        
        /**Negative tests */
        assertFalse(cityA.equals(cityF));       //Different countries. 
        assertFalse(cityF.equals(cityA));
        
    }
    
    @Test
    public void hashCodeTest(){
        City cityA2 = new City("City A", 90, country1);
        City cityA3 = new City("City A", 70, country1);
        City cityNull = null;
        
        /** Test consistent */
        assertTrue(cityA.equals(cityA2) && cityA.hashCode()==cityA2.hashCode());
        assertTrue(cityA2.equals(cityA) && cityA2.hashCode()==cityA.hashCode());
        
        assertFalse(cityA.hashCode() == cityB.hashCode());              //Negated
        assertFalse(cityA.equals(cityB));
        
        
        /**Negative tests */
        assertFalse(cityA.hashCode()=="City A".hashCode()+country1.hashCode());
        assertFalse(cityA.hashCode()=="City A".hashCode());
        assertFalse(cityA.hashCode()==country1.hashCode());
        assertFalse(cityB.hashCode() == cityA.hashCode());       
        
        /** test null??? */
        
        /** Test Fixture*/
        assertNotEquals(cityA.hashCode(),cityB.hashCode());
        assertNotEquals(cityA.hashCode(),cityC.hashCode());
        assertNotEquals(cityA.hashCode(),cityD.hashCode());
        assertNotEquals(cityA.hashCode(),cityE.hashCode());
        assertNotEquals(cityA.hashCode(),cityF.hashCode());
        assertNotEquals(cityA.hashCode(),cityG.hashCode());
        assertNotEquals(cityB.hashCode(),cityC.hashCode());
        assertNotEquals(cityB.hashCode(),cityD.hashCode());
        assertNotEquals(cityB.hashCode(),cityE.hashCode());
        assertNotEquals(cityB.hashCode(),cityF.hashCode());
        assertNotEquals(cityB.hashCode(),cityG.hashCode());
        assertNotEquals(cityC.hashCode(),cityD.hashCode());
        assertNotEquals(cityC.hashCode(),cityE.hashCode());
        assertNotEquals(cityC.hashCode(),cityF.hashCode());
        assertNotEquals(cityC.hashCode(),cityG.hashCode());
        assertNotEquals(cityD.hashCode(),cityE.hashCode());
        assertNotEquals(cityD.hashCode(),cityF.hashCode());
        assertNotEquals(cityD.hashCode(),cityG.hashCode());
        assertNotEquals(cityE.hashCode(),cityF.hashCode());
        assertNotEquals(cityE.hashCode(),cityG.hashCode());
        assertNotEquals(cityF.hashCode(),cityG.hashCode());

        
    }

}
