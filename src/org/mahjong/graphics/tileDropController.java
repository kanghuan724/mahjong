package org.mahjong.graphics;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Image;

public class tileDropController extends SimpleDropController{
	
	private Image image;
	 public tileDropController(Image image) {

		super(image); 
		this.image=image;
		
	}
	 
	@Override
	  public void onDrop(DragContext context) {
	    System.out.println("I am dropping");
	    super.onDrop(context);
	  }
}
