package hotciv.standard;

import hotciv.framework.*;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.*;

/** Skeleton class for AlphaCiv test cases

    Updated Oct 2015 for using Hamcrest matchers

   This source code is from the book 
     "Flexible, Reliable Software:
       Using Patterns and Agile Development"
     published 2010 by CRC Press.
   Author: 
     Henrik B Christensen 
     Department of Computer Science
     Aarhus University
   
   Please visit http://www.baerbak.com/ for further information.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/
public class TestAlphaCiv {
  private Game game;

  /** Fixture for alphaciv testing. */
  @Before
  public void setUp() {
    game = new GameImpl();
    assertThat(game, is(notNullValue())); //Refactored from all tests to here
  }

  // FRS p. 455 states that 'Red is the first player to take a turn'.
  @Test
  public void shouldBeRedAsStartingPlayer() {
    assertThat(game.getPlayerInTurn(), is(Player.RED));
  }

  //After red it is blue that is in turn
  @Test
  public void shouldBeBlueAfterRed(){
    assertThat(game.getPlayerInTurn(), is(Player.RED));
    game.endOfTurn();
    assertThat(game.getPlayerInTurn(), is(Player.BLUE));
  }
  //After blue, it is red that is in turn
  @Test
  public void shouldBeRedAfterBlue(){
    assertThat(game.getPlayerInTurn(), is(Player.RED));
    game.endOfTurn();
    assertThat(game.getPlayerInTurn(), is(Player.BLUE));
    game.endOfTurn();
    assertThat(game.getPlayerInTurn(), is(Player.RED));
  }
    /* This test was refactored out after making the next one.
    It was kept to show our work progress.
  //Game starts at year 4000
  @Test
  public void startingAgeShouldBe4000(){
    assertThat(game.getAge(),is(4000));     // Evident and isolated test
  }
     */

  //Each round advances the game age 100 years
  @Test
  public void ageShouldProgress100PrRound(){
    assertThat(game.getAge(),is(4000));
    game.endOfTurn();
    assertThat(game.getAge(),is(4000));
    game.endOfTurn();
    assertThat(game.getAge(),is(3900));
    game.endOfTurn();
    assertThat(game.getAge(),is(3900));
    game.endOfTurn();
    assertThat(game.getAge(),is(3800));
    for (int i = 0; i <8 ; i++) {     //Iterate the remaining 8 turns, since we know the game should end with red winning in 3000BC
      assertThat(game.getAge(),is(3800-100*i));
      game.endOfTurn();
      assertThat(game.getAge(),is(3800-100*i));
      game.endOfTurn();
      assertThat(game.getAge(),is(3700-100*i));
    }
    assertThat(game.getAge(),is(3000));
  }

  //red wins in 3000BC
  @Test
  public void redWinsIn3000BC(){
    for (int i = 0; i < 10; i++) { //progress age to 3000BC
      game.endOfTurn();
      game.endOfTurn();
    }
    assertThat(game.getAge(), is(3000));
    assertThat(game.getWinner(),is(Player.RED));
  }

  //red wins exactly in 3000BC, not before
  @Test
  public void redWinsExactlyIn3000BC(){
    for (int i = 0; i < 9; i++) { //progress age to 3100BC
      game.endOfTurn();
      assertNull(game.getWinner());
      game.endOfTurn();
      assertNull(game.getWinner());
    }
    game.endOfTurn();
    game.endOfTurn();
    assertThat(game.getAge(), is(3000));
    assertThat(game.getWinner(),is(Player.RED));
  }


  //There is ocean at (1,0)
  @Test
  public void shouldBeOceanAtPosition1_0(){
    assertThat(game.getTileAt(new Position(1,0)).getTypeString(),is(GameConstants.OCEANS));
  }

  //There is hills at (0,1)
  @Test
  public void shouldBeHillsAtPosition0_1(){
    assertThat(game.getTileAt(new Position(0,1)).getTypeString(),is(GameConstants.HILLS));
  }

  //There is mountains at (2,2)
  @Test
  public void shouldBeMountainsAtPosition2_2(){
    assertThat(game.getTileAt(new Position(2,2)).getTypeString(),is(GameConstants.MOUNTAINS));
  }

  //There is plains everywhere except from: (1,0), (0,1) & (2,2)
  @Test
  public void shouldBeMostlyPlains(){
    for (int i = 0; i <GameConstants.WORLDSIZE ; i++) {
      for (int j = 0; j <GameConstants.WORLDSIZE ; j++) {
        if((i==2 && j ==2)||(i==0 & j== 1)||(i==1 && j==0)){}     //Positions for known non-plains
        else{                                                     //Rest should be plains
          assertThat(game.getTileAt(new Position(i,j)).getTypeString(),is(GameConstants.PLAINS));
        }
      }
    }
  }



  // Red city is at (1,1)
  @Test
  public void shouldBeRedCityAt1_1(){
    assertThat(game.getCityAt(new Position(1,1)).getOwner(),is(Player.RED));
  }
  //Blue city is at (4,1)
  @Test
  public void shouldBeBlueCityat4_1(){
    assertThat(game.getCityAt(new Position(4,1)).getOwner(),is(Player.BLUE));
  }

  //Red City's population always at 1
  @Test
  public void shouldAlwaysHavePopOfOneInRedCity(){
    assertThat(game.getCityAt(new Position(1,1)).getSize(),is(1));
    game.endOfTurn();
    game.endOfTurn();
    assertThat(game.getCityAt(new Position(1,1)).getSize(),is(1));
    game.endOfTurn();
    game.endOfTurn();
    assertThat(game.getCityAt(new Position(1,1)).getSize(),is(1));
    game.endOfTurn();
    game.endOfTurn();
    assertThat(game.getCityAt(new Position(1,1)).getSize(),is(1));
  }

  //Red should have an archer at (2,0)
  @Test
  public void shouldHaveRedArcherAt2_0(){
    assertThat(game.getUnitAt(new Position(2,0)).getTypeString(),is(GameConstants.ARCHER));
    assertThat(game.getUnitAt(new Position(2,0)).getOwner(),is(Player.RED));
  }

  //Blue has a legion at (3,2)
  @Test
  public void shouldHaveBluelegionAt3_2(){
    assertThat(game.getUnitAt(new Position(3,2)).getTypeString(),is(GameConstants.LEGION));
    assertThat(game.getUnitAt(new Position(3,2)).getOwner(),is(Player.BLUE));
  }

  //Red should have an archer at (2,0)
  @Test
  public void shouldHaveRedSettlerAt4_3(){
    assertThat(game.getUnitAt(new Position(4,3)).getTypeString(),is(GameConstants.SETTLER));
    assertThat(game.getUnitAt(new Position(4,3)).getOwner(),is(Player.RED));
  }

  //The red settler at (4,3) can move to (4,4)
  @Test
  public void settlerShouldMove43_44(){
    Unit settler = game.getUnitAt(new Position(4,3));
    assertThat(game.moveUnit(new Position(4,3),new Position(4,4)),is(true));
    assertThat(game.getUnitAt(new Position(4,4)),is(settler));
    assertNull(game.getUnitAt(new Position(4,3)));
  }

  //The red settler at (4,3) cannot move to (6,6)
  @Test
  public void settlerShouldntMove43_66(){
    Unit settler = game.getUnitAt(new Position(4,3));
    assertThat(game.moveUnit(new Position(4,3),new Position(6,6)),is(false));
    assertThat(game.getUnitAt(new Position(4,3)),is(settler));
    assertNull(game.getUnitAt(new Position(6,6)));
  }

 //Units cannot move over oceans and mountains.
  @Test
  public void unitsShouldNotMoveOverOceansAndMountains(){
    assertThat(game.moveUnit(new Position(2,0),new Position(1,0)),is(false));
    assertThat(game.moveUnit(new Position(3,2),new Position(2,2)),is(false));
  }

  //Attacking units always wins
  @Test
  public void attackingUnitShouldWin(){
    Unit legio = game.getUnitAt(new Position(3,2));
    game.moveUnit(new Position(2,0),new Position(3,1)); //Move the archer into position for sacrifice
    game.endOfTurn(); // Switch to blue player
    game.moveUnit(new Position(3,2),new Position(3,1));   //Move the Legion to attack the archer.
    assertThat(game.getUnitAt((new Position(3,1))),is(legio));
  }


  //Red cannot move blue's units
  @Test
  public void redCannotMoveBluesUnits(){
    assertThat(game.getPlayerInTurn(),is(Player.RED));
    assertThat(game.getUnitAt(new Position(3,2)).getOwner(),is(Player.BLUE));
    assertThat(game.moveUnit(new Position(3,2),new Position(4,2)),is(false));
  }

  //Blue cannot move reds units.
  @Test
  public void blueCannotMoveRedsUnits(){
    game.endOfTurn();
    assertThat(game.getPlayerInTurn(),is(Player.BLUE));
    assertThat(game.getUnitAt(new Position(2,0)).getOwner(),is(Player.RED));
    assertThat(game.moveUnit(new Position(2,0),new Position(3,1)),is(false));
  }

  //Cities produce 6 productions after a round has ended, and saves it in the treasury.
  @Test
  public void cityProduction(){
    assertThat(game.getCityAt(new Position(1,1)).getTreasury(),is(0));
    assertThat(game.getCityAt(new Position(4,1)).getTreasury(),is(0));
    game.endOfTurn();
    game.endOfTurn();
    assertThat(game.getCityAt(new Position(1,1)).getTreasury(),is(6));
    assertThat(game.getCityAt(new Position(4,1)).getTreasury(),is(6));
    game.endOfTurn();
    game.endOfTurn();
    assertThat(game.getCityAt(new Position(1,1)).getTreasury(),is(12));
    assertThat(game.getCityAt(new Position(4,1)).getTreasury(),is(12));
  }

  //Units have attack and defense values dependant on type
  @Test
  public void unitStatsFit(){
    assertThat(game.getUnitAt(new Position(2,0)).getAttackingStrength(),is(2)); //Red archer
    assertThat(game.getUnitAt(new Position(2,0)).getDefensiveStrength(),is(3));
    assertThat(game.getUnitAt(new Position(3,2)).getAttackingStrength(),is(4)); //Blue legion
    assertThat(game.getUnitAt(new Position(3,2)).getDefensiveStrength(),is(2));
    assertThat(game.getUnitAt(new Position(4,3)).getAttackingStrength(),is(0)); //Red settler
    assertThat(game.getUnitAt(new Position(4,3)).getDefensiveStrength(),is(3));
  }

  //No friendly fire
  @Test
  public void noFriendlyFire(){
    game.moveUnit(new Position(2,0),new Position(3,1)); //red archer moves into position
    game.moveUnit(new Position(4,3),new Position(4,2)); //red settler moves into position next to the archer
    game.endOfTurn();
    ((GameImpl) game).placeUnitAt(new UnitImpl(Player.BLUE,GameConstants.ARCHER), new Position(3,3));
    assertThat(game.moveUnit(new Position(3,2),new Position(3,3)),is(false));
    game.endOfTurn();
    assertThat(game.moveUnit(new Position(3,1),new Position(4,2)),is(false));
  }
}
