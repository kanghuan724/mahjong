package org.mahjong.graphics;

import java.util.Collections;
import java.util.List;

import org.mahjong.client.*;
import org.mahjong.client.MahJongPresenter.MahJongMessage;
import org.mahjong.client.MahJongPresenter.View;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragEvent;
import com.google.gwt.event.dom.client.DragHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.media.client.Audio;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.RootPanel;
/**
 * Graphics for the game of mahjong.
 */
public class MahJongGraphics extends Composite implements MahJongPresenter.View {
  public interface MahJongGraphicsUiBinder extends UiBinder<Widget, MahJongGraphics> {
  }
  
  @UiField
  AbsolutePanel animation;
 
  @UiField
  HorizontalPanel acrossDeclaredArea;
  @UiField
  HorizontalPanel acrossAtHandArea;
  @UiField
  HorizontalPanel usedArea;
  @UiField
  HorizontalPanel myAtHandArea;
  @UiField
  HorizontalPanel selectedArea;
  @UiField
  HorizontalPanel myDeclaredArea;
  @UiField
  VerticalPanel leftAtHandArea;
  @UiField
  VerticalPanel leftDeclaredArea;
  @UiField
  VerticalPanel rightDeclaredArea;
  @UiField
  VerticalPanel rightAtHandArea;
  @UiField
  Button claimBtn;
  private boolean enableClicks = false;
  
  private final TileImageSupplier tileImageSupplier;
  private MahJongPresenter presenter;
  private Audio pieceDown;
  private Audio pieceCaptured;
  private GameSounds gameSounds;
  private PickupDragController dragController;
  private String systemTime;
  private SoundController soundController;
  
  
  public MahJongGraphics() {
    TileImages tileImages = GWT.create(TileImages.class);
     gameSounds = GWT.create(GameSounds.class);
    this.tileImageSupplier = new TileImageSupplier(tileImages);
    MahJongGraphicsUiBinder uiBinder = GWT.create(MahJongGraphicsUiBinder.class);
    initWidget(uiBinder.createAndBindUi(this));
    systemTime = String.valueOf(System.currentTimeMillis());
    soundController = new SoundController();
 
  }

  private List<Image> createHorizonBackTiles(int numOfTiles) {
    List<TileImage> images = Lists.newArrayList();
    for (int i = 0; i < numOfTiles; i++) {
      images.add(TileImage.Factory.getBackOfTileImage("HORIZONTAL"));
    }
    return createImages(images, false);
  }
  
  private List<Image> createVerticalBackTiles(int numOfTiles,boolean withClick) {
	  List<TileImage> images = Lists.newArrayList();
	  for (int i = 0; i < numOfTiles; i++) {
		  images.add(TileImage.Factory.getBackOfTileImage("VERTICAL"));
	  }
	  if (withClick==false)
	   return createImages(images, false);
	  else
		  return createImageForRight(images);
  }

  private List<Image> createHorizonTileImages(List<Tile> tiles, boolean withClick) {
    List<TileImage> images = Lists.newArrayList();
    for (Tile tile : tiles) {
      images.add(TileImage.Factory.getTileImage("HORIZONTAL", tile));
    }
    return createImages(images, withClick);
  }

  private List<Image> createLeftTileImages(List<Tile> tiles, boolean withClick) {
	  List<TileImage> images = Lists.newArrayList();
	  for (Tile tile : tiles) {
	      images.add(TileImage.Factory.getTileImage("LEFT", tile));
	  }
	  return createImages(images, withClick);
  }

  private List<Image> createRightTileImages(List<Tile> tiles, boolean withClick) {
	  List<TileImage> images = Lists.newArrayList();
	  for (Tile tile : tiles) {
	      images.add(TileImage.Factory.getTileImage("RIGHT", tile));
	  }
	  return createImages(images,withClick);
  }

