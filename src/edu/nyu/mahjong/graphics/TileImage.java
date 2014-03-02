package edu.nyu.mahjong.graphics;

import java.util.Arrays;





import edu.nyu.mahjong.logic.Suit;
import edu.nyu.mahjong.logic.Rank;
import edu.nyu.mahjong.logic.Tile;
import edu.nyu.mahjong.iface.Equality;


public class TileImage extends Equality{
	enum TileImageKind {BACK,NORMAL,}
	public static class Factory {
	    public static TileImage getBackOfTileImage() {
	      return new TileImage(TileImageKind.BACK, null);
	    }

	    public static TileImage getTileImage(Tile tile) {
	      return new TileImage(TileImageKind.NORMAL, tile);
	    }
	  }
	public final TileImageKind kind;
	public final Tile tile;
	
	private TileImage(TileImageKind kind, Tile tile) {
	    this.kind = kind;
	    this.tile = tile;
	  }
   @Override
	public Object getId() {
	    return Arrays.asList(kind, tile);
    }
   private String tile2str(Tile tile)
   {
	   return tile.toString(); 
   }
   @Override
   public String toString() {
     switch (kind) {
       case BACK:
         return "tiles/b.gif";
       case NORMAL:
         return "tiles/" + tile2str(tile) + ".gif";
       default:
         return "Forgot kind=" + kind;
     }
   }
}
