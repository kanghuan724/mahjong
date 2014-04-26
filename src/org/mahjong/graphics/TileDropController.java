package org.mahjong.graphics;

import org.mahjong.client.MahJongPresenter;

import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.Widget;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.Image;

public class TileDropController extends SimpleDropController{
	
	private Image image;
	private MahJongPresenter presenter;
	private MahJongGraphics graphic;
	public TileDropController(Image image,MahJongPresenter presenter,MahJongGraphics graphic) {
	    super(image);
	    this.image = image;
	    this.presenter = presenter;
	    this.graphic = graphic;
	  }
	 @Override
	 public void onEnter(DragContext context) {
	 //   super.onEnter(context);
	    image.setStyleName("imgBigger");
	 }
      @Override
	  public void onLeave(DragContext context) {
    	image.setStyleName("imgContainer");
    	
	 //   super.onLeave(context);
	  }

      @Override
      public void onDrop(DragContext context) {
	        
	        
	        //String data = context.getData("index");
    	   // String data = context.toString();
    	  String data = context.draggable.toString();
    	  String currentData = String.valueOf(((DropImage)image).getIndex());
    	  super.onDrop(context);    	  
	      presenter.tileSwitch(Integer.parseInt(data),Integer.parseInt(currentData));
	      String currentTime = String.valueOf(System.currentTimeMillis());
	      graphic.setTime(currentTime);
         // super.onDrop(context);
      }

}
