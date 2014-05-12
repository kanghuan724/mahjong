package org.mahjong.client;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.mahjong.client.*;
import org.game_api.GameApi.Container;
import org.game_api.GameApi.EndGame;
import org.game_api.GameApi.Operation;
import org.game_api.GameApi.SetTurn;
import org.game_api.GameApi.UpdateUI;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * The presenter that controls the mahjong graphics.
 * We use the MVP pattern:
 * the model is {@link MahJongState},
 * the view will have the mahjong graphics and it will implement {@link MahJongPresenter.View},
 * and the presenter is {@link MahJongPresenter}.
 */
public class MahJongPresenter {
  /**
   * The possible mahjong messages.
   * The mahjong related messages are:
   * INVISIBLE: if no hu/gang/peng/chi is possible, show nothing (e.g., on the first move)
   * HU: ask the player whether to hu.
   * GANG: ask the player whether to gang.
   * PENG: ask the player whether to peng.
   * CHI: ask the player whether to chi.
   */
  public enum MahJongMessage {
    INVISIBLE,LOSE,END, HU, GANG, PENG, CHI, PICK,Discard, WaitForHu,WaitForChi,WaitForGang,WaitForPeng,BLIND;
  }

  private static final String WP = "WaitForPeng";
  private static final String WC = "WaitForChi";
  private static final String WG = "WaitForGang";
  private static final String WH = "WaitForHu";
  private static final String M = "Move";
  private static final String PU = "PickUp";
  private static final String D = "Discard";
  private static final String P = "Peng";
  private static final String C = "Chi";
  private static final String G = "Gang";
  private static final String RP = "RefusePeng";
  private static final String RC = "RefuseChi";
  private static final String RG = "RefuseGang";
  private static final String RH = "RefuseHu";
  private static final String H = "Hu";
	
  public interface View {
    /**
     * Sets the presenter. The viewer will call certain methods on the presenter, e.g.,
     * when a tile is selected ({@link #tileSelected}),
     * when selection is done ({@link #tileDiscarded}), etc.
     *
     * The process of discarding a tile looks as follows to the viewer:
     * 1) The viewer calls {@link #tileSelected} (usually once) to select the tile to discard
     * 2) The viewer calls {@link #tileDiscarded} to finalize his selection
     * The process of discarding a tile looks as follows to the presenter:
     * 1) The presenter calls {@link #chooseTile} and passes the current selection
     * 2) The presenter calls {@link #confirmSelection} and passes the current selection.
     *
     * The process of hu/gang/peng/chi a tile looks as follows to the viewer:
     * 1) The viewer calls {@link #hu/gang/peng/chi} to catch the last tile used
     * 2) The viewer calls {@link #finishedChoosingCombo} to finalize the tiles to declare 
     * if more than one possible combo exist (except hu)
     * The process of discarding a tile looks as follows to the presenter:
     * 1) The viewer calls {@link #hu/gang/peng/chiAvailable}
     * 2) The presenter calls {@link #chooseCombo} if more than one possible combo exist (except hu).
     */
    void setPresenter(MahJongPresenter mahJongPresenter);

    /** Sets the state for a viewer, i.e., not one of the players. */
    void setViewerState(int numberOfTilesAtHand1, int numberOfTilesAtHand2, 
    		int numberOfTilesAtHand3, int numberOfTilesAtHand4,
    		List<Tile> tilesAtDeclared1, List<Tile> tilesAtDeclared2,
    		List<Tile> tilesAtDeclared3, List<Tile> tilesAtDeclared4,
    		int numberOfTilesAtWall, List<Tile> tilesUsed,
    		MahJongMessage mahJongMessage);

    /**
     * Sets the state for a player (whether the player has the turn or not).
     * The "hu/gang/peng/chi" button should be enabled only for MahJongMessage.HU/GANG/PENG/CHI.
     * The LEFT player is the previous one while the RIGHT player is the next.
     */
    void setPlayerState(int numberOfTilesAtHandLeft, int numberOfTilesAtHandRight, 
    		int numberOfTilesAtHandAcross,
    		List<Tile> tilesAtDeclaredLeft, List<Tile> tilesAtDeclaredRight, 
    		List<Tile> tilesAtDeclaredAcross,
    		int numberOfTilesAtWall, List<Tile> tilesUsed,
            List<Tile> myTilesAtHand, List<Tile> myTilesDeclared,
            MahJongMessage mahJongMessage);
    void setPlayerState(List<Tile> tilesAtLeft, List<Tile> tilesAtRight, 
    		List<Tile> tilesAtAcross,
    		List<Tile> tilesAtDeclaredLeft, List<Tile> tilesAtDeclaredRight, 
    		List<Tile> tilesAtDeclaredAcross,
    		int numberOfTilesAtWall, List<Tile> tilesUsed,
            List<Tile> myTilesAtHand, List<Tile> myTilesDeclared,
            MahJongMessage mahJongMessage);
    /**
     * Asks the player to choose the Tile or finish his selection.
     * We pass what tile is selected (will be discarded), and what tiles will remain in the player hand.
     * The user can either select another tile (by calling {@link #tileSelected),
     * or discard the currently selected tile
     * (by calling {@link #tileDiscarded}; only allowed if selectedTile.size=1).
     * If the user selects a tile from remainingTiles, then it replaces selectedTile with that tile,
     * i.e. moves the previous selectedTile to remainingTiles.
     */
    void chooseTile(List<Tile> selectedTile, List<Tile> remainingTiles);
    void chooseTileToHu( List<Tile> remainingTiles);
    void chooseTileToChi(List<Tile> remainingTiles);
    void waitFor();
    
  }
  List<Integer> fakeTile = new ArrayList<Integer> ();
  private boolean chooseToHu = false;
  private final MahJongLogic mahJongLogic = new MahJongLogic();
  private final View view;
  private final Container container;
  /** A viewer doesn't have a playerId. */
  private String turn;
  private MahJongState mahJongState;
  //private Tile selectedTile;
  private List<Tile> selectedTile=new ArrayList<Tile> ();
  //private boolean chi;
  MahJongMessage mahJongMessage;
  private Tile lastUsedTile;
  private List<Tile> selectedCombo;
  private boolean gameEnd;