  private List<Image> createImageForRight(List<TileImage> images)
  {
	  List<Image> res = Lists.newArrayList();
	    int count=0;
	    for (TileImage img : images) {
	    	final TileImage imgFinal = img;
	    	Image image = new Image(tileImageSupplier.getResource(img));
	    	if (count==images.size()-1)
	    	{
	    		 image.addDragOverHandler(new DragOverHandler() {
	    	      	    @Override
	    	      	    public void onDragOver(DragOverEvent event) {
	    	      	    
	    	      	    }
	    	      	});
	    	        image.addDropHandler(new DropHandler() {
	    	      	    @Override
	    	      	    public void onDrop(DropEvent event) {
	    	      	        // prevent the native text drop
	    	      	        event.preventDefault();
	    	      	        
	    	      	        // get the data out of the event
	    	      	        String data = event.getData("index");
	    	      	      presenter.tileSwitch(Integer.parseInt(data));
	    	      	    }
	    	      	});
	    	}
	    		
	        count++;
	        res.add(image);
	    }
	    return res;
  }
  public void setTime(String time)
  {
	  this.systemTime = time;
  }
  private List<Image> createImages(List<TileImage> images, boolean withClick) {
    List<Image> res = Lists.newArrayList();
    int count=0;
    dragController = new PickupDragController(RootPanel.get(), false);
    dragController.setBehaviorDragStartSensitivity(1);
    for (TileImage img : images) {
      final TileImage imgFinal = img;
      final ImageResource temp = tileImageSupplier.getResource(img);
      final Image image = new DropImage(tileImageSupplier.getResource(img),count);
      if (withClick) {
        image.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            long currentTime = System.currentTimeMillis();
            long lastDropTime = Long.parseLong(systemTime);
            
            if (enableClicks&&currentTime-lastDropTime>1000) {
              int startX,startY,endX,endY;
              boolean tilePosition = presenter.tilePosition(imgFinal.tile);
              if (tilePosition == true)
              {
              startX = image.getAbsoluteLeft();
              startY = image.getAbsoluteTop();
              endX = selectedArea.getAbsoluteLeft();
              endY = selectedArea.getAbsoluteTop();
              }
              else
              {
            	  startX = image.getAbsoluteLeft();
            	  startY = image.getAbsoluteTop();
            	  endX = myAtHandArea.getAbsoluteLeft();
            	  endY = myAtHandArea.getAbsoluteTop();
              }
             
              ImageResource context = temp;
             
              pieceMoveAnimation anime = new pieceMoveAnimation(image,presenter,imgFinal,startX,startY,endX,endY,context,animation);
              anime.run(1500);
              Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_WAV_PCM,
                  "http://3-dot-huan-kang.appspot.com/pieceCaptured.wav");
              sound.play();
            //  if (Audio.isSupported()) {
                 // pieceDown = Audio.createIfSupported();
                 // pieceDown.addSource(gameSounds.pieceDownMp3().getSafeUri()
                 //                 .asString(), AudioElement.TYPE_MP3);
                 // pieceDown.play();
         // }
         
              
            }
          }
        });
        
        dragController.makeDraggable(image);
        DropController dropController = new TileDropController(image,presenter,this);
        dragController.registerDropController(dropController);
        /*image.addDragStartHandler(new DragStartHandler() {
            @Override
            public void onDragStart(com.google.gwt.event.dom.client.DragStartEvent event)  {
             
                String a = String.valueOf(((DropImage)image).getIndex());
                System.out.println("Drag Start"+a);
                event.setData("index", a);
                pieceCaptured = Audio.createIfSupported();
                if (Audio.isSupported()) {
                pieceCaptured.addSource(gameSounds.pieceCapturedMp3().getSafeUri()
                                .asString(), AudioElement.TYPE_MP3);
                pieceCaptured.addSource(gameSounds.pieceCapturedWav().getSafeUri()
                                .asString(), AudioElement.TYPE_WAV);
                pieceCaptured.play();
                }
            } 		
          });
        image.addDragOverHandler(new DragOverHandler() {
      	    @Override
      	    public void onDragOver(DragOverEvent event) {
      	    	image.setStyleName("imgBigger");
      	    	
      	        
      	    }
      	});
        image.addDragLeaveHandler(new DragLeaveHandler() {
      	    @Override
      	    public void onDragLeave(DragLeaveEvent event) {
      	    	image.setStyleName("imgContainer");
      	    	
      	        
      	    }
      	});
        image.addDropHandler(new DropHandler() {
      	    @Override
      	    public void onDrop(DropEvent event) {
      	        // prevent the native text drop
      	        event.preventDefault();
      	        
      	        // get the data out of the event
      	        String data = event.getData("index");
      	        String currentData = String.valueOf(((DropImage)image).getIndex());
      	    
      	      if (Audio.isSupported()) {
                  pieceDown = Audio.createIfSupported();
                  pieceDown.addSource(gameSounds.pieceDownMp3().getSafeUri()
                                  .asString(), AudioElement.TYPE_MP3);
                  pieceDown.addSource(gameSounds.pieceDownWav().getSafeUri()
                                  .asString(), AudioElement.TYPE_WAV);
                  pieceDown.play();
          }
      	      presenter.tileSwitch(Integer.parseInt(data),Integer.parseInt(currentData));
      	    }
      	});*/
      }
      count++;
      res.add(image);
    }
    return res;
  }

  private void placeHorizonImages(HorizontalPanel panel, List<Image> images) {
    panel.clear();
   // Image last = images.isEmpty() ? null : images.get(images.size() - 1);
    for (Image image : images) {
      FlowPanel imageContainer = new FlowPanel();
      imageContainer.add(image);
      //imageContainer.setStyleName(image != last ? "imgShortContainer" : "imgContainer");
      imageContainer.setStyleName("imgContainer");
      
      
      panel.add(imageContainer);
    }
  }
  
  private void placeVerticalImages(VerticalPanel panel, List<Image> images) {
	// TODO: The void to placeVerticalImages for the vertical panels 
	// (AtHand & Declared) for left and right players
	  panel.clear();
	 // Image last = images.isEmpty() ? null : images.get(images.size() - 1);
	  for (Image image : images) {
	      FlowPanel imageContainer = new FlowPanel();
	      //imageContainer.setStyleName(image != last ? "imgShortContainer" : "imgContainer");
	      imageContainer.setStyleName("imgVerticalContainer");
	      imageContainer.add(image);
	      panel.add(imageContainer);
	    }
  }

  private void alertMahJongMessage(MahJongMessage mahjongMessage)
  {
	  String message="";
	  List<String> options=Lists.newArrayList();
	  /*
	   Discard is not included in mahjongMessage.
	   mahjongmessage consists of pick, hu, gang, peng, chi and invisible
	   */
	   
	  switch (mahjongMessage) {
      case PICK:
        message += "Your Turn To Pick Up A Tile";
        break;
      case HU:
        message += "Are You Able To Hu?";
        if (presenter.huHelper()==true)
          options.add("Yes,Let me Hu");
        options.add("No,not now");
        break;
      case GANG:
    	message += "Wanna Gang That Tile?";
    	List<Integer> comboToGang=presenter.gangHelper();
	      if (comboToGang.size()==4)
    	options.add("Yes, god gang that");
    	options.add("No, not now");
    	break;
      case PENG:
    	message += "Wanna Peng That Tile?";
    	List<Integer> comboToPeng=presenter.pengHelper();
	      if (comboToPeng.size()==3)
    	options.add("Yes, god peng that");
    	options.add("No, not now");
    	break;
      case CHI:
    	message += "Wanna Chi That Tile?";
    	List<Integer> comboToChi=presenter.chiHelper();
    	if (comboToChi.size()==3)
    	  options.add("Yes, god chi that");
        options.add("No, not now");
    	break;
      case INVISIBLE:
        break;
      
      default:
        break;
    }
	if (mahjongMessage==MahJongMessage.PICK) {
	      options.add("OK");
	    }
	if (message.equals("")==false)
	{
	  PopupChoices.OptionChosen eventTriggered=eventFactory.build(presenter, mahjongMessage);
	  new PopupChoices(message, options,eventTriggered).show();
	 
	}
	   
  }
  
  

  private void disableClicks() {
    claimBtn.setEnabled(false);
    enableClicks = false;
   // enableClicks = true;
  }

  @UiHandler("claimBtn")
  void onClickClaimBtn(ClickEvent e) {
    disableClicks();
    presenter.finishedSelectingTiles();
  }

  @Override
  public void setPresenter(MahJongPresenter mahJongPresenter) {
    this.presenter = mahJongPresenter;
  }

  @Override
  public void setViewerState(int numberOfTilesAtHand1, int numberOfTilesAtHand2, 
  		int numberOfTilesAtHand3, int numberOfTilesAtHand4,
  		List<Tile> tilesAtDeclared1, List<Tile> tilesAtDeclared2,
  		List<Tile> tilesAtDeclared3, List<Tile> tilesAtDeclared4,
  		int numberOfTilesAtWall, List<Tile> tilesUsed,
  		MahJongMessage mahJongMessage) {
    placeHorizonImages(myAtHandArea, createHorizonBackTiles(numberOfTilesAtHand1));
    placeVerticalImages(rightAtHandArea, createVerticalBackTiles(numberOfTilesAtHand2,false));
    placeHorizonImages(acrossAtHandArea, createHorizonBackTiles(numberOfTilesAtHand3));
    placeVerticalImages(leftAtHandArea, createVerticalBackTiles(numberOfTilesAtHand4,false));
    placeHorizonImages(myDeclaredArea, createHorizonTileImages(tilesAtDeclared1, false));
    placeVerticalImages(rightDeclaredArea, createLeftTileImages(tilesAtDeclared2, false));
    placeHorizonImages(acrossDeclaredArea, createHorizonTileImages(tilesAtDeclared3, false));
    placeVerticalImages(leftDeclaredArea, createRightTileImages(tilesAtDeclared4, false));
    placeHorizonImages(usedArea, createHorizonTileImages(tilesUsed, false));    
    placeHorizonImages(selectedArea, ImmutableList.<Image>of());
    alertMahJongMessage(mahJongMessage);
    //disableClicks();
  }

  @Override
  public void setPlayerState(int numberOfTilesAtHandLeft, int numberOfTilesAtHandRight, 
  		int numberOfTilesAtHandAcross,
  		List<Tile> tilesAtDeclaredLeft, List<Tile> tilesAtDeclaredRight, 
  		List<Tile> tilesAtDeclaredAcross,
  		int numberOfTilesAtWall, List<Tile> tilesUsed,
          List<Tile> myTilesAtHand, List<Tile> myTilesDeclared,
          MahJongMessage mahJongMessage) {
    //Collections.sort(myTilesAtHand);
    placeVerticalImages(leftAtHandArea, createVerticalBackTiles(numberOfTilesAtHandLeft,false));
    placeHorizonImages(acrossAtHandArea, createHorizonBackTiles(numberOfTilesAtHandAcross));
    placeVerticalImages(rightAtHandArea, createVerticalBackTiles(numberOfTilesAtHandRight,true));
    placeVerticalImages(leftDeclaredArea, createLeftTileImages(tilesAtDeclaredLeft, false));
    placeHorizonImages(acrossDeclaredArea, createHorizonTileImages(tilesAtDeclaredAcross, false));
    placeVerticalImages(rightDeclaredArea, createRightTileImages(tilesAtDeclaredRight, false));
    placeHorizonImages(usedArea, createHorizonTileImages(tilesUsed, false));    
    placeHorizonImages(selectedArea, ImmutableList.<Image>of());
    placeHorizonImages(myAtHandArea, createHorizonTileImages(myTilesAtHand, false));
    placeHorizonImages(myDeclaredArea, createHorizonTileImages(myTilesDeclared, false));
    alertMahJongMessage(mahJongMessage);
    disableClicks();
  }

  @Override
  public void chooseTile(List<Tile> selectedTiles, List<Tile> remainingTiles) {
    //Collections.sort(remainingTiles);
    Collections.sort(selectedTiles);
    enableClicks = true;
    claimBtn.setEnabled(!selectedTiles.isEmpty());
    placeHorizonImages(myAtHandArea, createHorizonTileImages(remainingTiles, true));
    placeHorizonImages(selectedArea, createHorizonTileImages(ImmutableList.<Tile>copyOf(selectedTiles), true));
    //TODO: design the way to discard the selected tile
  }
  @Override
  public void chooseTileToChi(List<Tile> selectedTiles, List<Tile> remainingTiles) {
   // Collections.sort(remainingTiles);
    Collections.sort(selectedTiles);
    enableClicks = true;
    claimBtn.setEnabled(selectedTiles.size()==2);
    placeHorizonImages(myAtHandArea, createHorizonTileImages(remainingTiles, true));
    placeHorizonImages(selectedArea, createHorizonTileImages(ImmutableList.<Tile>copyOf(selectedTiles), false));
    //TODO: design the way to discard the selected tile
  }
@Override
public void huAvailable(Tile tileToHu, List<Tile> myTilesAtHand) {
	// TODO Auto-generated method stub
	
}
@Override
public void gangAvailable(Tile tileToGang, List<Tile> tilesToGang) {
	// TODO Auto-generated method stub
	
}
@Override
public void pengAvailable(Tile tileToPeng, List<Tile> tilesToPeng) {
	// TODO Auto-generated method stub
	
}
@Override
public void chiAvailable(Tile tileToChi, List<Tile> tilesToChi) {
	// TODO Auto-generated method stub
	
}
@Override
public void discard() {
	// TODO Auto-generated method stub
	
}
  
}
