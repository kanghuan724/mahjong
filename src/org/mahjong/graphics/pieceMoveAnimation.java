package org.mahjong.graphics;


import org.mahjong.client.MahJongPresenter;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class pieceMoveAnimation extends Animation{
	AbsolutePanel panel;
	FlowPanel imageContainer;
    Image start,end,moving;
    ImageResource context;
    int startX, startY;
    int endX, endY;
    MahJongPresenter presenter;
    TileImage tileimage;
    //Audio soundAtEnd;
    boolean cancelled;
    public pieceMoveAnimation(Image image,MahJongPresenter presenter,TileImage tileimage,int startX,int startY,int endX,int endY,
            ImageResource context,AbsolutePanel canvas) {
    this.context = context;
    panel = canvas;
    this.presenter = presenter;
    this.tileimage = tileimage;
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    start = image;
    Image moving = new Image(context);
    imageContainer = new FlowPanel();
    imageContainer.add(moving);
    imageContainer.setStyleName("imgContainer");
    panel.add(imageContainer,startX,startY);
    start.removeFromParent();
   }
    @Override
    protected void onUpdate(double progress) {
            int x = (int) (startX + (endX - startX) * progress);
            int y = (int) (startY + (endY - startY) * progress);
            double scale = 1 + 0.5 * Math.sin(progress * Math.PI);
            panel.remove(imageContainer);
            moving = new Image(context);
            imageContainer.add(moving);
            imageContainer.setStyleName("imgContainer");
            panel.add(imageContainer,x,y);
    }
    @Override
    protected void onComplete() {
    	panel.remove(imageContainer);
    	presenter.tileSelected(tileimage.tile);
    }


}