  public MahJongPresenter(View view, Container container) {
    this.view = view;
    this.container = container;
    view.setPresenter(this);
  }
  public int idIndex(List<String> playerIds,String playerId)
  {
	  for (int i=0;i<playerIds.size();i++)
		   if (playerIds.get(i).equals(playerId))
			     return i;
	  return 0;
  }

  /** Updates the presenter and the view with the state in updateUI. */
  public void updateUI(UpdateUI updateUI) {
	  gameEnd = false;
	  List<String> playerIds = updateUI.getPlayerIds();
		String yourPlayerId = updateUI.getYourPlayerId();
		int playerNum = playerIds.size();
	    if (updateUI.getState().isEmpty()) {
	      // The 0 player sends the initial setup move.
         

	      if (playerIds.indexOf(yourPlayerId) == 0) {

	        sendInitialMove(playerIds);
	      }
	      return;
	    }
	  for (Operation operation : updateUI.getLastMove()) {
	      if (operation instanceof SetTurn) {
	        turn = ((SetTurn) operation).getPlayerId();
	      }
	      if (operation instanceof EndGame)
	      {
	    	  gameEnd = true;
	    	  break;
	      }
	    }
		
		mahJongState = mahJongLogic.gameApiStateToMahJongState(
					updateUI.getState(), yourPlayerId, playerIds);
		
		
		if (gameEnd == false) {
			mahJongMessage = getMahJongMessage();
		}
		
		if (updateUI.isAiPlayer()) {
			int yourPlayerIndex = updateUI.getPlayerIndex(yourPlayerId);
			chooseToHu = false;
			if (isMyTurn() == false)
				return;
			if (gameEnd == true || mahJongState.getMove().getName().equals(H)) {
				if (isMyTurn() && gameEnd == false) {
					gameEnd(turn);
				}
				return;
			}
			switch (mahJongMessage) {
			case PICK:
				pickUpTile();
				break;
			case Discard:
				List<Integer> selectedTileIndex = Lists.newArrayList();
				String playerId = mahJongState.getTurn();
				List<Integer> atHand = mahJongState
						.getTilesAtHand(String.valueOf(idIndex(
								mahJongState.getPlayerIds(), playerId)));
				Random random = new Random();
				int selectedTile = random.nextInt(atHand.size());
				selectedTileIndex.add(atHand.get(atHand.size()-1));
				container.sendMakeMove(mahJongLogic.discard(mahJongState,
						selectedTileIndex, mahJongState.getPlayerIds()));
				break;
			case WaitForHu:
				waitForHu();
				break;
			case WaitForGang:
				waitForGang();
				break;
			case WaitForPeng:
				waitForPeng();
				break;
			case WaitForChi:
				waitForChi();
				break;
			case HU:
				if (huHelper() == true) {
					Hu();
				} else {
					refusehu();
				}
				break;
			case GANG:
				if (gangHelper().size() == 4)
					gang(gangHelper());
				else
					refusegang();
				break;
			case PENG:
				if (pengHelper().size() == 3)
					peng(pengHelper());
				else
					refusepeng();
				break;
			case CHI:
				if (chiHelper().size() == 3)
					chi(chiHelper());
				else
					refusechi();
				break;
			}
			return;
		}
		
	if (gameEnd ==true||mahJongState.getMove().getName().equals(H))
	{
			if (playerNum == 4) {
				String rightId = MahJongLogic.nextId(yourPlayerId, playerIds);
				String acrossId = MahJongLogic.nextId(rightId, playerIds);
				String leftId = MahJongLogic.nextId(acrossId, playerIds);
				List<Tile> TilesAtHandLeft = getTiles(mahJongState
						.getTilesAtHand(String.valueOf(idIndex(playerIds,
								leftId))));
				List<Tile> TilesAtAcross = getTiles(mahJongState
						.getTilesAtHand(String.valueOf(idIndex(playerIds,
								acrossId))));
				List<Tile> TilesAtRight = getTiles(mahJongState
						.getTilesAtHand(String.valueOf(idIndex(playerIds,
								rightId))));
				List<Tile> tilesAtDeclaredLeft = getTiles(mahJongState
						.getTilesAtDeclared(String.valueOf(idIndex(playerIds,
								leftId))));
				List<Tile> tilesAtDeclaredAcross = getTiles(mahJongState
						.getTilesAtDeclared(String.valueOf(idIndex(playerIds,
								acrossId))));
				List<Tile> tilesAtDeclaredRight = getTiles(mahJongState
						.getTilesAtDeclared(String.valueOf(idIndex(playerIds,
								rightId))));
				view.setPlayerState(TilesAtHandLeft, TilesAtRight,
						TilesAtAcross, tilesAtDeclaredLeft,
						tilesAtDeclaredRight, tilesAtDeclaredAcross,
						mahJongState.getTilesAtWall().size(),
						getTiles(mahJongState.getTilesUsed()),
						getTiles(mahJongState.getTilesAtHand(String
								.valueOf(idIndex(playerIds, yourPlayerId)))),
						getTiles(mahJongState.getTilesAtDeclared(String
								.valueOf(idIndex(playerIds, yourPlayerId)))),
						getMahJongMessage());
				if (isMyTurn() && gameEnd == false) {
					gameEnd(turn);
				}
				return;
			}
			if (playerNum == 2){
				
				String acrossId = MahJongLogic.nextId(yourPlayerId, playerIds);
				List<Tile> TilesAtAcross = getTiles(mahJongState
						.getTilesAtHand(String.valueOf(idIndex(playerIds,
								acrossId))));

				List<Tile> tilesAtDeclaredAcross = getTiles(mahJongState
						.getTilesAtDeclared(String.valueOf(idIndex(playerIds,
								acrossId))));
				view.setPlayerState(null, null,
						TilesAtAcross, null,
						null, tilesAtDeclaredAcross,
						mahJongState.getTilesAtWall().size(),
						getTiles(mahJongState.getTilesUsed()),
						getTiles(mahJongState.getTilesAtHand(String
								.valueOf(idIndex(playerIds, yourPlayerId)))),
						getTiles(mahJongState.getTilesAtDeclared(String
								.valueOf(idIndex(playerIds, yourPlayerId)))),
						getMahJongMessage());
				if (isMyTurn() && gameEnd == false) {
					gameEnd(turn);
				}
				return;
			}
	}
    
    int yourPlayerIndex = updateUI.getPlayerIndex(yourPlayerId);
   
   
   
    chooseToHu = false;

    if (updateUI.isViewer()) {
			if (playerNum == 4) {
				view.setViewerState(mahJongState.getTilesAtHand("0").size(),
						mahJongState.getTilesAtHand("1").size(), mahJongState
								.getTilesAtHand("2").size(), mahJongState
								.getTilesAtHand("3").size(),
						getTiles(mahJongState.getTilesAtDeclared("0")),
						getTiles(mahJongState.getTilesAtDeclared("1")),
						getTiles(mahJongState.getTilesAtDeclared("2")),
						getTiles(mahJongState.getTilesAtDeclared("3")),
						mahJongState.getTilesAtWall().size(),
						getTiles(mahJongState.getTilesUsed()), mahJongMessage);
			}
			if (playerNum == 2) {
				view.setViewerState(mahJongState.getTilesAtHand("0").size(),
						-1, mahJongState.getTilesAtHand("2").size(), -1,
						getTiles(mahJongState.getTilesAtDeclared("0")), null,
						getTiles(mahJongState.getTilesAtDeclared("2")), null,
						mahJongState.getTilesAtWall().size(),
						getTiles(mahJongState.getTilesUsed()), mahJongMessage);
			}
      return;
    }
      
   
    
    String rightId,acrossId,leftId;
    if (playerNum==4)
     rightId = MahJongLogic.nextId(yourPlayerId, playerIds);
    else
      rightId = null;
    if (playerNum==4)
      acrossId = MahJongLogic.nextId(rightId, playerIds);
    else
      acrossId = MahJongLogic.nextId(yourPlayerId, playerIds);
    if (playerNum==4)
      leftId = MahJongLogic.nextId(acrossId, playerIds);
    else
      leftId = null;
 
    int numberOfTilesAtHandLeft,numberOfTilesAtHandAcross,numberOfTilesAtHandRight;
    if (playerNum==4)
      numberOfTilesAtHandLeft = mahJongState.getTilesAtHand(String.valueOf(idIndex(playerIds,leftId))).size();
    else
      numberOfTilesAtHandLeft = -1;
    numberOfTilesAtHandAcross = mahJongState.getTilesAtHand(String.valueOf(idIndex(playerIds,acrossId))).size();
    if (playerNum==4)
      numberOfTilesAtHandRight = mahJongState.getTilesAtHand(String.valueOf(idIndex(playerIds,rightId))).size();
    else
    	numberOfTilesAtHandRight = -1;
    List<Tile> tilesAtDeclaredLeft,tilesAtDeclaredAcross,tilesAtDeclaredRight;
    if (playerNum==4)
     tilesAtDeclaredLeft = getTiles(mahJongState.getTilesAtDeclared(String.valueOf(idIndex(playerIds,leftId))));
    else
     tilesAtDeclaredLeft =null;
    tilesAtDeclaredAcross = getTiles(mahJongState.getTilesAtDeclared(String.valueOf(idIndex(playerIds,acrossId))));
    if (playerNum==4)
     tilesAtDeclaredRight = getTiles(mahJongState.getTilesAtDeclared(String.valueOf(idIndex(playerIds,rightId))));
    else
      tilesAtDeclaredRight=null;
    if (mahJongMessage!=MahJongMessage.END)
    {
			if (playerNum == 4)
				view.setPlayerState(numberOfTilesAtHandLeft,
						numberOfTilesAtHandRight, numberOfTilesAtHandAcross,
						tilesAtDeclaredLeft, tilesAtDeclaredRight,
						tilesAtDeclaredAcross, mahJongState.getTilesAtWall()
								.size(), getTiles(mahJongState.getTilesUsed()),
						getTiles(mahJongState.getTilesAtHand(String
								.valueOf(idIndex(playerIds, yourPlayerId)))),
						getTiles(mahJongState.getTilesAtDeclared(String
								.valueOf(idIndex(playerIds, yourPlayerId)))),
						getMahJongMessage());
           if (playerNum==2)
				view.setPlayerState(-1, -1, numberOfTilesAtHandAcross, null,
						null, tilesAtDeclaredAcross, mahJongState
								.getTilesAtWall().size(), getTiles(mahJongState
								.getTilesUsed()), getTiles(mahJongState
								.getTilesAtHand(String.valueOf(idIndex(
										playerIds, yourPlayerId)))),
						getTiles(mahJongState.getTilesAtDeclared(String
								.valueOf(idIndex(playerIds, yourPlayerId)))),
						getMahJongMessage());
    }   

    if (isMyTurn()) {
    	if (mahJongMessage==MahJongMessage.PICK)
    	{
    		pickUpTile();
    	}
    	if (mahJongMessage==MahJongMessage.CHI)
    	{
    		chooseTileToChi();
    	}
        if (mahJongMessage==MahJongMessage.Discard) {
          
      	  chooseTile();
        }
        if (mahJongMessage==MahJongMessage.WaitForHu)
        {	waitForHu();}
        if (mahJongMessage==MahJongMessage.WaitForGang)
        {	
        	
        	waitForGang();
        }
        if (mahJongMessage==MahJongMessage.WaitForPeng)
        { 	waitForPeng();}
        if (mahJongMessage==MahJongMessage.WaitForChi)
        {	waitForChi();}
      }
    
    
    
  }

 
  private MahJongMessage getMahJongMessage() {
	if (isMyTurn()==false)
	{
		if (gameEnd==true)
		{
			return mahJongMessage.LOSE;
		}
		else
		{
		  return MahJongMessage.BLIND;
		}
	}
	if (gameEnd==true)
		return mahJongMessage.END;
    switch (mahJongState.getMove().getName()) {
    case (H):
    	return MahJongMessage.END;
    case ("Empty"):
    	return MahJongMessage.PICK;
    case (P):
    	 return MahJongMessage.Discard;
    case (C):
    	 return MahJongMessage.Discard;
    case (G):
    	 return MahJongMessage.PICK;
    case (PU):
    	return MahJongMessage.Discard;
    case (D):
    	return MahJongMessage.WaitForHu;
    case (WH):
    	//if (canHu()) 
    	  return MahJongMessage.HU;
		case (RH): {
			RefuseHu move = (RefuseHu) (mahJongState.getMove());
			String sourceId = move.getSource();
			String currentTurn = mahJongState.getTurn();
			if (sourceId.equals(currentTurn)) {
				return MahJongMessage.WaitForGang;
			} else {
				return MahJongMessage.HU;
			}
		}
		case (RG): {
			RefuseGang move = (RefuseGang) (mahJongState.getMove());
			String sourceId = move.getSource();
			String currentTurn = mahJongState.getTurn();
			if (sourceId.equals(currentTurn)) {
				return MahJongMessage.WaitForPeng;
			} else {
				return MahJongMessage.GANG;
			}
		}
		case (RP): {
			RefusePeng move = (RefusePeng) (mahJongState.getMove());
			String sourceId = move.getSource();
			String currentTurn = mahJongState.getTurn();
			if (sourceId.equals(currentTurn)) {
				return MahJongMessage.WaitForChi;
			} else
				return MahJongMessage.PENG;
		}
		case (RC): {
			return MahJongMessage.PICK;
		}
    case (WG):
    	
			return MahJongMessage.GANG;
		case (WP):
			
			return MahJongMessage.PENG;
		case (WC):
			
			return MahJongMessage.CHI;
		}
    return MahJongMessage.INVISIBLE;
  }

