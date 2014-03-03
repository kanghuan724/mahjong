package org.mahjong.client;

import java.util.List;
import java.util.ArrayList;

import org.mahjong.client.*;

import org.mahjong.client.GameApi.Container;
import org.mahjong.client.GameApi.Operation;
import org.mahjong.client.GameApi.SetTurn;
import org.mahjong.client.GameApi.UpdateUI;

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
    INVISIBLE, HU, GANG, PENG, CHI, PICK,Discard, WaitForHu,WaitForChi,WaitForGang,WaitForPeng;
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
    void chooseTileToChi(List<Tile> selectedTile, List<Tile> remainingTiles);
    /**
     * Asks the player to hu.
     * We pass what tile is last discarded, and what tiles in the player hand.
     * The user can either hu (by calling {@link #hu), or cancel, 
     * only allowed if tileToHu and myTilesAtHand make a valid set).
     */
    void huAvailable(Tile tileToHu, List<Tile> myTilesAtHand);
    
    /**
     * Asks the player to gang.
     * We pass what tile is last discarded, and what tiles to gang with in the player hand.
     * The user can either gang (by calling {@link #gang), or cancel, 
     * only allowed if tileToGang and tilesToGang make a valid combo).
     */
    void gangAvailable(Tile tileToGang, List<Tile> tilesToGang);
    
    /**
     * Asks the player to peng.
     * We pass what tile is last discarded, and what tiles to peng with in the player hand.
     * The user can either peng (by calling {@link #peng), or cancel, 
     * only allowed if tileToPeng and tilesToPeng make a valid combo).
     */
    void pengAvailable(Tile tileToPeng, List<Tile> tilesToPeng);
    
    /**
     * Asks the player to chi.
     * We pass what tile is last discarded, and what tiles to chi with in the player hand.
     * The user can either chi (by calling {@link #chi), or cancel, 
     * only allowed if tileToChi and tilesToChi make a valid combo).
     */
    void chiAvailable(Tile tileToChi, List<Tile> tilesToChi);
    void discard();
    /**
     * If more than one possible combo exist (except hu), the player needs to choose one.
     */
    //void chooseCombo(List<List<Tile>> combo);
  }

  private final MahJongLogic mahJongLogic = new MahJongLogic();
  private final View view;
  private final Container container;
  /** A viewer doesn't have a playerId. */
  private int turn;
  private MahJongState mahJongState;
  //private Tile selectedTile;
  private List<Tile> selectedTile;
  private boolean chi;
  private Tile lastUsedTile;
  private List<Tile> selectedCombo;

  public MahJongPresenter(View view, Container container) {
    this.view = view;
    this.container = container;
    view.setPresenter(this);
  }

  /** Updates the presenter and the view with the state in updateUI. */
  public void updateUI(UpdateUI updateUI) {
    List<Integer> playerIds = updateUI.getPlayerIds();
    int yourPlayerId = updateUI.getYourPlayerId();
    int yourPlayerIndex = updateUI.getPlayerIndex(yourPlayerId);
    if (updateUI.getState().isEmpty()) {
      // The 0 player sends the initial setup move.


      if (playerIds.indexOf(yourPlayerId) == 0) {

        sendInitialMove(playerIds);
      }
      return;
    }
    mahJongState = mahJongLogic.gameApiStateToMahJongState(updateUI.getState(), yourPlayerId, playerIds);
    for (Operation operation : updateUI.getLastMove()) {
      if (operation instanceof SetTurn) {
        turn = playerIds.indexOf(((SetTurn) operation).getPlayerId());
      }
    }
    chi=false;

    MahJongMessage mahJongMessage = getMahJongMessage();
    if (updateUI.isViewer()) {
      view.setViewerState(mahJongState.getTilesAtHand(playerIds.get(0)).size(), 
    		  mahJongState.getTilesAtHand(playerIds.get(1)).size(), 
    		  mahJongState.getTilesAtHand(playerIds.get(2)).size(),
    		  mahJongState.getTilesAtHand(playerIds.get(3)).size(),
    		  getTiles(mahJongState.getTilesAtDeclared(playerIds.get(0))),
    		  getTiles(mahJongState.getTilesAtDeclared(playerIds.get(1))),
    		  getTiles(mahJongState.getTilesAtDeclared(playerIds.get(2))),
    		  getTiles(mahJongState.getTilesAtDeclared(playerIds.get(3))),
    		  mahJongState.getTilesAtWall().size(), getTiles(mahJongState.getTilesUsed()),
    		  mahJongMessage);
      return;
    }
    
    if (updateUI.isAiPlayer()) {
      // TODO: implement AI in a later HW!
      //container.sendMakeMove(..);
      return;
    }
    // Must be a player!
    int rightId = MahJongLogic.nextId(yourPlayerId, playerIds);
    int acrossId = MahJongLogic.nextId(rightId, playerIds);
    int leftId = MahJongLogic.nextId(acrossId, playerIds);
    int numberOfTilesAtHandLeft = mahJongState.getTilesAtHand(leftId).size();
    int numberOfTilesAtHandAcross = mahJongState.getTilesAtHand(acrossId).size();
    int numberOfTilesAtHandRight = mahJongState.getTilesAtHand(rightId).size();
    List<Tile> tilesAtDeclaredLeft = getTiles(mahJongState.getTilesAtDeclared(leftId));
    List<Tile> tilesAtDeclaredAcross = getTiles(mahJongState.getTilesAtDeclared(acrossId));
    List<Tile> tilesAtDeclaredRight = getTiles(mahJongState.getTilesAtDeclared(rightId));
    view.setPlayerState(numberOfTilesAtHandLeft, numberOfTilesAtHandRight, numberOfTilesAtHandAcross,
    		tilesAtDeclaredLeft, tilesAtDeclaredRight, tilesAtDeclaredAcross, 
    		mahJongState.getTilesAtWall().size(), getTiles(mahJongState.getTilesUsed()),
    		getTiles(mahJongState.getTilesAtHand(yourPlayerId)), 
    		getTiles(mahJongState.getTilesAtDeclared(yourPlayerId)),
    		getMahJongMessage());

    // TODO: implement main logic of updateUI
    if (getMahJongMessage()==MahJongMessage.CHI)
    	chi=true;
    if (isMyTurn()) {
    	if (chi==true)
    	{
    		chooseTileToChi();
    	}
        if (mahJongMessage==MahJongMessage.Discard) {
      	  chooseTile();
        }
        if (mahJongMessage==MahJongMessage.WaitForHu)
        	waitForHu();
        if (mahJongMessage==MahJongMessage.WaitForGang)
        	waitForGang();
        if (mahJongMessage==MahJongMessage.WaitForPeng)
        	waitForPeng();
        if (mahJongMessage==MahJongMessage.WaitForChi)
        	waitForChi();
      }

    
    
  }

  private boolean canHu() {
    return false;
  }
  
  private boolean canGang() {
	    return false;
  }
  
  private boolean canPeng() {
	    return false;
  }
  
  private boolean canChi() {
	    return false;
  }
  private MahJongMessage getMahJongMessage() {
	if (mahJongState.getMove()==null)
		return MahJongMessage.PICK;
    switch (mahJongState.getMove().getName()) {
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
    case (RH):
    {     RefuseHu move=(RefuseHu)mahJongState.getMove();
          if (move.getSource()==mahJongState.getTurn())
    	    return MahJongMessage.WaitForGang;
          else
        	return MahJongMessage.HU;
    }
    case (RG):
    {     RefuseGang move=(RefuseGang)mahJongState.getMove();
          if (move.getSource()==mahJongState.getTurn())
    	    return MahJongMessage.WaitForPeng;
          else
        	return MahJongMessage.GANG;
    }
    case (RP):
    {     RefusePeng move=(RefusePeng)mahJongState.getMove();
          if (move.getSource()==mahJongState.getTurn())
    	    return MahJongMessage.WaitForChi;
          else
        	return MahJongMessage.PENG;
    }
    case (RC):
    {
    	return MahJongMessage.PICK;
    }
    case (WG):
    	//if (canGang()) 
    	return MahJongMessage.GANG;
    case (WP):
    	//if (canPeng()) 
    	return MahJongMessage.PENG;
    case (WC):
    	//if (canChi()) 
    	return MahJongMessage.CHI;
    }
    return MahJongMessage.INVISIBLE;
  }

  private boolean isMyTurn() {
    return turn == mahJongState.getTurn();
  }
  /*public void rankSelected(Rank rank) {
	    check(isMyTurn() && !selectedCards.isEmpty() && getPossibleRanks().contains(rank));
	    List<Integer> myCardIndices = cheatState.getWhiteOrBlack(cheatState.getTurn());
	    List<Card> myCards = getMyCards();
	    List<Integer> cardsToMoveToMiddle = Lists.newArrayList();
	    for (Card card : selectedCards) {
	      int cardIndex = myCardIndices.get(myCards.indexOf(card));
	      cardsToMoveToMiddle.add(cardIndex);
	    }
	    container.sendMakeMove(cheatLogic.getMoveClaim(cheatState, rank, cardsToMoveToMiddle));
	  }*/
  public void pickUpTile()
  {
	  //check
	  container.sendMakeMove(mahJongLogic.pickUp(mahJongState,  mahJongState.getPlayerIds())); 
  }
  private List<Tile> getTiles(ImmutableList<Integer> targetIndices) {
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
  
  private void chooseTile() {
    view.chooseTile(selectedTile, 
    		mahJongLogic.subtract(getTiles(mahJongState.getTilesAtHand(turn)), selectedTile));
  }
  private void chooseTileToChi() {
	    view.chooseTileToChi(selectedTile, 
	    		mahJongLogic.subtract(getTiles(mahJongState.getTilesAtHand(turn)), selectedTile));
	  }
  /**
   * Add/remove the tile from the {@link #selectedTile}.
   * The view can only call this method if the presenter called {@link View#chooseTile}.
   */
  public void tileSelected(Tile tile) {
    //check(isMyTurn());
    if (selectedTile.contains(tile)) {
    	selectedTile.remove(tile) ;
    } 
    else 
    {
      if (chi==false)
      {
    	if (selectedTile.size()<1)
        selectedTile .add(tile);
      }
      if (chi==true)
      {
    	  if (selectedTile.size()<2)
    		  selectedTile.add(tile);
      }
    }
    chooseTile();
  }
  
  /**
   * Finishes the tile selection process.
   * The view can only call this method if the presenter called {@link View#chooseTile}
   * and exactly one tile was selected by calling {@link #tileSelected}.
   */
  //ToDo: Can't use tile as parameter, should give the index.
  void tileDiscarded() {
    check(isMyTurn() && selectedTile != null);
    List<Integer> selectedTileIndex = Lists.newArrayList();
    int playerId=mahJongState.getTurn();
    List<Integer> atHand=mahJongState.getTilesAtHand(playerId);
    for (int index = 0; index < atHand.size(); index++) {
    	if ((mahJongState.getTiles().get(atHand.get(index)).get().equals(selectedTile.get(0)))) {
    		selectedTileIndex.add(atHand.get(index));
    		break;
    	}
    }
    container.sendMakeMove(mahJongLogic.discard(mahJongState, selectedTileIndex, mahJongState.getPlayerIds()));
  }
  void tileChi() {
	    check(isMyTurn() && selectedTile != null);
	    List<Integer> selectedTileIndex = Lists.newArrayList();
	    int playerId=mahJongState.getTurn();
	    List<Integer> atHand=mahJongState.getTilesAtHand(playerId);
	    for (int index = 0; index < atHand.size(); index++) {
	    	if ((mahJongState.getTiles().get(atHand.get(index)).get().equals(selectedTile.get(0)))) {
	    		selectedTileIndex.add(atHand.get(index));
	    		break;
	    	}
	    }
	    List<Integer> Used=mahJongState.getTilesUsed();
	    selectedTileIndex.add(Used.size()-1);
	    container.sendMakeMove(mahJongLogic.chi(mahJongState, selectedTileIndex, mahJongState.getPlayerIds()));
	  }
  

  void huAvailable(Tile tileToHu, List<Tile> myTilesAtHand) {
	    view.huAvailable(lastUsedTile, getTiles(mahJongState.getTilesAtHand(turn)));
  }
  
  void gangAvailable(Tile tileToGang, List<Tile> tilesToHu) {
	    view.gangAvailable(lastUsedTile, getTiles(mahJongState.getTilesAtHand(turn)));
  }
  
  void pengAvailable(Tile tile, List<Tile> tilesToPeng) {
	    view.pengAvailable(lastUsedTile, getTiles(mahJongState.getTilesAtHand(turn)));
  }
  
  void chiAvailable(Tile tile, List<Tile> tilesToChi) {
	    view.chiAvailable(lastUsedTile, getTiles(mahJongState.getTilesAtHand(turn)));
  }

  /**
   * Sends a move of hu/gang/peng/chi.
   * The view can only call this method if the presenter passed
   * CheaterMessage.HU/GANG/PENG/CHI in {@link View#setPlayerState}.
   */
  void hu(List<Operation> lastMove) {
    container.sendMakeMove(mahJongLogic.hu(mahJongState, lastMove, mahJongState.getPlayerIds()));
  }
  void waitForHu() {
	    container.sendMakeMove(mahJongLogic.WaitForHu(mahJongState,  mahJongState.getPlayerIds()));
	  }
  void waitForGang() {
	    container.sendMakeMove(mahJongLogic.WaitForGang(mahJongState,  mahJongState.getPlayerIds()));
	  }
  void waitForPeng() {
	    container.sendMakeMove(mahJongLogic.WaitForPeng(mahJongState,  mahJongState.getPlayerIds()));
	  }
  void waitForChi() {
	    container.sendMakeMove(mahJongLogic.WaitForChi(mahJongState,  mahJongState.getPlayerIds()));
	  }
  public List<Integer> gangHelper()
  {
      List<Integer> selectedComboIndex = Lists.newArrayList();
	  
	  int playerId = mahJongState.getTurn();
	  List<Integer> lastAtHand = mahJongState.getTilesAtHand(playerId);
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
  public List<Integer> pengHelper()
  {
      List<Integer> selectedComboIndex = Lists.newArrayList();	  
	  int playerId = mahJongState.getTurn();
	  List<Integer> lastAtHand = mahJongState.getTilesAtHand(playerId);
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
  public void gang(List<Integer> selectedComboIndex) {
	  
	    container.sendMakeMove(mahJongLogic.gang(mahJongState, selectedComboIndex, mahJongState.getPlayerIds()));
  }
  public void refusegang() {
	  
	    container.sendMakeMove(mahJongLogic.refusegang(mahJongState,  mahJongState.getPlayerIds()));
}
  public void refusepeng() {
	  
	    container.sendMakeMove(mahJongLogic.refusepeng(mahJongState,  mahJongState.getPlayerIds()));
}
  public void refusehu() {
	  
	    container.sendMakeMove(mahJongLogic.refusehu(mahJongState,  mahJongState.getPlayerIds()));
}
  public void refusechi() {
	  
	    container.sendMakeMove(mahJongLogic.refusechi(mahJongState,  mahJongState.getPlayerIds()));
}
  public void peng(List<Integer> selectedComboIndex) {
	 
	  container.sendMakeMove(mahJongLogic.peng(mahJongState, selectedComboIndex, mahJongState.getPlayerIds()));
  }
  public boolean ableToChi()
  {
	  List<Integer> ChiCombo=new ArrayList<Integer> ();
	  for (int i=0;i<selectedTile.size();i++)
	  {
		   List<Integer> tileAtHand=mahJongState.getTilesAtHand(turn);
		   for (int j=0;i<tileAtHand.size();j++)
		   {
			 int CurrentIndex=tileAtHand.get(j);
			 if (mahJongState.getTiles().get(CurrentIndex).equals(selectedTile.get(i)));
		     ChiCombo.add(CurrentIndex);
		   }
	  }
	  List<Integer> Used=mahJongState.getTilesUsed();
	  ChiCombo.add(Used.get(Used.size()-1));
	  return Chi.chiCorrect(mahJongState, ChiCombo);
  }
  
  /*void chi() {
	  List<Integer> selectedComboIndex = Lists.newArrayList();
	  for (int index = 0; index < mahJongState.getTiles().size(); index++) {
		  for (int cI = 0; cI < selectedCombo.size(); cI++) {
			  if ((mahJongState.getTiles().get(index).get().equals(selectedCombo.get(cI)))) {
				  selectedComboIndex.add(index);
			  }
		  }
	  }
	  container.sendMakeMove(mahJongLogic.chi(mahJongState, selectedComboIndex, mahJongState.getPlayerIds()));
  }*/
  public void finishedSelectingTiles()
  {
	 //check......
	  if (chi==false)
	  {	  
		  tileDiscarded();
	  }
	  else
	  {
		  if (ableToChi())
		    tileChi();
		  //ToDo: Maybe Need to add a new logic FailToChi in Logic
		  //If that chi is invalid, the player will return to the previous mode that 
		  //asks whether the player is gonna chi
		  else
		  {
			  
		  }
		  
		  
	  }
  }
  
  
  private void sendInitialMove(List<Integer> playerIds) {
    container.sendMakeMove(mahJongLogic.getInitialMove(playerIds));
  }
}
