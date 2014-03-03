package org.mahjong.graphics;




import org.mahjong.client.Tile;

import com.google.gwt.resources.client.ImageResource;

import org.mahjong.client.Suit;
import org.mahjong.client.Rank;

public class TileImageSupplier {

	private final TileImages tileImages;

	  public TileImageSupplier(TileImages tileImages) {
	    this.tileImages = tileImages;
	  }
	  
	public ImageResource getResource(TileImage tileImage) {
		    switch (tileImage.kind) {
		      case BACK:
		        return getBackOfTileImage();
		 
		      case NORMAL:
		        return getTileImage(tileImage.tile);
		      default:
		        throw new RuntimeException("Forgot kind=" + tileImage.kind);
		    }
		  } 
	public ImageResource getBackOfTileImage() {
	    return tileImages.b();
	  } 
	public ImageResource getTileImage(Tile tile) {
		//TODO: return the TileImage of certain tile
		Suit tileSuit=tile.getSuit();
		Rank tileRank=tile.getRank();
		switch (tileSuit)
		{
		 case ACHARACTERS:
			 switch (tileRank)
			 {
			   case ONE: return tileImages.a1();
			   case TWO: return tileImages.a2();
			   case THREE: return tileImages.a3();
			   case FOUR: return tileImages.a4();
			   case FIVE: return tileImages.a5();
			   case SIX: return tileImages.a6();
			   case SEVEN: return tileImages.a7();
			   case EIGHT: return tileImages.a8();
			   case NINE: return tileImages.a9();
			   default: throw new RuntimeException("Forgot rank=" + tileRank);
			 }

		 case BAMBOOS:
			 switch (tileRank)
			 {
			   case ONE: return tileImages.b1();
			   case TWO: return tileImages.b2();
			   case THREE: return tileImages.b3();
			   case FOUR: return tileImages.b4();
			   case FIVE: return tileImages.b5();
			   case SIX: return tileImages.b6();
			   case SEVEN: return tileImages.b7();
			   case EIGHT: return tileImages.b8();
			   case NINE: return tileImages.b9();
			   default: throw new RuntimeException("Forgot rank=" + tileRank);
			 }
		 case CIRCLES:
			 switch (tileRank)
			 {
			   case ONE: return tileImages.c1();
			   case TWO: return tileImages.c2();
			   case THREE: return tileImages.c3();
			   case FOUR: return tileImages.c4();
			   case FIVE: return tileImages.c5();
			   case SIX: return tileImages.c6();
			   case SEVEN: return tileImages.c7();
			   case EIGHT: return tileImages.c8();
			   case NINE: return tileImages.c9();
			   default: throw new RuntimeException("Forgot rank=" + tileRank);
			 }
		 case EAST:
			 return tileImages.e0();
		 case WEST:
			 return tileImages.w0();
		 case SOUTH:
			 return tileImages.s0();
		 case NORTH:
			 return tileImages.n0();
		 case RED:
			 return tileImages.r0();
		 case GREEN:
			 return tileImages.g0();
		 case DWHITE:
			 return tileImages.d0();
		 default: throw new RuntimeException("Forgot tile=" + tileSuit); 
		}
	  }  
	  
}
