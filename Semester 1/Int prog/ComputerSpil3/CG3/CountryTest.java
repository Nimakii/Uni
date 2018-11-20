import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * The test class CountryTest.
 *
 * @author  Jens Kristian Nielsen & Thomas Vinther
 * @version Computerspil2
 */
public class CountryTest
{
    private Game game;
    private Country country1, country2;
    private City cityA, cityB, cityC, cityD, cityE, cityF, cityG;
    private Map<City, List<Road>> network1, network2;
    private List<Road> roadsA, roadsB, roadsC, roadsD, roadsE, roadsF, roadsG;
    /**
     * Default constructor for test class CountryTest
     */
    public CountryTest()
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
        network1 = new HashMap<>();
        network2 = new HashMap<>();

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
        roadsA = new ArrayList<>();
        roadsB = new ArrayList<>();
        roadsC = new ArrayList<>();
        roadsD = new ArrayList<>();
        roadsE = new ArrayList<>();
        roadsF = new ArrayList<>();
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
    public void constructor(){
        assertEquals("Country 1",country1.getName());
        assertEquals(network1,country1.getNetwork());
    }

    @Test
    public void setGame(){
        Game game2 = new Game(0);
        country1.setGame(game2);
        assertEquals(game2,country1.getGame());
    }

    @Test
    public void getGame(){
        Game game2 = new Game(0);
        country1.setGame(game2);
        assertEquals(game2,country1.getGame());
    }

    @Test
    public void getNetwork(){
        assertEquals(network1,country1.getNetwork());
        assertEquals(network2,country2.getNetwork());
    }

    @Test
    public void position(){
        Position pos = new Position(cityA,cityA,0);
        assertEquals(pos.getFrom(),country1.position(cityA).getFrom());
        assertEquals(pos.getTo(),country1.position(cityA).getTo());
        assertEquals(pos.getDistance(),country1.position(cityA).getDistance());
    } 

    @Test
    public void readyToTravel(){
        Position posSame = country1.readyToTravel(cityA,cityA); //Dublicate city
        assertEquals(posSame.getFrom(),cityA);
        assertEquals(posSame.getTo(),cityA);
        assertEquals(posSame.getDistance(),0);
        
        Position pos = country1.readyToTravel(cityA,cityB);     //Possible travel
        assertEquals(cityA,pos.getFrom());                      //there is a road from A to B
        assertEquals(cityB,pos.getTo());
        assertEquals(4,pos.getDistance());

        pos = country1.readyToTravel(cityA,cityE);  //Impossible travel
        assertEquals(cityA,pos.getFrom());          //There is no road from A to E
        assertNotEquals(cityE,pos.getTo());
        assertEquals(cityA,pos.getTo());

        country1.addRoads(cityA,cityE,3);           //Making impossible travel possible
        //Now there is a road from A to E
        pos = country2.readyToTravel(cityE,cityA);  //And not one from E to A
        assertEquals(cityE,pos.getFrom());          //Making travel directly from E to A
        assertNotEquals(cityA,pos.getTo());         //Impossible!
        assertNotEquals(3,pos.getDistance());
        assertEquals(cityE,pos.getTo());

        pos = country1.readyToTravel(cityA,cityE);  //Now we prepare to travel from
        assertEquals(cityA,pos.getFrom());          //A to E
        assertEquals(cityE,pos.getTo());
        assertEquals(3,pos.getDistance());

        pos = country2.readyToTravel(cityA,cityE);  //We try to initiate travel from
        assertEquals(cityA,pos.getFrom());          //A to E, within Country2
        assertEquals(cityA,pos.getTo());            //Which we cannot!
        assertEquals(0,pos.getDistance());
    }

    @Test
    public void reset(){
        country1.getCities().forEach(c -> c.changeValue(10));
        assertNotEquals(80,country1.getCity("City A").getValue());
        assertNotEquals(60,country1.getCity("City B").getValue());
        assertNotEquals(40,country1.getCity("City C").getValue());
        assertNotEquals(100,country1.getCity("City D").getValue());
        country1.reset();
        assertTrue(network1.size()>0);                              //test that reset doesnt
        assertEquals("Country 1",country1.getName());               //reset stuff it isnt
        assertEquals(game,country1.getGame());                      //supposed to
        assertEquals(80,country1.getCity("City A").getValue());     
        assertEquals(60,country1.getCity("City B").getValue());
        assertEquals(40,country1.getCity("City C").getValue());
        assertEquals(100,country1.getCity("City D").getValue());
        cityA.arrive(); cityA.arrive(); cityA.arrive();
        cityE.arrive(); cityE.arrive(); cityE.arrive();
        int valueE = cityE.getValue();                          // Remember value of cityE
        country1.reset();
        assertEquals(cityA.getValue(), 80);                     // cityA is reset
        assertEquals(cityE.getValue(), valueE);                 // cityE is unchanged
    }

    @Test
    public void getCity(){
        assertEquals(cityA,country1.getCity("City A"));
        assertNotEquals(cityA,country2.getCity("City A"));
        assertNotEquals(cityA,country1.getCity("cityA"));
    }

