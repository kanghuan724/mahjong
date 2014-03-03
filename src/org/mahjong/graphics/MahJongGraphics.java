package org.mahjong.graphics;

import java.util.Collections;
import java.util.List;

import org.mahjong.client.*;
import org.mahjong.client.MahJongPresenter.MahJongMessage;
import org.mahjong.client.MahJongPresenter.View;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Graphics for the game of mahjong.
 */
public class MahJongGraphics extends Composite implements MahJongPresenter.View {
  public interface MahJongGraphicsUiBinder extends UiBinder<Widget, MahJongGraphics> {
  }

  // TODO: There are 4 more areas: leftDeclaredArea, leftAtHandArea, rightDeclaredArea, rightAtHandArea, 
  // TODO: Buttons to be designed
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
  Button claimBtn;
  private boolean enableClicks = false;
  
  // TODO: Auxiliary classes: TileImageSupplier, TileImages, TileImage
  private final TileImageSupplier tileImageSupplier;
  private MahJongPresenter presenter;

  public MahJongGraphics() {
    TileImages tileImages = GWT.create(TileImages.class);
    this.tileImageSupplier = new TileImageSupplier(tileImages);
    MahJongGraphicsUiBinder uiBinder = GWT.create(MahJongGraphicsUiBinder.class);
    initWidget(uiBinder.createAndBindUi(this));
  }

  private List<Image> createHorizonBackTiles(int numOfTiles) {
    List<TileImage> images = Lists.newArrayList();
    for (int i = 0; i < numOfTiles; i++) {
      images.add(TileImage.Factory.getBackOfTileImage());
    }
    return createImages(images, false);
  }
  
  private List<Image> createVerticalBackTiles(int numOfTiles) {
	  List<TileImage> images = Lists.newArrayList();
	//TODO: The logic to createVerticalBackTiles for the vertical panels for left and right players 
	  return createImages(images, false);
  }

  private List<Image> createHorizonTileImages(List<Tile> tiles, boolean withClick) {
    List<TileImage> images = Lists.newArrayList();
    for (Tile tile : tiles) {
      images.add(TileImage.Factory.getTileImage(tile));
    }
    return createImages(images, withClick);
  }

  private List<Image> createVerticalTileImages(List<Tile> tiles, boolean withClick) {
	  List<TileImage> images = Lists.newArrayList();
	//TODO: The void to createVerticalTileImages for the vertical panels for left and right players
	  return createImages(images, withClick);
  }


  
  private List<Image> createImages(List<TileImage> images, boolean withClick) {
    List<Image> res = Lists.newArrayList();
    for (TileImage img : images) {
      final TileImage imgFinal = img;
      Image image = new Image(tileImageSupplier.getResource(img));
      if (withClick) {
        image.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            if (enableClicks) {
              presenter.tileSelected(imgFinal.tile);
            }
          }
        });
      }
      res.add(image);
    }
    return res;
  }

  private void placeHorizonImages(HorizontalPanel panel, List<Image> images) {
    panel.clear();
   // Image last = images.isEmpty() ? null : images.get(images.size() - 1);
    for (Image image : images) {
      FlowPanel imageContainer = new FlowPanel();
      //imageContainer.setStyleName(image != last ? "imgShortContainer" : "imgContainer");
      imageContainer.setStyleName("imgContainer");
      imageContainer.add(image);
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
	      imageContainer.setStyleName("imgContainer");
	      imageContainer.add(image);
	      panel.add(imageContainer);
	    }
  }
  
  //TODO: Main logic of MahJongMessage
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
    	options.add("Yes, god chi that");
        options.add("No, not now");
    	break;
      case INVISIBLE:
        break;
      default:
        break;
    }
	if (options.isEmpty()) {
	      options.add("OK");
	    }
	PopupChoices.OptionChosen eventTriggered=eventFactory.build(presenter, mahjongMessage);
	new PopupChoices(message, options,
	        eventTriggered).center();
	   
  }
  
  

  private void disableClicks() {
    claimBtn.setEnabled(false);
    enableClicks = false;
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
    placeVerticalImages(leftAtHandArea, createVerticalBackTiles(numberOfTilesAtHand2));
    placeHorizonImages(acrossAtHandArea, createHorizonBackTiles(numberOfTilesAtHand3));
    placeVerticalImages(rightAtHandArea, createVerticalBackTiles(numberOfTilesAtHand4));
    placeHorizonImages(myDeclaredArea, createHorizonTileImages(tilesAtDeclared1, false));
    placeVerticalImages(leftDeclaredArea, createVerticalTileImages(tilesAtDeclared2, false));
    placeHorizonImages(acrossDeclaredArea, createHorizonTileImages(tilesAtDeclared3, false));
    placeVerticalImages(rightDeclaredArea, createVerticalTileImages(tilesAtDeclared4, false));
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
    Collections.sort(myTilesAtHand);
    placeVerticalImages(leftAtHandArea, createVerticalBackTiles(numberOfTilesAtHandLeft));
    placeHorizonImages(acrossAtHandArea, createHorizonBackTiles(numberOfTilesAtHandAcross));
    placeVerticalImages(rightAtHandArea, createVerticalBackTiles(numberOfTilesAtHandRight));
    placeVerticalImages(leftDeclaredArea, createVerticalTileImages(tilesAtDeclaredLeft, false));
    placeHorizonImages(acrossDeclaredArea, createHorizonTileImages(tilesAtDeclaredAcross, false));
    placeVerticalImages(rightDeclaredArea, createVerticalTileImages(tilesAtDeclaredRight, false));
    placeHorizonImages(usedArea, createHorizonTileImages(tilesUsed, false));    
    placeHorizonImages(selectedArea, ImmutableList.<Image>of());
    placeHorizonImages(myAtHandArea, createHorizonTileImages(myTilesAtHand, false));
    placeHorizonImages(myDeclaredArea, createHorizonTileImages(myTilesDeclared, false));
    alertMahJongMessage(mahJongMessage);
    disableClicks();
  }

  @Override
  public void chooseTile(List<Tile> selectedTiles, List<Tile> remainingTiles) {
    Collections.sort(remainingTiles);
    Collections.sort(selectedTiles);
    enableClicks = true;
    claimBtn.setEnabled(!selectedTiles.isEmpty());
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
