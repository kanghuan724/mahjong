package org.mahjong.graphics;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.mahjong.client.*;
import org.mahjong.client.MahJongPresenter.MahJongMessage;


import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;

import com.allen_sauer.gwt.voices.client.SoundController;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

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

import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.ui.client.dialog.Dialogs;
/**
 * Graphics for the game of mahjong.
 */
public class MahJongGraphics extends Composite implements MahJongPresenter.View {
  public interface MahJongGraphicsUiBinder extends UiBinder<Widget, MahJongGraphics> {
  }
   
  @UiField
  AbsolutePanel animation;
  @UiField
  AbsolutePanel Dialogue;

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
  @UiField
  Button claimHu;
  @UiField
  HorizontalPanel standard;
  
  private boolean enableClicks = false;
  
  private final TileImageSupplier tileImageSupplier;
  private MahJongPresenter presenter;

  private PickupDragController dragController;
  
  private SoundController soundController;
  private Dialogs dialogue;
  MahjongConstants constants;
  //Index is 0, switch
  //Index is 1, discard
  public int switchOrDiscard = 0;
  public int switchCardIndex = -1;
  public List<Integer> chiSelected = new ArrayList<Integer> ();
  public List<Integer> chiSequence = new ArrayList<Integer> ();

  public MahJongGraphics() {
    TileImages tileImages = GWT.create(TileImages.class);
    this.tileImageSupplier = new TileImageSupplier(tileImages);
    MahJongGraphicsUiBinder uiBinder = GWT.create(MahJongGraphicsUiBinder.class);
    initWidget(uiBinder.createAndBindUi(this));

    soundController = new SoundController();
    dialogue = new Dialogs();
    constants = (MahjongConstants) GWT.create(MahjongConstants.class);
 

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
  private List<Image> createHorizonTileImagesToChi(List<Tile> tiles, boolean withClick) {
	    List<TileImage> images = Lists.newArrayList();
	    for (Tile tile : tiles) {
	      images.add(TileImage.Factory.getTileImage("HORIZONTAL", tile));
	    }
	    return createImagesToChi(images, withClick);
	  }
  private List<Image> createHorizonTileImagesForHu(List<Tile> tiles, boolean withClick) {
	    List<TileImage> images = Lists.newArrayList();
	    for (Tile tile : tiles) {
	      images.add(TileImage.Factory.getTileImage("HORIZONTAL", tile));
	    }
	    return createImagesForHu(images, withClick);
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
	    
	    for (TileImage img : images) {
	         Image image = new Image(tileImageSupplier.getResource(img));
	    	
	    		
	        
	        res.add(image);
	    }
	    return res;
  }
  

  public static void console(String text)
  {
      System.out.println(text);
  }
  private List<Image> createImages(List<TileImage> images, boolean withClick) {
    List<Image> res = Lists.newArrayList();
    int count=0;  
    for (TileImage img : images) {
      final TileImage imgFinal = img;
      final ImageResource temp = tileImageSupplier.getResource(img);
      final Image image = new DropImage(tileImageSupplier.getResource(img),count);
      final int cardIndex = count;
      if (withClick) {
				if (switchOrDiscard == 1) {
					image.addClickHandler( new discardClick(animation,acrossDeclaredArea,
							  acrossAtHandArea,	myAtHandArea, selectedArea,
								 leftAtHandArea,leftDeclaredArea, enableClicks,
								  presenter,imgFinal,image,
								  temp ,soundController));
				}
				else
				{
					image.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
						   switchCard(cardIndex,image);
						}
						
					});
				}

      }
      count++;
      res.add(image);
    }
    return res;
  }
  private List<Image> createImagesToChi(List<TileImage> images, boolean withClick) {
	    List<Image> res = Lists.newArrayList();
	    int count=0;
	    for (TileImage img : images) {
	       Image image = new DropImage(tileImageSupplier.getResource(img),count);
	       int cardIndex = count;
		  image.addClickHandler(new chiClick(this,image,cardIndex,this.presenter));	  
	      count++;
	      res.add(image);
	    }
	    return res;
	  }
  private void switchCard(int count,Image image)
  {
	  System.out.println("index"+switchCardIndex);
	  if (switchCardIndex<0)
	  {
		  switchCardIndex = count;
		  image.setStyleName("imgBigger");
	  }
	  else
	  {
		  int switchFrom = switchCardIndex;
		  switchCardIndex = -1;
		  presenter.tileSwitch(switchFrom,count);
		  
	  }
  }
  private List<Image> createImagesForHu(List<TileImage> images, boolean withClick) {
	    List<Image> res = Lists.newArrayList();
	    int count=0;


	    for (TileImage img : images) {
	     
	      final Image image = new DropImage(tileImageSupplier.getResource(img),count);
			final int cardIndex = count;
			image.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
				   switchCard(cardIndex,image);
				}
				
			});
	      res.add(image);
	      count++;
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
  
  private void placeHorizonImagesForChi(HorizontalPanel panel, List<Image> images) {
	    panel.clear();
	   // Image last = images.isEmpty() ? null : images.get(images.size() - 1);
	    int count =0;
	    for (Image image : images) {
	      FlowPanel imageContainer = new FlowPanel();
	      imageContainer.add(image);
	      if (chiSelected.contains(count)==true)
	    	  imageContainer.setStyleName("imgBigger");
	      else
	        imageContainer.setStyleName("imgContainer");
	      panel.add(imageContainer);
	      count++;
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
  private List<Dialogs.OptionsDialogEntry> getButtons(List<String> options)
  {
	  List<Dialogs.OptionsDialogEntry> result = new ArrayList<Dialogs.OptionsDialogEntry> ();
	  for (int i=0;i<options.size();i++)
	  {
		  if (options.get(i).charAt(0)=='P')
			  result.add(new Dialogs.OptionsDialogEntry(options.get(i),Dialogs.ButtonType.IMPORTANT));
		  else
		      result.add(new Dialogs.OptionsDialogEntry(options.get(i),Dialogs.ButtonType.NORMAL));
	  }
	  return result;
  }
  private void alertMahJongMessage(MahJongMessage mahjongMessage)
  {
	  MahjongConstants constants = (MahjongConstants) GWT.create(MahjongConstants.class);
	  String message="";
	  List<String> options=Lists.newArrayList();
	  List<Dialogs.OptionsDialogEntry> welcomeButton = new ArrayList<Dialogs.OptionsDialogEntry> ();
		switch (mahjongMessage) {
		case END:
			welcomeButton.add(new Dialogs.OptionsDialogEntry(
					constants.youWin(), Dialogs.ButtonType.IMPORTANT));
			break;
		case LOSE:
			welcomeButton.add(new Dialogs.OptionsDialogEntry(constants
					.youLose(), Dialogs.ButtonType.IMPORTANT));
			break;
		case Discard:
			if (switchOrDiscard == 0)
				welcomeButton.add(new Dialogs.OptionsDialogEntry(constants
						.arrange(), Dialogs.ButtonType.IMPORTANT));
			else
				welcomeButton.add(new Dialogs.OptionsDialogEntry(constants
						.discardTile(), Dialogs.ButtonType.IMPORTANT));
			break;
		default:
			welcomeButton.add(new Dialogs.OptionsDialogEntry(constants
					.waitFor(), Dialogs.ButtonType.IMPORTANT));
			break;
		}
		int skip = 0;
		switch (mahjongMessage) {

		case PICK:
			message += constants.yourTurn();

			break;
		case HU:
			message += constants.huMsg();
			options.add(constants.huYes());
			options.add(message + constants.deny());
			break;
		case GANG:
			List<Integer> comboToGang = presenter.gangHelper();
			if (comboToGang.size() == 4) {
				message += constants.gangMsg();
				options.add(constants.gangYes());
				options.add(message + constants.deny());
			} else {
				//welcomeButton.add(new Dialogs.OptionsDialogEntry(constants
				//		.waitFor(), Dialogs.ButtonType.IMPORTANT));
				skip = 1;
			}
			break;
		case PENG:

			List<Integer> comboToPeng = presenter.pengHelper();
			if (comboToPeng.size() == 3) {
				message += constants.pengMsg();
				options.add(constants.pengYes());
				options.add(message + constants.deny());
			} else {
				//welcomeButton.add(new Dialogs.OptionsDialogEntry(constants
					//	.waitFor(), Dialogs.ButtonType.IMPORTANT));
				skip = 2;
			}
			break;
		case CHI:
			List<Integer> comboToChi = presenter.chiHelper();
			if (comboToChi.size() == 3) {
				message += constants.chiMsg();
				//options.add(constants.chiYes());
				options.add(message + constants.denyChi());
			} else {
			//	welcomeButton.add(new Dialogs.OptionsDialogEntry(constants
			//			.waitFor(), Dialogs.ButtonType.IMPORTANT));
				skip = 3;
			}
			break;
		case INVISIBLE:
			break;

		default:
			break;
		}
		//Dialogs.OptionCallback decoration = new callBackHelper();
		dialogue.options(welcomeButton, null, Dialogue);
		switch (skip) {
		case (1):
			presenter.refusegang();
			break;
		case (2):
			presenter.refusepeng();
			break;
		case (3):
			presenter.refusechi();
			break;
		default:
			break;

		}
		if (mahjongMessage == MahJongMessage.PICK) {
			options.add(message + ":" + constants.accept());
		}

		if (message.equals("") == false) {

			PopupChoices.OptionChosen eventTriggered = eventFactory.build(
					presenter, mahjongMessage);
			List<Dialogs.OptionsDialogEntry> buttons = getButtons(options);
			Dialogs.OptionCallback callback = new callBackHelper(
					eventTriggered, mahjongMessage, buttons);
			dialogue.options(buttons, callback, Dialogue);

		}
	
  }
  
 
  
  private void disableClicks() {
    claimBtn.setEnabled(false);
    claimHu.setEnabled(false);
    enableClicks = false;
   // enableClicks = true;
  }

  @UiHandler("claimBtn")
	void onClickClaimBtn(ClickEvent e) {
		disableClicks();
		if (claimBtn.getText().equals(constants.chi()) == true) {
              presenter.chi(chiSequence);
		} else {
			if (switchOrDiscard == 0) {
				switchOrDiscard = 1;
				presenter.chooseTile();
			} else {
				presenter.finishedSelectingTiles();
			}
		}
	}
  
  @UiHandler("claimHu")
  void onClickClaimHu(ClickEvent e) {
	  
    disableClicks();
    //System.out.println("Hu!!!!!");
    presenter.Hu();
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
    if (numberOfTilesAtHand2<0)
      placeVerticalImages(rightAtHandArea, createVerticalBackTiles(numberOfTilesAtHand2,false));
    placeHorizonImages(acrossAtHandArea, createHorizonBackTiles(numberOfTilesAtHand3));
    if (numberOfTilesAtHand4<0)
      placeVerticalImages(leftAtHandArea, createVerticalBackTiles(numberOfTilesAtHand4,false));
    placeHorizonImages(myDeclaredArea, createHorizonTileImages(tilesAtDeclared1, false));
    if (numberOfTilesAtHand2<0)
      placeVerticalImages(rightDeclaredArea, createLeftTileImages(tilesAtDeclared2, false));
    placeHorizonImages(acrossDeclaredArea, createHorizonTileImages(tilesAtDeclared3, false));
    if (numberOfTilesAtHand4<0)
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
	  switchOrDiscard = 0;
	  switchCardIndex = -1;
	  chiSelected=new ArrayList<Integer> ();
	  chiSequence = new ArrayList<Integer> ();
	placeHorizonImages(myAtHandArea, createHorizonTileImages(myTilesAtHand, false));
	if (numberOfTilesAtHandLeft>0)
      placeVerticalImages(leftAtHandArea, createVerticalBackTiles(numberOfTilesAtHandLeft,false));
    placeHorizonImages(acrossAtHandArea, createHorizonBackTiles(numberOfTilesAtHandAcross));
    if (numberOfTilesAtHandRight>0)
      placeVerticalImages(rightAtHandArea, createVerticalBackTiles(numberOfTilesAtHandRight,true));
    if (numberOfTilesAtHandLeft>0)
      placeVerticalImages(leftDeclaredArea, createLeftTileImages(tilesAtDeclaredLeft, false));
    placeHorizonImages(acrossDeclaredArea, createHorizonTileImages(tilesAtDeclaredAcross, false));
    if (numberOfTilesAtHandRight>0)
      placeVerticalImages(rightDeclaredArea, createRightTileImages(tilesAtDeclaredRight, false));
    placeHorizonImages(usedArea, createHorizonTileImages(tilesUsed, false));    
    placeHorizonImages(selectedArea, ImmutableList.<Image>of());
    placeHorizonImages(myDeclaredArea, createHorizonTileImages(myTilesDeclared, false));
    disableClicks();
    alertMahJongMessage(mahJongMessage);
    
  }
  
  @Override
  public void setPlayerState(List<Tile> tilesAtLeft,List<Tile> tilesAtRight, 
		  List<Tile> tilesAtAcross,
  		List<Tile> tilesAtDeclaredLeft, List<Tile> tilesAtDeclaredRight, 
  		List<Tile> tilesAtDeclaredAcross,
  		int numberOfTilesAtWall, List<Tile> tilesUsed,
          List<Tile> myTilesAtHand, List<Tile> myTilesDeclared,
          MahJongMessage mahJongMessage) {
    //Collections.sort(myTilesAtHand);



	
	placeHorizonImages(myAtHandArea, createHorizonTileImages(myTilesAtHand, false));
	if (tilesAtLeft!=null)
	  placeVerticalImages(leftAtHandArea, createLeftTileImages(tilesAtLeft,false));
    
	placeHorizonImages(acrossAtHandArea, createHorizonTileImages(tilesAtAcross, false));
	
   
	if (tilesAtRight!=null)
	  placeVerticalImages(rightAtHandArea, createRightTileImages(tilesAtRight,false));
	if (tilesAtLeft!=null)
      placeVerticalImages(leftDeclaredArea, createLeftTileImages(tilesAtDeclaredLeft, false));
    placeHorizonImages(acrossDeclaredArea, createHorizonTileImages(tilesAtDeclaredAcross, false));
    if (tilesAtRight!=null)
      placeVerticalImages(rightDeclaredArea, createRightTileImages(tilesAtDeclaredRight, false));
    placeHorizonImages(usedArea, createHorizonTileImages(tilesUsed, false));    
    placeHorizonImages(selectedArea, ImmutableList.<Image>of());
    placeHorizonImages(myDeclaredArea, createHorizonTileImages(myTilesDeclared, false));
    alertMahJongMessage(mahJongMessage);
    disableClicks();
  }

  @Override
  public void chooseTile(List<Tile> selectedTiles, List<Tile> remainingTiles) {
    
    Collections.sort(selectedTiles);
    enableClicks = true;
    MahjongConstants constants = (MahjongConstants) GWT.create(MahjongConstants.class);
		if (switchOrDiscard == 0) {
			claimBtn.setText(constants.finish());
			claimBtn.setEnabled(true);
				
		} else {
			
			List<Dialogs.OptionsDialogEntry> welcomeButton = new ArrayList<Dialogs.OptionsDialogEntry> ();
			welcomeButton.add(new Dialogs.OptionsDialogEntry(constants.discardTile(), Dialogs.ButtonType.IMPORTANT));
		   
		    dialogue.options(welcomeButton,null,Dialogue);		
			claimBtn.setText(constants.discard());
			claimBtn.setEnabled(!selectedTiles.isEmpty());
		}
    
    placeHorizonImages(selectedArea, createHorizonTileImages(ImmutableList.<Tile>copyOf(selectedTiles), true));
    placeHorizonImages(myAtHandArea, createHorizonTileImages(remainingTiles, true));
    int widgetCount = rightAtHandArea.getWidgetCount();
    placeVerticalImages(rightAtHandArea, createVerticalBackTiles(widgetCount,true));
    
    if (presenter.huHelper()==true)
    	claimHu.setEnabled(true);
    
  }
  @Override
  public void chooseTileToHu(List<Tile> remainingTiles) {

    enableClicks = true;
    if (remainingTiles!=null)
    {
      placeHorizonImages(myAtHandArea, createHorizonTileImagesForHu(remainingTiles, false));
      int widgetCount = rightAtHandArea.getWidgetCount();
      placeVerticalImages(rightAtHandArea, createVerticalBackTiles(widgetCount,true));
    }
    alertMahJongMessage(MahJongMessage.HU);
    if (presenter.huHelper()==true)
    	claimHu.setEnabled(true);
    
  }
  @Override
  public void chooseTileToChi(List<Tile> remainingTiles) {
   // Collections.sort(remainingTiles);
	MahjongConstants constants = (MahjongConstants) GWT.create(MahjongConstants.class);
   // Collections.sort(selectedTiles);
    enableClicks = true;
    claimBtn.setText(constants.chi());
    chiSequence = presenter.chiLegal(chiSelected);
    claimBtn.setEnabled( chiSequence.size()>=3);
    placeHorizonImagesForChi(myAtHandArea, createHorizonTileImagesToChi(remainingTiles, true));
 
  }

  
}
