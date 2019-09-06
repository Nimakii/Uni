package hotciv.standard;


import hotciv.framework.GameConstants;
import hotciv.framework.Tile;

public class TileImpl implements Tile {
  private String terrainType;

  public TileImpl(String terrainType){
    this.terrainType = terrainType;
  }

  @Override
  public String getTypeString(){
    return terrainType;
  }
}
