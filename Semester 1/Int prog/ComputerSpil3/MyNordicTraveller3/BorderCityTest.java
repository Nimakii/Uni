
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * The test class BorderCityTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class BorderCityTest
{
    private Game game;
    private Country country1, country2;
    private City cityA, cityB, cityC, cityD, cityE, cityF, cityG;

    /**
     * Default constructor for test class BorderCityTest
     */
    public BorderCityTest()
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
        cityC = new BorderCity("City C", 40, country1);
        cityD = new BorderCity("City D", 100, country1);
        cityE = new BorderCity("City E", 50, country2);
        cityF = new BorderCity("City F", 90, country2);
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

    @Test
    public void arrive(){
        for(int i=0; i<10000; i++) { /** regular to border, same country1 */
            Player player = new Player(new Position(cityA, cityC, 0), 250);
            game.getRandom().setSeed(i);    // Set seed
            int bonus = country1.bonus(40); // Remember bonus
            game.getRandom().setSeed(i);    // Reset seed
            int arrive = cityC.arrive(player);    // Same bonus
            assertEquals(bonus, arrive);
            assertEquals(40-bonus,cityC.getValue());
            cityC.reset(); 
        }
        for(int i=0; i<10000; i++) { /** border to regular, same country1 */
            Player player = new Player(new Position(cityC, cityA, 0), 2500000);
            game.getRandom().setSeed(i);    // Set seed
            int bonus = country1.bonus(80); // Remember bonus
            game.getRandom().setSeed(i);    // Reset seed
            int arrive = cityA.arrive(player);    // Same bonus
            assertEquals(bonus, arrive);
            assertEquals(80-bonus,cityA.getValue());
            cityA.reset(); 
        }
        for(int i=0; i<10000; i++) { /** regular to border, same country2 */
            Player player = new Player(new Position(cityG, cityF, 0), 250);
            game.getRandom().setSeed(i);    // Set seed
            int bonus = country2.bonus(90); // Remember bonus
            game.getRandom().setSeed(i);    // Reset seed
            int arrive = cityF.arrive(player);    // Same bonus
            assertEquals(bonus, arrive);
            assertEquals(90-bonus,cityF.getValue());
            cityF.reset(); 
        }
    }

    @Test
    public void arriveFromOtherCountry() {
        for(int i=0; i<1000; i++) { /** two border cities in different countries */
            Player player = new Player(new Position(cityE, cityC, 0), 250);
            game.getRandom().setSeed(i);            // Set seed
            int bonus = country1.bonus(40);         // Remember bonus
            int toll = country1.getGame().getSettings().getTollToBePaid()*player.getMoney()/100;     // Calculate toll
            game.getRandom().setSeed(i);            // Reset seed
            int arrive = cityC.arrive(player);      // Same bonus
            assertEquals(bonus-toll,arrive);
            assertEquals(40-(bonus-toll),cityC.getValue());
            cityC.reset(); 
        } 
        for(int i=0; i<1000; i++) { /** regular to border, crossing border */
            Player player = new Player(new Position(cityG, cityC, 0), 250);
            game.getRandom().setSeed(i);            // Set seed
            int bonus = country1.bonus(40);         // Remember bonus
            int toll = country1.getGame().getSettings().getTollToBePaid()*player.getMoney()/100;     // Calculate toll
            game.getRandom().setSeed(i);            // Reset seed
            int arrive = cityC.arrive(player);      // Same bonus
            assertEquals(bonus-toll,arrive);
            assertEquals(40-(bonus-toll),cityC.getValue());
            cityC.reset(); 
        }
        for(int i=0; i<1000; i++) { /** border to regular, crossing border */
            Player player = new Player(new Position(cityC, cityG, 0), 250);
            game.getRandom().setSeed(i);            // Set seed
            int bonus = country1.bonus(70);         // Remember bonus
            //int toll = country1.getGame().getSettings().getTollToBePaid()*player.getMoney()/100;     // Calculate toll
            game.getRandom().setSeed(i);            // Reset seed
            int arrive = cityG.arrive(player);      // Same bonus
            assertEquals(bonus,arrive);
            assertEquals(70-(bonus),cityG.getValue());
            cityG.reset(); 
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
