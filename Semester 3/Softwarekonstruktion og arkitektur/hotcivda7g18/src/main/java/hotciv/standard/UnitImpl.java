package hotciv.standard;

import hotciv.framework.GameConstants;
import hotciv.framework.Player;
import hotciv.framework.Unit;

public class UnitImpl implements Unit {
  private final Player owner; //final, this is not age of empires. Wololo
  private final String type;
  private final int attackingStrength;
  private final int defensiveStrength;

  /*
   *  Initialises the unit
   */
  public UnitImpl(Player owner, String type){
    this.owner = owner;
    this.type = type;
    if(type.equals(GameConstants.ARCHER)){
      this.attackingStrength = 2;
      this.defensiveStrength = 3;
    } else if(type.equals(GameConstants.LEGION)){
      this.attackingStrength = 4;
      this.defensiveStrength = 2;
    } else if(type.equals(GameConstants.SETTLER)){
      this.attackingStrength = 0;
      this.defensiveStrength = 3;
    } else {
      this.attackingStrength = 0;
      this.defensiveStrength = 0;
    }

  }

  @Override
  public String getTypeString() {
    return type;
  }

  @Override
  public Player getOwner() {
    return owner;
  }

  @Override
  public int getMoveCount() {
    return 1;
  }

  @Override
  public int getDefensiveStrength() {
    return defensiveStrength;
  }

  @Override
  public int getAttackingStrength() {
    return attackingStrength;
  }
}
