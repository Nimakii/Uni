
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * The test class MafiaCountryTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class MafiaCountryTest
{
    private Game game;
    private Country country1, country2;
    private City cityA, cityB, cityC, cityD, cityE, cityF, cityG;

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
        country2 = new MafiaCountry("Country 2", network2);
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
     * Test bonus method in a mafia country.
     */
    @Test
    public void bonus(){
        for(int seed = 0; seed < 1000; seed++){
            game.getRandom().setSeed(seed);
            int robs = 0;
            int loss = 0;
            Set<Integer> values = new HashSet<>();
            for(int i = 0; i<50000; i++) {
                int bonus = country2.bonus(80);
                if(bonus < 0) {
                    robs++;
                    assertTrue(bonus >= -51 && bonus <= -10);       //Bonus lies in the interval [-50;-10]
                    loss -= bonus;
                    values.add(-bonus);
                }
            }
            assertTrue(10500 > robs && robs > 9500);                //You get robbed approx. 20% of the time
            assertTrue(robs*35 > loss && robs*25 < loss);           //you usually lose 30 
            assertEquals(41,values.size());                         //The loss can be all values in the interval [10;50], i.e. values has size 41
        } 
    }
    /**
     * Test bonus method in a normal country. 
     */
    @Test
    public void bonusNotSweden(){
        for(int seed = 0; seed < 1000; seed++){
            game.getRandom().setSeed(seed);
            int robs = 0;
            int loss = 0;
            Set<Integer> values = new HashSet<>();
            for(int i = 0; i<50000; i++) {
                int bonus = country1.bonus(80);
                if(bonus < 0) {
                    robs++;
                    assertTrue(bonus >= -51 && bonus <= -10);       //Bonus lies in the interval [-50;-10]
                    loss -= bonus;
                    values.add(-bonus);
                }
            }
            assertTrue(robs==0);                                  
            assertTrue(loss==0);                                  
            assertEquals(0,values.size());                        
        } 
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
}
