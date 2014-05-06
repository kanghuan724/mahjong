package org.mahjong.graphics;

import org.mahjong.client.MahJongPresenter;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.Image;

public class TileDropControllerForRight extends SimpleDropController{
	private Image image;
	private MahJongPresenter presenter;
	private MahJongGraphics graphic;
	public TileDropControllerForRight(Image image,MahJongPresenter presenter,MahJongGraphics graphic) {
	    super(image);
	    this.image = image;
	    this.presenter = presenter;
	    this.graphic = graphic;
	  }
	 @Override
	 public void onEnter(DragContext context) {
		 
	  
	 }
      @Override
	  public void onLeave(DragContext context) {
    	
    	 
	 
	  }

      @Override
      public void onDrop(DragContext context) {
	        
	        
	        //String data = context.getData("index");
    	   // String data = context.toString();
    	  System.out.println(" drop on me");
    	  String data = context.draggable.toString();
    	 
    	  super.onDrop(context);    	  
    	  presenter.tileSwitch(Integer.parseInt(data));
	      String currentTime = String.valueOf(System.currentTimeMillis());
	      graphic.setTime(currentTime);
         // super.onDrop(context);
      }
}




