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
    
    /**
     * Tests that the arrive method in the destination City works as intended.
     * Works with CapitalCity, BorderCity and City.
     * 
     * @param from  the City or BorderCity or CapitalCity we left
     * @param to    the City or BorderCity or CapitalCity we arrive in
     * @param money how much money the player has
     */
    private void arriveTest(City from, City to,int money){
        for(int i=0; i<1000; i++) {
            Player player = new Player(new Position(from,to, 0), money);
            game.getRandom().setSeed(i);             // Set seed
            int toInitialValue = to.getValue();
            int bonus = to.getCountry().bonus(toInitialValue);         // Remember bonus
            int toll = 0;
            if(!from.getCountry().equals(to.getCountry())
               && (to.getClass()==BorderCity.class
                   || to.getClass()==CapitalCity.class)){ //check if toll is applicable
                toll = to.getCountry().getGame().getSettings().getTollToBePaid()
                       *player.getMoney()/100;     // Calculate toll
            }
            int bonusAndToll = bonus-toll;
            int spending = 0;
            if(to.getClass()==CapitalCity.class){ //check if the destination is a capital
                spending = to.getCountry().getGame().getRandom().nextInt(player.getMoney()
                           +1+bonusAndToll); //Calculate how much money the player spends in the capital.
            }
            game.getRandom().setSeed(i);             // Reset seed
            int arrive = to.arrive(player);          
            assertEquals(bonusAndToll-spending,arrive);
            assertEquals(toInitialValue-(bonusAndToll-spending),to.getValue());
            to.reset(); 
        }
    }
    
    @Test
    public void arriveDifferent(){
        for(int j=-50;j<100;j++){ /** testing with different values */
            cityF = new City("City E", j, country1);
            Random random = new Random();
            double multiplier = random.nextGaussian()*random.nextInt(5); /** double in [0,4]*/
            if(random.nextInt(2)>0){ /** 50/50 chance of negative */
                multiplier = -1*multiplier;
            }
            cityD = new CapitalCity("City C", ((int) (j*multiplier)), country2); /** city with integer value in [0,4j] */
            
            multiplier = random.nextGaussian()*random.nextInt(5);
            if(random.nextInt(2)>0){
                multiplier = -1*multiplier;
            }
            cityE = new CapitalCity("City G", ((int) (j*multiplier)), country2); /** city with integer value in [0,4j] */
            
            multiplier = random.nextGaussian()*random.nextInt(5);
            if(random.nextInt(2)>0){
                multiplier = -1*multiplier;
            }
            City cityH = new BorderCity("City H", ((int) (j*multiplier)), country1); /** city with integer value in [0,4j] */
            
            for(int m=-50;m<100;m++){
                arriveTest(cityF,cityD,250); /** regular to capital, different countries */
                arriveTest(cityE,cityD,250); /** capital to capital, different countries */
                arriveTest(cityE,cityA,250); /** capital to regular, different countries */
                arriveTest(cityE,cityH,250); /** capital to border, different countries */
                arriveTest(cityH,cityE,250); /** border to capital, different countries */
            }
        }
    }
    
    @Test
    public void arriveSame(){
        for(int j=-50;j<100;j++){ /** testing with different values */
            cityA = new City("City E", j, country1);
            Random random = new Random();
            double multiplier = random.nextGaussian()*random.nextInt(5); /** double in [0,4]*/
            if(random.nextInt(2)>0){ /** 50/50 chance of negative */
                multiplier = -1*multiplier;
            }
            cityD = new CapitalCity("City C", ((int) (j*multiplier)), country2); /** city with value in [0,4j] */
            
            multiplier = random.nextGaussian()*random.nextInt(5);
            if(random.nextInt(2)>0){
                multiplier = -1*multiplier;
            }
            cityE = new CapitalCity("City G", ((int) (j*multiplier)), country2); /** city with value in [0,4j] */
            
            multiplier = random.nextGaussian()*random.nextInt(5);
            if(random.nextInt(2)>0){
                multiplier = -1*multiplier;
            }
            City cityH = new BorderCity("City H", ((int) (j*multiplier)), country1); /** city with value in [0,4j] */
            
            multiplier = random.nextGaussian()*random.nextInt(5);
            if(random.nextInt(2)>0){
                multiplier = -1*multiplier;
            }
            City cityI = new CapitalCity("City I", ((int) (j*multiplier)), country1); /** city with value in [0,4j] */
            
            for(int m=-50;m<100;m++){
                arriveTest(cityA,cityD,250); /** regular to capital, same country1 */
                arriveTest(cityD,cityA,250); /** capital to regular, same country1 */
                arriveTest(cityI,cityD,250); /** capital to capital, same country1 */
                arriveTest(cityE,cityH,250); /** capital to border, same country2 */
                arriveTest(cityH,cityE,250); /** border to capital, same country2 */
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
