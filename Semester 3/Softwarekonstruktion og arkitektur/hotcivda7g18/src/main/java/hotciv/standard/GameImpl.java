package hotciv.standard;

import hotciv.framework.*;

import java.util.HashMap;
import java.util.Map;

/** Skeleton implementation of HotCiv.
 
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

public class GameImpl implements Game {
  private TileImpl[][] worldLayout;
  private Player playerInTurn;
  private int currentAge;
  private Map<Position,Unit> unitPositions;
  private Map<Position,CityImpl> cityPositions;

  /*
   *Initialises AlphaCiv
   */
  public GameImpl(){
    this.playerInTurn = Player.RED;   //Starting player
    this.currentAge = 4000;           //Starting age

    //Making the worldLayout version AlphaCiv
    this.worldLayout = new TileImpl[GameConstants.WORLDSIZE][GameConstants.WORLDSIZE];
    for (int i = 0; i < GameConstants.WORLDSIZE ; i++) {
      for (int j = 0; j < GameConstants.WORLDSIZE; j++) {
        worldLayout[i][j] = new TileImpl(GameConstants.PLAINS);
      }
    }
    this.worldLayout[1][0] = new TileImpl(GameConstants.OCEANS);
    this.worldLayout[0][1] = new TileImpl(GameConstants.HILLS);
    this.worldLayout[2][2] = new TileImpl(GameConstants.MOUNTAINS);

    //Making the unitPositions version AlphaCiv
    this.unitPositions = new HashMap<>();
    this.unitPositions.put(new Position(2, 0), new UnitImpl(Player.RED,GameConstants.ARCHER));
    this.unitPositions.put(new Position(3, 2), new UnitImpl(Player.BLUE,GameConstants.LEGION));
    this.unitPositions.put(new Position(4, 3), new UnitImpl(Player.RED,GameConstants.SETTLER));

    //Making the cityPositions version AlphaCiv
    this.cityPositions = new HashMap<>();
    this.cityPositions.put(new Position(1,1),new CityImpl(Player.RED));
    this.cityPositions.put(new Position(4,1),new CityImpl(Player.BLUE));



  }

  public Tile getTileAt( Position p ) { return worldLayout[p.getRow()][p.getColumn()]; }
  public Unit getUnitAt( Position p ) { return unitPositions.get(p); }
  public City getCityAt( Position p ) { return cityPositions.get(p); }    //TODO Fake it code
  public Player getPlayerInTurn() { return playerInTurn; }
  public int getAge() { return currentAge; }

  /*
   *Red is the winner in age 3000
   * @return Winning player
   */
  public Player getWinner() {
    if(currentAge!=3000){
      return null;
    }
    else {
      return Player.RED;
     }
  }

  /*
   * Moves a unit, from a postion in the game world to another, if there is an obstacle on the desired destination, the move is not carried out.
   * This also simulates combat, as moving into another Postion already occupied by another unit destroying that unit, regardless of faction.
   * @return If the move succeded
   * @param Postion from
   * @param Position to
   */
  public boolean moveUnit( Position from, Position to ) {
    boolean noFriendlyFire = true;
    if(unitPositions.get(to) != null &&
            unitPositions.get(to).getOwner() == playerInTurn){
      noFriendlyFire = false;
    }
    if(distance(from,to)<=getUnitAt(from).getMoveCount() && //distance ok?
            playerInTurn == getUnitAt(from).getOwner() && //dont move opponents units
            noFriendlyFire //friendly fire not allowed
    ){
      String tileTypeTo = getTileAt(to).getTypeString();
      if(tileTypeTo.equals(GameConstants.OCEANS)||tileTypeTo.equals(GameConstants.MOUNTAINS)){
        return false;
      }
      unitPositions.put(to,unitPositions.get(from));
      unitPositions.remove(from);
      return true;
    }
    return false;
  }

  /**
   * Distance function, calculating the number of tiles from one Position to another, where a move allows to move to any adjacent tile
   *  @return The distance as a natural number
   *  @param p1 position 1
   *  @param p2 position 2
   */
  private int distance(Position p1, Position p2){
    return Math.max(Math.abs(p1.getRow()-p2.getRow()),Math.abs(p1.getColumn()-p2.getColumn()));
  }

  /*
   *  A very simple implementation of progressing the game, switching the active player,
   *  and when switching to red progresses the game by a 100 years.
   */
  public void endOfTurn() {
    if(this.playerInTurn == Player.RED){ //change from red to blue
      this.playerInTurn = Player.BLUE;
    }
    else{ //change from blue to red, and ends the round
      this.playerInTurn = Player.RED;
      this.currentAge -= 100;
      cityPositions.forEach( (Position p, CityImpl c) -> c.increaseTreasury(6));
    }
  }

  public void changeWorkForceFocusInCityAt( Position p, String balance ) {}
  public void changeProductionInCityAt( Position p, String unitType ) {}
  public void performUnitActionAt( Position p ) {}

  /**
   * Places the unit at the given position, note that this can place new units.
   * @param unit to be placed
   * @param location where the unit is placed
   */
  public void placeUnitAt(Unit unit, Position location ){
    String tileTypeTo = getTileAt(location).getTypeString();
    if((!tileTypeTo.equals(GameConstants.OCEANS))&&(!tileTypeTo.equals(GameConstants.MOUNTAINS))){
      unitPositions.put(location,unit);
    }
  }
}