  private boolean isMyTurn() {
    return (turn.equals(mahJongState.getTurn()));
  }
 
  public void pickUpTile()
  {
	  //check
	  container.sendMakeMove(mahJongLogic.pickUp(mahJongState,  mahJongState.getPlayerIds())); 
  }
  private List<Tile> getTiles(List<Integer> targetIndices) {
	  List<Tile> targetTiles = Lists.newArrayList();
	  ImmutableList<Optional<Tile>> tiles = mahJongState.getTiles();
	  for (Integer tileIndex : targetIndices) {
		  targetTiles.add(tiles.get(tileIndex).get());
	  }
	  return targetTiles;	  
  }
  
  private void check(boolean val) {
	    if (!val) {
	      throw new IllegalArgumentException();
	    }
  }
  
  public  void chooseTile() {
	  
    
    List<Tile> current = getTiles(mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),turn))));

    view.chooseTile(selectedTile, 
    		mahJongLogic.subtract(getTiles(mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),turn)))), selectedTile));
  }
  private void chooseTileToHu () {
	  try
	  {

			List<Integer> tiles = mahJongState.getTilesAtHand(String
					.valueOf(idIndex(mahJongState.getPlayerIds(), turn)));
			List<Integer> Used = mahJongState.getTilesUsed();
			int huIndex = Used.get(Used.size() - 1);
			List<Integer> tilesTemp = new ArrayList<Integer>();
			for (int index : tiles) {
				tilesTemp.add(index);
			}
			if (chooseToHu == false)
			{
				int id =idIndex(mahJongState.getPlayerIds(),turn);
				tilesTemp.add(huIndex);
				ImmutableList<Integer> targetImmute = ImmutableList.copyOf(tilesTemp);
				mahJongState.changeTileSequence(id, targetImmute);
				
				fakeTile .add(huIndex);
			}
			List<Tile> current = getTiles(tilesTemp);
			chooseToHu = true;
			view.chooseTileToHu(current);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

  }
  public void chooseTileToChi() {
	    view.chooseTileToChi(getTiles(mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),turn)))));
	  }
 

  public void tileSwitch(int origin,int des)
  {
	  if (origin==des)
	  {
		  chooseTile();
		  return;
	  }
	  int id =idIndex(mahJongState.getPlayerIds(),turn);

	  List<Integer> current = mahJongState.getTilesAtHand(String.valueOf(id));

	  List<Integer> target =new ArrayList<Integer> ();
	  for (int i=0;i<current.size();i++)
	  {
		  if (i!=origin)
		  {
			  if (i!=des)
			    target.add(current.get(i));
			  else
			  {
				  target.add(current.get(origin));
				  target.add(current.get(i));
			  }
				  
		  }
		  
	  }
	  ImmutableList<Integer> targetImmute = ImmutableList.copyOf(target);
	  mahJongState.changeTileSequence(id, targetImmute);
	  if (mahJongMessage==MahJongMessage.Discard)
	    chooseTile();
	  else
	  {
		chooseTileToHu();
	  }
  }
  
  public void tileSwitch(int origin)
  {
	  
	  int id =idIndex(mahJongState.getPlayerIds(),turn);

	  List<Integer> current = mahJongState.getTilesAtHand(String.valueOf(id));

	  List<Integer> target =new ArrayList<Integer> ();
	  int des = current.size()-1;
	  for (int i=0;i<current.size();i++)
	  {
		  if (i!=origin)
		  {
			  if (i!=des)
			    target.add(current.get(i));
			  else
			  {
				  
				  target.add(current.get(i));
				  target.add(current.get(origin));
			  }
				  
		  }
		  
	  }
	  ImmutableList<Integer> targetImmute = ImmutableList.copyOf(target);
	  mahJongState.changeTileSequence(id, targetImmute);
	  chooseTile();
  }

  public boolean tilePosition(Tile tile) {
	  if (selectedTile.contains(tile)) {
	    	return false;
	    } 
	  
		  return true;
  }
  public void huChoose()
  {
	  chooseTileToHu();
  }
  
  public void tileSelected(Tile tile) {
   
    if (selectedTile.contains(tile)) {
    	selectedTile.remove(tile) ;
    } 
    else 
    {
      if (mahJongMessage==MahJongMessage.CHI||mahJongMessage==MahJongMessage.HU)
      {
       	  if (selectedTile.size()<2)
       		  selectedTile.add(tile);
      }
     
      else
      {
    	if (selectedTile.size()>=1)
    		selectedTile.remove(0);
        selectedTile .add(tile);
    	
      }
     
    }
    chooseTile();
  }
  
  /**
   * Finishes the tile selection process.
   * The view can only call this method if the presenter called {@link View#chooseTile}
   * and exactly one tile was selected by calling {@link #tileSelected}.
   */
  
  void tileDiscarded() {
    check(isMyTurn() && selectedTile.size()>0);
    List<Integer> selectedTileIndex = Lists.newArrayList();
    String playerId=mahJongState.getTurn();
    List<Integer> atHand=mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),playerId)));
    for (int index = 0; index < atHand.size(); index++) {
    	if ((mahJongState.getTiles().get(atHand.get(index)).get().equals(selectedTile.get(0)))) {
    		selectedTileIndex.add(atHand.get(index));
    		break;
    	}
    }
    selectedTile=new ArrayList<Tile> ();
    view.waitFor();
    container.sendMakeMove(mahJongLogic.discard(mahJongState, selectedTileIndex, mahJongState.getPlayerIds()));
  }
  void tileChi() {
	    check(isMyTurn() && selectedTile != null);
	    List<Integer> selectedTileIndex = Lists.newArrayList();
	    String playerId=mahJongState.getTurn();
	    List<Integer> atHand=mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),playerId)));
	    for (int index = 0; index < atHand.size(); index++) {
	    	if ((mahJongState.getTiles().get(atHand.get(index)).get().equals(selectedTile.get(0)))) {
	    		selectedTileIndex.add(atHand.get(index));
	    		break;
	    	}
	    }
	    for (int index = 0; index < atHand.size(); index++) {
	    	if ((mahJongState.getTiles().get(atHand.get(index)).get().equals(selectedTile.get(1)))) {
	    		selectedTileIndex.add(atHand.get(index));
	    		break;
	    	}
	    }
	    List<Integer> Used=mahJongState.getTilesUsed();
	    selectedTileIndex.add(Used.size()-1);
	    
	    selectedTile=new ArrayList<Tile> ();
	    container.sendMakeMove(mahJongLogic.chi(mahJongState, selectedTileIndex, mahJongState.getPlayerIds()));
	  }
  

  

  /**
   * Sends a move of hu/gang/peng/chi.
   * The view can only call this method if the presenter passed
   * CheaterMessage.HU/GANG/PENG/CHI in {@link View#setPlayerState}.
   */
  public void Hu() {
    container.sendMakeMove(mahJongLogic.hu(mahJongState,  mahJongState.getPlayerIds()));
  }
  public void gameEnd(String playerId) {
	    container.sendMakeMove(mahJongLogic.gameEnd(playerId));
	  }
  void waitForHu() {
	  view.waitFor();
	    container.sendMakeMove(mahJongLogic.WaitForHu(mahJongState,  mahJongState.getPlayerIds()));
	  }
  void waitForGang() {
	  view.waitFor();
	    container.sendMakeMove(mahJongLogic.WaitForGang(mahJongState,  mahJongState.getPlayerIds()));
	  }
  void waitForPeng() {
	    view.waitFor();
	    container.sendMakeMove(mahJongLogic.WaitForPeng(mahJongState,  mahJongState.getPlayerIds()));
	  }
  void waitForChi() {
	    view.waitFor();
	    if (mahJongState.getTilesAtWall().size()==0)
	    	container.sendMakeMove(MahJongLogic.GameEnd(mahJongState.getPlayerIds()));
	    else
	      container.sendMakeMove(mahJongLogic.WaitForChi(mahJongState,  mahJongState.getPlayerIds()));
	  }
  public List<Integer> gangHelper()
  {
      List<Integer> selectedComboIndex = Lists.newArrayList();
	  
	  String playerId = mahJongState.getTurn();
	  List<Integer> lastAtHand = mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),playerId)));
	  List<Integer> Used= mahJongState.getTilesUsed();
	  int gangIndex=Used.get(Used.size() - 1);
	  String gangtile=mahJongState.getTiles().get(gangIndex).get().toString();
	  selectedComboIndex.add(gangIndex);
	  for (int index=0;index<lastAtHand.size();index++)
	  {
		  String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.equals(gangtile))
			  selectedComboIndex.add(lastAtHand.get(index));			  
	  }
	  return selectedComboIndex;
  }
  public boolean huHelper()
  {
	  
	    
		List<Tile> current = getTiles(mahJongState.getTilesAtHand(String
				.valueOf(idIndex(mahJongState.getPlayerIds(), turn))));
		
		if (current.get(current.size()-1).toString().equals(current.get(current.size()-2).toString())==false)
		{
			
			return false;
		}
		for (int i=0;i<current.size()-2;i+=3)
		{
			//System.out.println("i: "+i +"result : "+connectThree(i,current));
			if (connectThree(i,current)==false)
				return false;
		}
		
		return true;
  }
  private boolean connectThree(int startIndex,List<Tile> current)
  {
	  Tile first = current.get(startIndex);
	  Tile second = current.get(startIndex+1);
	  Tile third = current.get(startIndex+2);
	  if (first.toString().equals(second.toString())==true)
	  {
		if( second.toString().equals(third.toString())==true)		    
			return true;
				  
	  }
	  if (first.getSuit()==second.getSuit()&&first.getRank().ordinal()+1==second.getRank().ordinal())
	  {
		  if(second.getSuit()==third.getSuit()&&second.getRank().ordinal()+1==third.getRank().ordinal())
			  return true;
	  }
	  
	  return false;
  }
  public List<Integer> pengHelper()
  {
      List<Integer> selectedComboIndex = Lists.newArrayList();	  
	  String playerId = mahJongState.getTurn();
	  List<Integer> lastAtHand = mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),playerId)));
	  List<Integer> Used= mahJongState.getTilesUsed();
	  int gangIndex=Used.get(Used.size() - 1);
	  String gangtile=mahJongState.getTiles().get(gangIndex).get().toString();
	  selectedComboIndex.add(gangIndex);
	  for (int index=0;index<lastAtHand.size();index++)
	  {
		  String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.equals(gangtile))
			  selectedComboIndex.add(lastAtHand.get(index));			  
	  }
	  return selectedComboIndex;
  }
  public List<Integer> chiLegal(List<Integer> tiles)
  {
	  String playerId = mahJongState.getTurn();
	  List<Integer> lastAtHand = mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),playerId)));
	  List<Integer> currentTiles = new ArrayList<Integer> ();
	  for (int i=0;i<tiles.size();i++)
	  {
		  currentTiles.add(lastAtHand.get(tiles.get(i)));
	  }
	  List<Integer> selectedComboIndex=Lists.newArrayList();
	  if (currentTiles.size()<2)
		  return selectedComboIndex;
	  List<Integer> Used= mahJongState.getTilesUsed();
	  int chiIndex=Used.get(Used.size() - 1);	 
	  selectedComboIndex=chiHelper_1(chiIndex,currentTiles);
	  if (selectedComboIndex.size()==3)
		  return selectedComboIndex;
	  selectedComboIndex=chiHelper_2(chiIndex,currentTiles);
	  if (selectedComboIndex.size()==3)
		  return selectedComboIndex;
	  selectedComboIndex=chiHelper_3(chiIndex,currentTiles);
	  if (selectedComboIndex.size()==3)
		  return selectedComboIndex;
	  return selectedComboIndex;
	  
  }
  public List<Integer> chiHelper()
  {
	  List<Integer> selectedComboIndex=Lists.newArrayList();
	  selectedComboIndex=chiHelper_1();
	  if (selectedComboIndex.size()==3)
		  return selectedComboIndex;
	  selectedComboIndex=chiHelper_2();
	  if (selectedComboIndex.size()==3)
		  return selectedComboIndex;
	  selectedComboIndex=chiHelper_3();
	    return selectedComboIndex;
  }
  public List<Integer> chiHelper_1()
  {
      List<Integer> selectedComboIndex = Lists.newArrayList();	  
	  String playerId = mahJongState.getTurn();
	  List<Integer> lastAtHand = mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),playerId)));
	  List<Integer> Used= mahJongState.getTilesUsed();
	  int chiIndex=Used.get(Used.size() - 1);
	  Tile chitile=mahJongState.getTiles().get(chiIndex).get();
	  selectedComboIndex.add(chiIndex);
	  for (int index=0;index<lastAtHand.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(lastAtHand.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()+1)
		  {
			  selectedComboIndex.add(lastAtHand.get(index));
			  break;
		  }
	  }
	  for (int index=0;index<lastAtHand.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(lastAtHand.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()+2)
		  {
			  selectedComboIndex.add(lastAtHand.get(index));
			  break;
		  }
	  }
	  
	  return selectedComboIndex;
  }
  public List<Integer> chiHelper_1(int chiIndex,List<Integer> selectedChi)
  {
      List<Integer> selectedComboIndex = Lists.newArrayList();	  
	  Tile chitile=mahJongState.getTiles().get(chiIndex).get();
	  selectedComboIndex.add(chiIndex);
	  for (int index=0;index<selectedChi.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(selectedChi.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()+1)
		  {
			  selectedComboIndex.add(selectedChi.get(index));
			  break;
		  }
	  }
	  for (int index=0;index<selectedChi.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(selectedChi.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()+2)
		  {
			  selectedComboIndex.add(selectedChi.get(index));
			  break;
		  }
	  }
	  
	  return selectedComboIndex;
  }
  public List<Integer> chiHelper_2()
  {
	  List<Integer> selectedComboIndex = Lists.newArrayList();	  
	  String playerId =mahJongState.getTurn();
	  List<Integer> lastAtHand = mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),playerId)));
	  List<Integer> Used= mahJongState.getTilesUsed();
	  int chiIndex=Used.get(Used.size() - 1);
	  Tile chitile=mahJongState.getTiles().get(chiIndex).get();
	  selectedComboIndex.add(chiIndex);
	  for (int index=0;index<lastAtHand.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(lastAtHand.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()+1)
		  {
			  selectedComboIndex.add(lastAtHand.get(index));
			  break;
		  }
	  }
	  for (int index=0;index<lastAtHand.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(lastAtHand.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()-1)
		  {
			  selectedComboIndex.add(lastAtHand.get(index));
			  break;
		  }
	  }
	  
	  return selectedComboIndex;
  }
  public List<Integer> chiHelper_2(int chiIndex,List<Integer> selectedChi)
  {
      List<Integer> selectedComboIndex = Lists.newArrayList();	  
	  Tile chitile=mahJongState.getTiles().get(chiIndex).get();
	  selectedComboIndex.add(chiIndex);
	  for (int index=0;index<selectedChi.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(selectedChi.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()+1)
		  {
			  selectedComboIndex.add(selectedChi.get(index));
			  break;
		  }
	  }
	  for (int index=0;index<selectedChi.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(selectedChi.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()-1)
		  {
			  selectedComboIndex.add(selectedChi.get(index));
			  break;
		  }
	  }
	  
	  return selectedComboIndex;
  }
  public List<Integer> chiHelper_3()
  {
	  List<Integer> selectedComboIndex = Lists.newArrayList();	  
	  String playerId = mahJongState.getTurn();
	  List<Integer> lastAtHand = mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),playerId)));
	  List<Integer> Used= mahJongState.getTilesUsed();
	  int chiIndex=Used.get(Used.size() - 1);
	  Tile chitile=mahJongState.getTiles().get(chiIndex).get();
	  selectedComboIndex.add(chiIndex);
	  for (int index=0;index<lastAtHand.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(lastAtHand.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()-1)
		  {
			  selectedComboIndex.add(lastAtHand.get(index));
			  break;
		  }
	  }
	  for (int index=0;index<lastAtHand.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(lastAtHand.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()-2)
		  {
			  selectedComboIndex.add(lastAtHand.get(index));
			  break;
		  }
	  }
	  
	  return selectedComboIndex;
  }
  public List<Integer> chiHelper_3(int chiIndex,List<Integer> selectedChi)
  {
      List<Integer> selectedComboIndex = Lists.newArrayList();	  
	  Tile chitile=mahJongState.getTiles().get(chiIndex).get();
	  selectedComboIndex.add(chiIndex);
	  for (int index=0;index<selectedChi.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(selectedChi.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()-1)
		  {
			  selectedComboIndex.add(selectedChi.get(index));
			  break;
		  }
	  }
	  for (int index=0;index<selectedChi.size();index++)
	  {
		  Tile current=mahJongState.getTiles().get(selectedChi.get(index)).get();
		  //String current=mahJongState.getTiles().get(lastAtHand.get(index)).get().toString();
		  if (current.getSuit()==chitile.getSuit()&&current.getRank().ordinal()==chitile.getRank().ordinal()-2)
		  {
			  selectedComboIndex.add(selectedChi.get(index));
			  break;
		  }
	  }
	  
	  return selectedComboIndex;
  }
  public void gang(List<Integer> selectedComboIndex) {
	    view.waitFor();
	    container.sendMakeMove(mahJongLogic.gang(mahJongState, selectedComboIndex, mahJongState.getPlayerIds()));
	   
  }
  public void refusegang() {
	    view.waitFor();
	    container.sendMakeMove(mahJongLogic.refusegang(mahJongState,  mahJongState.getPlayerIds()));
	    
}
  public void refusepeng() {
	  
	    view.waitFor();
	    container.sendMakeMove(mahJongLogic.refusepeng(mahJongState,  mahJongState.getPlayerIds()));
	    
}
  public void refusehu() {
	    if (fakeTile.size()>0)
	    {
	    	int id =idIndex(mahJongState.getPlayerIds(),turn);
	    	List<Integer> current = mahJongState.getTilesAtHand(String.valueOf(id));
	        mahJongLogic.subtract(current,fakeTile);
	        ImmutableList<Integer> targetImmute = ImmutableList.copyOf(current);
	  	    mahJongState.changeTileSequence(id, targetImmute);
	    }
	    view.waitFor();
	    container.sendMakeMove(mahJongLogic.refusehu(mahJongState,  mahJongState.getPlayerIds()));
	    
}
  public void refusechi() {
	    view.waitFor();
	    container.sendMakeMove(mahJongLogic.refusechi(mahJongState,  mahJongState.getPlayerIds()));
	    
}
  public void peng(List<Integer> selectedComboIndex) {
	 
	  view.waitFor();
	  container.sendMakeMove(mahJongLogic.peng(mahJongState, selectedComboIndex, mahJongState.getPlayerIds()));
	 
  }
  public void chi(List<Integer> selectedComboIndex) {
	
	  view.waitFor();
	  container.sendMakeMove(mahJongLogic.chi(mahJongState, selectedComboIndex, mahJongState.getPlayerIds()));
	 
  }
  public boolean ableToChi()
  {
	  List<Integer> ChiCombo=new ArrayList<Integer> ();
      
	  for (int i=0;i<selectedTile.size();i++)
	  {
		   
		   List<Integer> tileAtHand=mahJongState.getTilesAtHand(String.valueOf(idIndex(mahJongState.getPlayerIds(),turn)));
		   for (int j=0;j<tileAtHand.size();j++)
		   {
			 int CurrentIndex=tileAtHand.get(j);
			 if ((mahJongState.getTiles().get(CurrentIndex).get().toString()).equals(selectedTile.get(i).toString())==true)
			 {
			  
		       ChiCombo.add(CurrentIndex);
		       break;
			 }
		   }
	  }
	  
	  List<Integer> Used=mahJongState.getTilesUsed();
	  ChiCombo.add(Used.get(Used.size()-1));
	  
	  return Chi.chiCorrect(mahJongState, ChiCombo);
  }
  
  
  public void finishedSelectingTiles()
  {
	
	  if (mahJongMessage!=MahJongMessage.CHI)
	  {	  
		  tileDiscarded();
	  }
	  else
	  {
		  if (ableToChi()==true)
		  
		  {
		    tileChi();
		    
		  }

		  else
		  {
			  
		  }
		  
		  
	  }
  }
  
  
  private void sendInitialMove(List<String> playerIds) {
    container.sendMakeMove(mahJongLogic.getInitialMove(playerIds));
  }
}
