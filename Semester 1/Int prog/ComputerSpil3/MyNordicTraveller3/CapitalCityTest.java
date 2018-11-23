import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
/**
 * The test class CapitalCityTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class CapitalCityTest
{
    private Game game;
    private Country country1, country2;
    private City cityA, cityB, cityC, cityD, cityE, cityF, cityG;

    /**
     * Default constructor for test class CapitalCityTest
     */
    public CapitalCityTest()
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
        cityD = new CapitalCity("City D", 100, country1);
        cityE = new CapitalCity("City E", 50, country2);
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
    
    @Test
    public void arriveDifferent(){
        for(int i=0; i<1000; i++) { /** regular to capital, different countries */
            Player player = new Player(new Position(cityF, cityD, 0), 250);
            game.getRandom().setSeed(i);             // Set seed
            int bonus = country1.bonus(100);         // Remember bonus
            int toll = country1.getGame().getSettings().getTollToBePaid()*player.getMoney()/100;     // Calculate toll
            int bonusAndToll = bonus-toll;
            int spending = country1.getGame().getRandom().nextInt(player.getMoney()+1+bonusAndToll); //Calculate how much money the player spends in the capital.
            game.getRandom().setSeed(i);             // Reset seed
            int arrive = cityD.arrive(player);       // Same bonus
            assertEquals(bonusAndToll-spending,arrive);
            assertEquals(100-(bonusAndToll-spending),cityD.getValue());
            cityD.reset(); 
        }
        for(int i=0; i<1000; i++) { /** capital to capital, different countries */
            Player player = new Player(new Position(cityE, cityD, 0), 250);
            game.getRandom().setSeed(i);             // Set seed
            int bonus = country1.bonus(100);         // Remember bonus
            int toll = country1.getGame().getSettings().getTollToBePaid()*player.getMoney()/100;     // Calculate toll
            int bonusAndToll = bonus-toll;
            int spending = country1.getGame().getRandom().nextInt(player.getMoney()+1+bonusAndToll); //Calculate how much money the player spends in the capital.
            game.getRandom().setSeed(i);             // Reset seed
            int arrive = cityD.arrive(player);       // Same bonus
            assertEquals(bonusAndToll-spending,arrive);
            assertEquals(100-(bonusAndToll-spending),cityD.getValue());
            cityD.reset(); 
        }
        for(int i=0; i<1000; i++) { /** capital to regular, different countries */
            Player player = new Player(new Position(cityE, cityA, 0), 250);
            game.getRandom().setSeed(i);             // Set seed
            int bonus = country1.bonus(80);         // Remember bonus
            int toll = 0; /** destination not border */
            int bonusAndToll = bonus-toll;
            int spending = 0; /** destination is not a capital */
            game.getRandom().setSeed(i);             // Reset seed
            int arrive = cityA.arrive(player);       // Same bonus
            assertEquals(bonusAndToll-spending,arrive);
            assertEquals(80-(bonusAndToll-spending),cityA.getValue());
            cityA.reset(); 
        }
        City cityH = new BorderCity("City H", 120, country1);
        for(int i=0; i<1000; i++) { /** capital to border, different countries */
            Player player = new Player(new Position(cityE, cityH, 0), 250);
            game.getRandom().setSeed(i);             // Set seed
            int bonus = country1.bonus(120);         // Remember bonus
            int toll = country1.getGame().getSettings().getTollToBePaid()*player.getMoney()/100;     // Calculate toll
            int bonusAndToll = bonus-toll;
            int spending = 0; /** destination is not a capital */
            game.getRandom().setSeed(i);             // Reset seed
            int arrive = cityH.arrive(player);       // Same bonus
            assertEquals(bonusAndToll-spending,arrive);
            assertEquals(120-(bonusAndToll-spending),cityH.getValue());
            cityH.reset(); 
        }
        for(int i=0; i<1000; i++) { /** border to capital, different countries */
            Player player = new Player(new Position(cityH, cityE, 0), 250);
            game.getRandom().setSeed(i);             // Set seed
            int bonus = country2.bonus(50);         // Remember bonus
            int toll = country2.getGame().getSettings().getTollToBePaid()*player.getMoney()/100;     // Calculate toll
            int bonusAndToll = bonus-toll;
            int spending = country1.getGame().getRandom().nextInt(player.getMoney()+1+bonusAndToll); //Calculate how much money the player spends in the capital.
            game.getRandom().setSeed(i);             // Reset seed
            int arrive = cityE.arrive(player);       // Same bonus
            assertEquals(bonusAndToll-spending,arrive);
            assertEquals(50-(bonusAndToll-spending),cityE.getValue());
            cityE.reset(); 
        } 
    }
    
    @Test
    public void arriveSame(){
        for(int i=0; i<1000; i++) { /** regular to capital, same country1 */
            Player player = new Player(new Position(cityA, cityD, 0), 250);
            game.getRandom().setSeed(i);             // Set seed
            int bonus = country1.bonus(100);         // Remember bonus
            int toll = 0; /** same country1 */
            int bonusAndToll = bonus-toll;
            int spending = country1.getGame().getRandom().nextInt(player.getMoney()+1+bonusAndToll); //Calculate how much money the player spends in the capital.
            game.getRandom().setSeed(i);             // Reset seed
            int arrive = cityD.arrive(player);       // Same bonus
            assertEquals(bonusAndToll-spending,arrive);
            assertEquals(100-(bonusAndToll-spending),cityD.getValue());
            cityD.reset(); 
        } 
        for(int i=0; i<1000; i++) { /** capital to regular, same country1 */
            Player player = new Player(new Position(cityD, cityA, 0), 250);
            game.getRandom().setSeed(i);             // Set seed
            int bonus = country1.bonus(80);         // Remember bonus
            int toll = 0; /** same country1 */
            int bonusAndToll = bonus-toll;
            int spending = 0; /** destination is not a capital */
            game.getRandom().setSeed(i);             // Reset seed
            int arrive = cityA.arrive(player);       // Same bonus
            assertEquals(bonusAndToll-spending,arrive);
            assertEquals(80-(bonusAndToll-spending),cityA.getValue());
            cityA.reset(); 
        } 
        City cityH = new BorderCity("City H", 120, country2);
        for(int i=0; i<1000; i++) { /** capital to border, same country2 */
            Player player = new Player(new Position(cityE, cityH, 0), 250);
            game.getRandom().setSeed(i);             // Set seed
            int bonus = country2.bonus(120);         // Remember bonus
            int toll = 0; /** same country2 */
            int bonusAndToll = bonus-toll;
            int spending = 0; /** destination is not a capital */
            game.getRandom().setSeed(i);             // Reset seed
            int arrive = cityH.arrive(player);       // Same bonus
            assertEquals(bonusAndToll-spending,arrive);
            assertEquals(120-(bonusAndToll-spending),cityH.getValue());
            cityH.reset(); 
        } 
        for(int i=0; i<1000; i++) { /** border to capital, same country2 */
            Player player = new Player(new Position(cityH, cityE, 0), 250);
            game.getRandom().setSeed(i);             // Set seed
            int bonus = country2.bonus(50);         // Remember bonus
            int toll = 0; /** same country2 */
            int bonusAndToll = bonus-toll;
            int spending = country1.getGame().getRandom().nextInt(player.getMoney()+1+bonusAndToll); //Calculate how much money the player spends in the capital.
            game.getRandom().setSeed(i);             // Reset seed
            int arrive = cityE.arrive(player);       // Same bonus
            assertEquals(bonusAndToll-spending,arrive);
            assertEquals(50-(bonusAndToll-spending),cityE.getValue());
            cityE.reset(); 
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
