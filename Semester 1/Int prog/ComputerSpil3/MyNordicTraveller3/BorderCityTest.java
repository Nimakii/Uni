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

    private void arriveTest(City from, City to, int money){
        for(int i=0; i<1000; i++) {
            Player player = new Player(new Position(from,to, 0), money);
            game.getRandom().setSeed(i);             // Set seed
            int toInitialValue = to.getValue();
            int bonus = to.getCountry().bonus(toInitialValue);         // Remember bonus
            int toll = 0;
            if(!from.getCountry().equals(to.getCountry()) 
            && to.getClass()==BorderCity.class){ //check if toll is applicable
                toll = to.getCountry().getGame().getSettings().getTollToBePaid()
                *player.getMoney()/100;     // Calculate toll
            }
            int bonusAndToll = bonus-toll;
            game.getRandom().setSeed(i);             // Reset seed
            int arrive = to.arrive(player);          
            assertEquals(bonusAndToll,arrive);
            assertEquals(toInitialValue-bonusAndToll,to.getValue());
            to.reset(); 
        }
    }

    @Test
    public void arrive(){
        for(int j=-50;j<100;j++){ /** testing with different values */
            cityA = new City("City A", j, country1);
            Random random = new Random();
            double multiplier = random.nextGaussian()*random.nextInt(5); /** double in [0,4]*/
            if(random.nextInt(2)>0){ /** 50/50 chance of negative */
                multiplier = -1*multiplier;
            }
            cityB = new City("City B", ((int) (j*multiplier)), country1); /** city with value in [0,4j] */
            multiplier = random.nextGaussian()*random.nextInt(5);
            if(random.nextInt(2)>0){
                multiplier = -1*multiplier;
            }
            cityC = new BorderCity("City C", ((int) (j*multiplier)), country1);
            for(int m=-50;m<100;m++){
                arriveTest(cityA,cityC,m); /** regular to border, same country1 */
                arriveTest(cityC,cityA,m); /** border to regular, same country1 */
                arriveTest(cityG,cityF,m); /** regular to border, same country2 */
            }
        }
    }

    @Test
    public void arriveFromOtherCountry() {
        for(int j=-50;j<100;j++){ /** testing with different values */
            cityE = new BorderCity("City E", j, country1);
            Random random = new Random();
            double multiplier = random.nextGaussian()*random.nextInt(5); /** double in [0,4]*/
            if(random.nextInt(2)>0){ /** 50/50 chance of negative */
                multiplier = -1*multiplier;
            }
            cityC = new BorderCity("City C", ((int) (j*multiplier)), country2); /** city with value in [0,4j] */
            
            multiplier = random.nextGaussian()*random.nextInt(5);
            if(random.nextInt(2)>0){
                multiplier = -1*multiplier;
            }
            cityG = new City("City G", ((int) (j*multiplier)), country2);
            
            for(int m=-50;m<100;m++){
                arriveTest(cityE,cityC,m); /** border to border, crossing border */
                arriveTest(cityG,cityC,m); /** regular to border, crossing border */
                arriveTest(cityC,cityG,m); /** border to regular, crossing border */
            }
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
