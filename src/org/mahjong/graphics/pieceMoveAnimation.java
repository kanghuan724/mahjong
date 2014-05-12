package org.mahjong.graphics;


import org.mahjong.client.MahJongPresenter;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.media.client.Audio;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class pieceMoveAnimation extends Animation{

	//private final String audioAdress ="https://nyu-gaming-course-2013.googlecode.com/svn/trunk/eclipse/src/org/simongellis/hw5/pieceCaptured.mp3";

	AbsolutePanel panel;
	FlowPanel imageContainer;
    Image start,end,moving;
    ImageResource context;
    int startX, startY;
    int endX, endY;
    double xScale;
    double yScale;
    MahJongPresenter presenter;
    TileImage tileimage;

   // Audio soundAtEnd;
    Sound sound;


    //Audio soundAtEnd;
    boolean cancelled;
    public pieceMoveAnimation(Image image,MahJongPresenter presenter,TileImage tileimage,int startX,int startY,int endX,int endY,
            ImageResource context,AbsolutePanel canvas,double xScale,double yScale) {
    this.context = context;
    panel = canvas;
    this.presenter = presenter;
    this.tileimage = tileimage;
    this.xScale = xScale;
    this.yScale = yScale;

    double xOriginalStart = (double)startX*xScale;
    double yOriginalStart = (double)startY*yScale;
    double xOriginalEnd = (double)endX*xScale;
    double yOriginalEnd = (double)endY*yScale;
    this.startX = (int)xOriginalStart;
    this.startY = (int)yOriginalStart;
    this.endX = (int)xOriginalEnd;
    this.endY = (int)yOriginalEnd;

    start = image;



    Image moving = new Image(context);
    imageContainer = new FlowPanel();
    imageContainer.add(moving);
    imageContainer.setStyleName("imgContainer");
    panel.add(imageContainer,startX,startY);
    start.removeFromParent();

    SoundController soundController = new SoundController();
    sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_PCM,

            "http://huan-kang.appspot.com/pieceCaptured.wav");




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
            //panel.add(imageContainer,x,y);
          //  double xOriginal = (double)x*xScale;
           // double yOriginal = (double)y*yScale;
            panel.add(imageContainer,x,y);
            
    }
    @Override
    protected void onComplete() {
    	panel.remove(imageContainer);

    	
    	sound.play();
    	  

    	presenter.tileSelected(tileimage.tile);
    }


}
