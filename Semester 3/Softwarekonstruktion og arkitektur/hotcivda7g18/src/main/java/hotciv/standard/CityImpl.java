package hotciv.standard;

import hotciv.framework.City;
import hotciv.framework.Player;

public class CityImpl implements City {
  private Player owner;
  private int treasury;

  public CityImpl(Player p) {
    this.owner = p;
    this.treasury = 0;
  }

  @Override
  public Player getOwner() {      // TODO: Fake it
    return owner;
  }

  /*
   * Returns the size of the city. In AlphaCiv the size is always 1.
   * @return size of the city as a natural number
   */
  @Override
  public int getSize() {
    return 1;
  }

  @Override
  public int getTreasury() {
    return this.treasury;
  }
  public void increaseTreasury(int increment){
    this.treasury += increment;
  }

  @Override
  public String getProduction() {
    return null;
  }

  @Override
  public String getWorkforceFocus() {
    return null;
  }
}
