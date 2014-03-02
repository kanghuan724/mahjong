package edu.nyu.mahjong.graphics;




import edu.nyu.mahjong.logic.Tile;

import com.google.gwt.resources.client.ImageResource;


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
		return tileImages.a1();
	  }  
	  
}
