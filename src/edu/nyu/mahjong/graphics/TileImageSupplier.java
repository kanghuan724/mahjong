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
		        return getBackOfCardImage();
		 
		      case NORMAL:
		        return getCardImage(tileImage.tile);
		      default:
		        throw new RuntimeException("Forgot kind=" + tileImage.kind);
		    }
		  } 
	public ImageResource getBackOfCardImage() {
	    //return cardImages.b();
	  } 
	public ImageResource getCardImage(Tile tile) {
	    //return cardImages.b();
	  } 
	  
}
