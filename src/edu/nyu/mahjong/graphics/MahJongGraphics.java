package edu.nyu.mahjong.graphics;

import java.util.Collections;
import java.util.List;

import edu.nyu.mahjong.logic.*;
import edu.nyu.mahjong.logic.MahJongPresenter.MahJongMessage;
import edu.nyu.mahjong.logic.MahJongPresenter.View;

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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Graphics for the game of mahjong.
 */
public class MahJongGraphics extends Composite implements MahJongPresenter.View {
  public interface CheatGraphicsUiBinder extends UiBinder<Widget, CheatGraphics> {
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
  HorizontalPanel seletedArea;
  @UiField
  HorizontalPanel myDeclaredArea;
  @UiField
  //Button claimBtn;
  //private boolean enableClicks = false;
  
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

  private List<Image> createHorizonTileImages(List<Tile> cards, boolean withClick) {
    List<TileImage> images = Lists.newArrayList();
    for (Tile tile : tiles) {
      images.add(TileImage.Factory.getTileImage(tile));
    }
    return createImages(images, withClick);
  }

  private List<Image> createVerticalTileImages(List<Tile> cards, boolean withClick) {
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
    Image last = images.isEmpty() ? null : images.get(images.size() - 1);
    for (Image image : images) {
      FlowPanel imageContainer = new FlowPanel();
      imageContainer.setStyleName(image != last ? "imgShortContainer" : "imgContainer");
      imageContainer.add(image);
      panel.add(imageContainer);
    }
  }
  
  private void placeVerticalImages() {
	// TODO: The void to placeVerticalImages for the vertical panels 
	// (AtHand & Declared) for left and right players
  }
  
  //TODO: Main logic of MahJongMessage
	  
  /*private void alertMahJongMessage(MahJongMessage mahJongMessage) {
    String message = "";
    List<String> options = Lists.newArrayList();
    final String callCheatOption = "Call cheater!";
    if (lastClaim.isPresent()) {
      Claim claim = lastClaim.get();
      message = "Dropped " + claim.getNumberOfCards()
          + " cards, and claimed they are of rank " + claim.getCardRank() + ". ";
    }
    switch (cheaterMessage) {
      case WAS_CHEATING:
        message += "The player was cheating.";
        break;
      case WAS_NOT_CHEATING:
        message += "The player was NOT cheating.";
        break;
      case IS_OPPONENT_CHEATING:
        message += "Did the opponent cheat?";
        options.add("Probably told the truth");
        options.add(callCheatOption);
        break;
      case INVISIBLE:
        break;
      default:
        break;
    }
    if (message.isEmpty()) {
      return;
    }
    if (options.isEmpty()) {
      options.add("OK");
    }
    new PopupChoices(message, options,
        new PopupChoices.OptionChosen() {
      @Override
      public void optionChosen(String option) {
        if (option.equals(callCheatOption)) {
          presenter.declaredCheater();
        }
      }
    }).center();
  }*/

  //private void disableClicks() {
  //  claimBtn.setEnabled(false);
  //  enableClicks = false;
  //}

  //@UiHandler("claimBtn")
  //void onClickClaimBtn(ClickEvent e) {
  //  disableClicks();
  //  presenter.finishedSelectingCards();
  //}

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
    placeImages(usedArea, createHorizonTileImages(tilesUsed, false));    
    placeImages(selectedArea, ImmutableList.<Image>of());
    alertMahJongMessage(mahJongMessage);
    //disableClicks();
  }

  @Override
  public void setPlayerState(int numberOfTilesAtHandLeft, int numberOfTilesAtHandRight, 
  		int numberOfTilesAtHandAcross,
  		List<Tile> tilesAtDeclaredLeft, List<Tile> tilesAtDeclaredRight, 
  		List<Tile> tilesAtDeclaredAcorss,
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
    placeImages(usedArea, createHorizonTileImages(tilesUsed, false));    
    placeImages(selectedArea, ImmutableList.<Image>of());
    alertMahJongMessage(mahJongMessage);
    //disableClicks();
  }

  @Override
  public void chooseTile(Tile selectedCard, List<Tile> remainingTiles) {
    Collections.sort(remainingTiles);
    enableClicks = true;
    placeImages(myAtArea, createHorizonTileImages(remainingTiles, true));
    placeImages(selectedArea, createHorizonTileImages(ImmutableList.<Tile>of(selectedTile), true));
    //TODO: design the way to discard the selected tile
  }

  //TODO: hu/gang/peng/chiAvailable
}
