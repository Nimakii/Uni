
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
    private Country country1, country2, mCountry;
    private City cityA, cityB, cityC, cityD, cityE, cityF, cityG, mCity1, mCity2;

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
        mCountry = new MafiaCountry("Mafia country", network1);
        country1.setGame(game);
        country2.setGame(game);
        mCountry.setGame(game);

        // Create Cities
        cityA = new City("City A", 80, country1);
        cityB = new City("City B", 60, country1);
        cityC = new City("City C", 40, country1);
        cityD = new City("City D", 100, country1);
        cityE = new City("City E", 50, country2);
        cityF = new City("City F", 90, country2);
        cityG = new City("City G", 70, country2);
        mCity1 = new City("mafia city 1",200, mCountry);
        mCity2 = new City("mafia city 2",30, mCountry);

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
    
    /**
     * Tests that the arrive method in the destination City works as intended.
     * 
     * @param from  the City we left
     * @param to    the City we arrive in
     * @param money how much money the player has
     */
    private void arriveTest(City to){
        for(int i=0; i<10000; i++) { // Try different seeds
            game.getRandom().setSeed(i);    // Set seed
            int toInitialValue = to.getValue();
            int bonus = to.getCountry().bonus(to.getValue()); // Remember bonus
            game.getRandom().setSeed(i);    // Reset seed
            int arrive = to.arrive();    // Same bonus
            assertEquals(bonus, arrive);
            if(bonus<0){
                assertEquals(toInitialValue,to.getValue());
            }
            else{
                assertEquals(toInitialValue-bonus,to.getValue());
            }
            to.reset(); 
        }
    }
    private void arriveTestParamater(City to, Player player){
        for(int i=0; i<10000; i++) { // Try different seeds
            game.getRandom().setSeed(i);    // Set seed
            int toInitialValue = to.getValue();
            int bonus = to.getCountry().bonus(to.getValue()); // Remember bonus
            game.getRandom().setSeed(i);    // Reset seed
            int arrive = to.arrive(player);    // Same bonus
            assertEquals(bonus, arrive);
            if(bonus<0){
                assertEquals(toInitialValue,to.getValue());
            }
            else{
                assertEquals(toInitialValue-bonus,to.getValue());
            }
            to.reset(); 
        }
    }
    
    @Test
    public void arrive(){
        arriveTest(cityA);
        arriveTest(cityB);
        arriveTest(cityC);
        arriveTest(cityD);
        arriveTest(cityE);
        arriveTest(cityF);
        arriveTest(cityG);
        arriveTest(mCity1);
        arriveTest(mCity2);
        for(int i=0; i<200; i++){
            City mCity = new City("Test",i,mCountry);
            arriveTest(mCity);
        }
        for(int i=0; i<200; i++){
            City city = new City("Test",i,country1);
            arriveTest(city);
        }
    }
    @Test
    public void arriveParamater(){
        Player player = new Player(new Position(cityA,cityB,0),250);
        arriveTestParamater(cityA,player);
        arriveTestParamater(cityB,player);
        arriveTestParamater(cityC,player);
        arriveTestParamater(cityD,player);
        arriveTestParamater(cityE,player);
        arriveTestParamater(cityF,player);
        arriveTestParamater(cityG,player);
        arriveTestParamater(mCity1,player);
        arriveTestParamater(mCity2,player);
        for(int i=0; i<200; i++){
            City mCity = new City("Test",i,mCountry);
            Player player2 = new Player(new Position(cityA,cityB,0),200-i);
            arriveTestParamater(mCity,player2);
            arriveTestParamater(mCity,player);
        }
        for(int i=0; i<200; i++){
            City city = new City("Test",i,country1);
            Player player2 = new Player(new Position(cityA,cityB,0),200-i);
            arriveTestParamater(city,player2);
            arriveTestParamater(city,player);
        }
        Player poor = new Player(new Position(cityA,mCity1,2),-50);
        arriveTestParamater(cityA,poor);
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
    
    /**
     * Performs reflexivity, transitivity and (anti)symmetry tests on the 4 paramaters, all implementing the Comparable interface,
     * such that a=aEqual and a<b<c.
     * 
     * @param a      some object
     * @param aEqual some object that should test equal to a
     * @param b      some object strictly greater than a
     * @param c      some object strictly greater than both a and b
     */
    private void compareToTest(Comparable a, Comparable aEqual, Comparable b, Comparable c){
        /**Test of reflexivity x = x*/
        assertEquals(0,a.compareTo(a));
        assertEquals(0,aEqual.compareTo(aEqual));
        assertEquals(0,b.compareTo(b));
        assertEquals(0,c.compareTo(c));
        /**Test of transitivity of < a<b & b<c => a<c*/
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(c) < 0);
        assertTrue(a.compareTo(c) < 0);
        /**Test of transitivity of > */
        assertTrue(c.compareTo(b) > 0);
        assertTrue(b.compareTo(a) > 0);
        assertTrue(c.compareTo(a) > 0);
        /**Test of antisymmetry a<=b & b<=a => a=b*/
        assertTrue(a.compareTo(aEqual) >= 0);
        assertTrue(aEqual.compareTo(a) <= 0);
        assertEquals(0,a.compareTo(aEqual));
        /**Test of symmetry a=b <=> b=a*/
        assertEquals(0,a.compareTo(aEqual));
        assertEquals(0,aEqual.compareTo(a));
        assertTrue(a.compareTo(aEqual) == aEqual.compareTo(a));
    }
    @Test
    public void compareTo(){
        City cityK = new City("City A",100,country1);
        compareToTest(cityA,cityK,cityB,cityC);
    }
    
    private void equalsTest(Object a, Object b, Object c, Object notA){
        /**Test of reflexivity x = x*/
        assertTrue(a.equals(a));  
        /**Test of transitivity of < a<b & b<c => a<c*/
        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
        /**Test of transitivity of > */
        assertTrue(c.equals(b));
        assertTrue(b.equals(a));
        assertTrue(c.equals(a));
        /**Test of symmetry a=b <=> b=a*/
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertTrue(a.equals(b) == b.equals(a));
        /**Negative tests*/
        assertFalse(a.equals(notA));
        assertFalse(notA.equals(a));
        assertFalse(a.equals(null));
        assertFalse(a.equals(3));
        assertFalse(a.equals(Math.PI));
        assertFalse(a.equals(a.getClass()));
        assertFalse(a.equals(""));
    }
    @Test
    public void equals(){
        City cityA2 = new City("City A", 90, country1);
        City cityA3 = new City("City A", 70, country1);
        City cityAc2 = new City("City A", 95, country2);
        City cityAalmost = new City("CityA", 92, country1);
        equalsTest(cityA,cityA2,cityA3,cityAc2);
        equalsTest(cityA,cityA2,cityA3,cityAalmost);
        /**More negative tests */
        assertFalse(cityA.equals(cityF));       //Different countries. 
        assertFalse(cityF.equals(cityA));
        assertFalse(cityA.equals(country1));
        assertFalse(cityA.equals(country2));
        assertFalse(cityA.equals(new Position(cityB,cityC,3)));
        assertFalse(cityA.equals(game));
    }
    
    @Test
    public void hashCodeTest(){
        City cityA2 = new City("City A", 90, country1);
        City cityA3 = new City("City A", 70, country1);
        
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
