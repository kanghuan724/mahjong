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
		        return getBackOfTileImage(tileImage.direction.toString());
		 
		      case NORMAL:
		        return getTileImage(tileImage.direction.toString(), tileImage.tile);
		      default:
		        throw new RuntimeException("Forgot kind=" + tileImage.kind + "_" + tileImage.direction);
		    }
		  } 
	public ImageResource getBackOfTileImage(String direction) {
		if (direction == "HORIZONTAL") {
			return tileImages.b();
		} else {
			return tileImages.b_v();
		}
	  } 
	public ImageResource getTileImage(String direction, Tile tile) {
		Suit tileSuit=tile.getSuit();
		Rank tileRank=tile.getRank();
		switch (direction)
		{
		case "HORIZONTAL":
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
				   default: throw new RuntimeException("Forgot rank=" + tileRank + "_" + direction);
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
				   default: throw new RuntimeException("Forgot rank=" + tileRank + "_" + direction);
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
				   default: throw new RuntimeException("Forgot rank=" + tileRank + "_" + direction);
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
			 default: throw new RuntimeException("Forgot tile=" + tileSuit + "_" + direction); 
			}
		case "LEFT":
			switch (tileSuit)
			{
			 case ACHARACTERS:
				 switch (tileRank)
				 {
				   case ONE: return tileImages.a1_l();
				   case TWO: return tileImages.a2_l();
				   case THREE: return tileImages.a3_l();
				   case FOUR: return tileImages.a4_l();
				   case FIVE: return tileImages.a5_l();
				   case SIX: return tileImages.a6_l();
				   case SEVEN: return tileImages.a7_l();
				   case EIGHT: return tileImages.a8_l();
				   case NINE: return tileImages.a9_l();
				   default: throw new RuntimeException("Forgot rank=" + tileRank + "_" + direction);
				 }

			 case BAMBOOS:
				 switch (tileRank)
				 {
				   case ONE: return tileImages.b1_l();
				   case TWO: return tileImages.b2_l();
				   case THREE: return tileImages.b3_l();
				   case FOUR: return tileImages.b4_l();
				   case FIVE: return tileImages.b5_l();
				   case SIX: return tileImages.b6_l();
				   case SEVEN: return tileImages.b7_l();
				   case EIGHT: return tileImages.b8_l();
				   case NINE: return tileImages.b9_l();
				   default: throw new RuntimeException("Forgot rank=" + tileRank + "_" + direction);
				 }
			 case CIRCLES:
				 switch (tileRank)
				 {
				   case ONE: return tileImages.c1_l();
				   case TWO: return tileImages.c2_l();
				   case THREE: return tileImages.c3_l();
				   case FOUR: return tileImages.c4_l();
				   case FIVE: return tileImages.c5_l();
				   case SIX: return tileImages.c6_l();
				   case SEVEN: return tileImages.c7_l();
				   case EIGHT: return tileImages.c8_l();
				   case NINE: return tileImages.c9_l();
				   default: throw new RuntimeException("Forgot rank=" + tileRank + "_" + direction);
				 }
			 case EAST:
				 return tileImages.e0_l();
			 case WEST:
				 return tileImages.w0_l();
			 case SOUTH:
				 return tileImages.s0_l();
			 case NORTH:
				 return tileImages.n0_l();
			 case RED:
				 return tileImages.r0_l();
			 case GREEN:
				 return tileImages.g0_l();
			 case DWHITE:
				 return tileImages.d0_l();
			 default: throw new RuntimeException("Forgot tile=" + tileSuit + "_" + direction); 
			}
		  default:
				switch (tileSuit)
				{
				 case ACHARACTERS:
					 switch (tileRank)
					 {
					   case ONE: return tileImages.a1_r();
					   case TWO: return tileImages.a2_r();
					   case THREE: return tileImages.a3_r();
					   case FOUR: return tileImages.a4_r();
					   case FIVE: return tileImages.a5_r();
					   case SIX: return tileImages.a6_r();
					   case SEVEN: return tileImages.a7_r();
					   case EIGHT: return tileImages.a8_r();
					   case NINE: return tileImages.a9_r();
					   default: throw new RuntimeException("Forgot rank=" + tileRank + "_" + direction);
					 }

				 case BAMBOOS:
					 switch (tileRank)
					 {
					   case ONE: return tileImages.b1_r();
					   case TWO: return tileImages.b2_r();
					   case THREE: return tileImages.b3_r();
					   case FOUR: return tileImages.b4_r();
					   case FIVE: return tileImages.b5_r();
					   case SIX: return tileImages.b6_r();
					   case SEVEN: return tileImages.b7_r();
					   case EIGHT: return tileImages.b8_r();
					   case NINE: return tileImages.b9_r();
					   default: throw new RuntimeException("Forgot rank=" + tileRank + "_" + direction);
					 }
				 case CIRCLES:
					 switch (tileRank)
					 {
					   case ONE: return tileImages.c1_r();
					   case TWO: return tileImages.c2_r();
					   case THREE: return tileImages.c3_r();
					   case FOUR: return tileImages.c4_r();
					   case FIVE: return tileImages.c5_r();
					   case SIX: return tileImages.c6_r();
					   case SEVEN: return tileImages.c7_r();
					   case EIGHT: return tileImages.c8_r();
					   case NINE: return tileImages.c9_r();
					   default: throw new RuntimeException("Forgot rank=" + tileRank + "_" + direction);
					 }
				 case EAST:
					 return tileImages.e0_r();
				 case WEST:
					 return tileImages.w0_r();
				 case SOUTH:
					 return tileImages.s0_r();
				 case NORTH:
					 return tileImages.n0_r();
				 case RED:
					 return tileImages.r0_r();
				 case GREEN:
					 return tileImages.g0_r();
				 case DWHITE:
					 return tileImages.d0_r();
				 default: throw new RuntimeException("Forgot tile=" + tileSuit + "_" + direction); 
				}
		}
	}
}
	  

