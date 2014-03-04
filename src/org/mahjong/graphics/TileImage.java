package org.mahjong.graphics;

import java.util.Arrays;





import org.mahjong.client.Suit;
import org.mahjong.client.Rank;
import org.mahjong.client.Tile;
import org.mahjong.client.Equality;


public class TileImage extends Equality{
	enum TileImageKind {BACK,NORMAL};
	enum TileDirection {LEFT, RIGHT, HORIZONTAL, VERTICAL}
	public static class Factory {
	    public static TileImage getBackOfTileImage(String direction) {
	    	switch (direction) {
	    	case "VERTICAL":
	    		return new TileImage(TileImageKind.BACK, TileDirection.VERTICAL, null);
	    	case "HORIZONTAL":
	    		return new TileImage(TileImageKind.BACK, TileDirection.HORIZONTAL, null);
	    	default:
	    		return new TileImage(TileImageKind.BACK, TileDirection.HORIZONTAL, null);
	    	}
	    }

	    public static TileImage getTileImage(String direction, Tile tile) {
	    	switch (direction) {
	    	case "LEFT":
	  	        return new TileImage(TileImageKind.NORMAL, TileDirection.LEFT, tile);
	    	case "RIGHT":
	  	        return new TileImage(TileImageKind.NORMAL, TileDirection.RIGHT, tile);
	    	default:
	  	        return new TileImage(TileImageKind.NORMAL, TileDirection.HORIZONTAL, tile);
	    	}
	    }
	  }
	public final TileImageKind kind;
	public final TileDirection direction;
	public final Tile tile;
	
	private TileImage(TileImageKind kind, TileDirection direction, Tile tile) {
		this.direction = direction;
	    this.kind = kind;
	    this.tile = tile;
	  }
   @Override
	public Object getId() {
	    return Arrays.asList(kind, direction, tile);
    }
   private String tile2str(Tile tile)
   {
	   return tile.toString(); 
   }
   @Override
   public String toString() {
     switch (kind) {
       case BACK:
    	   if (direction == TileDirection.HORIZONTAL) {
    		   return "Images/Tiles/b.jpg";    		   
    	   } else {
    		   return "Images/Tiles/b_v.jpg";
    	   }

       case NORMAL:
    	   if (direction == TileDirection.LEFT) {
    		   return "Images/Tiles/" + tile2str(tile) + "_l.jpg";    		   
    	   } else if (direction == TileDirection.RIGHT) {
    		   return "Images/Tiles/" + tile2str(tile) + "_r.jpg";
    	   } else {
    		   return "Images/Tiles/" + tile2str(tile) + ".jpg";
    	   }
       default:
         return "Forgot kind=" + kind + "_" + direction;
     }
   }
}