    @Test
    public void getCities(){
        List<City> cityList1 = new ArrayList<>();
        cityList1.add(cityA);
        cityList1.add(cityB);
        cityList1.add(cityC);
        assertNotEquals(cityList1,country1.getCities());
        cityList1.add(cityD);
        assertEquals(cityList1,country1.getCities());
        cityList1.add(cityE);
        assertNotEquals(cityList1,country1.getCities());
        network1.put(cityE,roadsE);
        assertEquals(cityList1,country1.getCities());
    }

    @Test
    public void bonus(){
        for(int seed = 0; seed < 1000; seed++) {            // Try 1000 different seeds
            game.getRandom().setSeed(seed);
            int sum = 0;
            Set<Integer> values = new HashSet<>();
            for(int i = 0; i < 10000; i++) {                // Call method 10000 times
                int bonus = country1.bonus(80);
                assertTrue(0<= bonus && bonus <=80);        // Correct interval
                sum += bonus;
                values.add(bonus); }
            assertTrue(350000 < sum && sum < 450000);        // Average close to 40
            assertEquals(values.size(),81);               // All values returned
        }
        for(int seed = 0; seed < 1000; seed++) {        // Try 1000 different seeds
            game.getRandom().setSeed(seed);
            int sum = 0;
            Set<Integer> values = new HashSet<>();
            int bonusTestCount = 10000;
            for(int i = 0; i < bonusTestCount; i++) {   // Call method 10000 times
                int bonus = country1.bonus(80);
                assertTrue(0<= bonus && bonus <=80);    // Correct interval
                sum += bonus;
                values.add(bonus); 
            }
            int avg = sum/bonusTestCount;
            assertTrue(35 < avg && avg < 45);         // Average close to 40
            assertEquals(81,values.size());        // All values returned
        }
    }

    @Test
    public void getRoads(){
        assertEquals(roadsA,country1.getRoads(cityA));
        assertEquals(roadsB,country1.getRoads(cityB));
        ArrayList<Road> empty = new ArrayList<Road>();
        assertEquals(empty,country1.getRoads(cityF)); //cityF lies in country 2 should return empty list. 
        
    }

    @Test
    public void addRoads(){
        int lengthA = roadsA.size();
        int lengthE = roadsE.size();
        country1.addRoads(cityA,cityE,6);
        Road rAE = new Road(cityA,cityE,6);
        assertEquals(0,roadsA.get(lengthA).compareTo(rAE));
        assertEquals(lengthA+1,country1.getRoads(cityA).size());
        assertNotEquals(lengthA,country1.getRoads(cityA).size());
        assertNotEquals(lengthE+1,country2.getRoads(cityE).size());
        assertEquals(lengthE,country2.getRoads(cityE).size());
    }
    
    @Test
    public void equalsTest(){
        Country country1A = new Country("Country 1", network1);
        Country country1B = new Country("Country 1", network2);
        Country country1C = new Country("Country 1", network1);
        Country country1Null = null;
        
        /** Reflexivity */
        assertEquals(country1A,country1A);
        assertEquals(country1B,country1B);
        assertTrue(country1A.equals(country1A));
        assertEquals(country1Null,country1Null);
        //assertTrue(country1Null.equals(country1Null));
        
        /** Symmetry */
        assertEquals(country1,country1B);
        assertEquals(country1B,country1);
        assertEquals(country1,country1A);
        
        /** Transitivity */
        assertEquals(country1,country1A);
        assertEquals(country1A,country1B);
        assertEquals(country1,country1B);
        
        /** Negative tests */
        assertNotEquals(country1,country2);
        assertNotEquals(country1A,null);
        assertNotEquals(country1B,null);
        assertEquals(country1Null,null);
        assertNotEquals(game,country1);
        Road rAE = new Road(cityA,cityE,8);
        assertNotEquals(rAE,country1);
        Position pos = new Position(cityA,cityB,8);
        assertNotEquals(pos,country1);
        assertNotEquals(Math.PI,country1);
        assertNotEquals("Country 1",country1);
    }
    
    @Test
    public void hashCodeTest(){
        Country country1A = new Country("Country 1",network1);
        Country country2A = new Country("Country1",network2);
        Country country2B = new Country("CounTry 1",network2);
        
        /** Consistency */
        assertTrue(country1.equals(country1A));
        assertTrue(country1.hashCode() == country1A.hashCode());
        assertTrue(country1.equals(country1A) && country1.hashCode() == country1A.hashCode());
        
        assertFalse(country1.equals(country2A));
        assertFalse(country1.hashCode() == country2A.hashCode());
        assertFalse(country1.equals(country2B));
        assertFalse(country1.hashCode() == country2B.hashCode());
        
        assertFalse(country1.hashCode() == cityA.hashCode());
        
        assertTrue(country1.hashCode() == 97*"Country 1".hashCode());
        assertFalse(country1.hashCode() == "Country 1".hashCode());
        assertFalse(country2A.hashCode() == "Country1".hashCode());
        assertFalse(country2B.hashCode() == "CounTry 1".hashCode());
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
