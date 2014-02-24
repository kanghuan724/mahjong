package edu.nyu.mahjong.logic;

import java.util.List;

import edu.nyu.mahjong.logic.*;
import org.cheat.client.GameApi.Container;
import org.cheat.client.GameApi.Operation;
import org.cheat.client.GameApi.SetTurn;
import org.cheat.client.GameApi.UpdateUI;

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
  enum MahJongMessage {
    INVISIBLE, HU, GANG, PENG, CHI;
  }

  private static final String WP = "WaitForPeng";
  private static final String WC = "WaitForChi";
  private static final String WG = "WaitForGang";
  private static final String WH = "WaitForHu";
	
  interface View {
    /**
     * Sets the presenter. The viewer will call certain methods on the presenter, e.g.,
     * when a tile is selected ({@link #tileSelected}),
     * when selection is done ({@link #tileDiscarded}), etc.
     *
     * The process of discarding a tile looks as follows to the viewer:
     * 1) The viewer calls {@link #tileSelected} (usually once) to select the tile to discard
     * 2) The viewer calls {@link #tileDiscarded} to finalize his selection
     * The process of discarding a tile looks as follows to the presenter:
     * 1) The presenter calls {@link #confirmSelection} and passes the current selection.
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
    		List<Tile> tilesAtDeclaredAcorss,
    		int numberOfTilesAtWall, List<Tile> tilesUsed,
            List<Tile> myTilesAtHand, List<Tile> myTilesDeclared,
            MahJongMessage mahJongMessage);

    /**
     * Asks the player to confirm the selection.
     * We pass what tile is selected (will be discarded), and what tiles will remain in the player hand.
     * The user can either select another tile (by calling {@link #tileSelected),
     * or discard the currently selected tile
     * (by calling {@link #tileDiscarded}; only allowed if selectedTile.size=1).
     * If the user selects a tile from remainingTiles, then it replaces selectedTile with that tile,
     * i.e. moves the previous selectedTile to remainingTiles.
     */
    void confirmSelection(Tile selectedTile, List<Tile> remainingTiles);
    
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

    /**
     * If more than one possible combo exist (except hu), the player needs to choose one.
     */
    void chooseCombo(List<List<Tile>> combo);
  }

  private final MahJongLogic mahJongLogic = new MahJongLogic();
  private final View view;
  private final Container container;
  /** A viewer doesn't have a playerId. */
  private int turn;
  private MahJongState mahJongState;
  private Tile selectedTile;

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
      if (mahJongState.getTurn() == yourPlayerId && playerIds.indexOf(yourPlayerId) == 0) {
        sendInitialMove(playerIds);
      }
      return;
    }
    for (Operation operation : updateUI.getLastMove()) {
      if (operation instanceof SetTurn) {
        turn = playerIds.indexOf(((SetTurn) operation).getPlayerId());
      }
    }
    mahJongState = mahJongLogic.gameApiStateToMahJongState(updateUI.getState(), yourPlayerId, playerIds);

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
    
    //if (isMyTurn()) {
    //  if (cheatState.isCheater()) {
    //    checkIfCheated();
    //  } else {
        // Choose the next card only if the game is not over
    //    if (numberOfOpponentCards > 0) {
    //      chooseNextCard();
    //    }
    //  }
    //}
  }

  private boolean canHu() {
    return true;
  }
  
  private boolean canGang() {
	    return true;
  }
  
  private boolean canPeng() {
	    return true;
  }
  
  private boolean canChi() {
	    return true;
  }
  private MahJongMessage getMahJongMessage() {
    switch (mahJongState.getMove().getName()) {
    case (WH):
    	if (canHu()) return MahJongMessage.HU;
    case (WG):
    	if (canGang()) return MahJongMessage.GANG;
    case (WP):
    	if (canPeng()) return MahJongMessage.PENG;
    case (WC):
    	if (canChi()) return MahJongMessage.CHI;
    }
    return MahJongMessage.INVISIBLE;
  }

  private boolean isMyTurn() {
    return turn == mahJongState.getTurn();
  }

  private List<Tile> getTiles(ImmutableList<Integer> targetIndices) {
	  List<Tile> targetTiles = Lists.newArrayList();
	  ImmutableList<Optional<Tile>> tiles = mahJongState.getTiles();
	  for (Integer tileIndex : targetIndices) {
		  targetTiles.add(tiles.get(tileIndex).get());
	  }
	  return targetTiles;	  
  }

  private void chooseNextCard() {
    view.chooseNextCard(
        ImmutableList.copyOf(selectedCards), cheatLogic.subtract(getMyCards(), selectedCards));
  }

  private void check(boolean val) {
    if (!val) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Adds/remove the card from the {@link #selectedCards}.
   * The view can only call this method if the presenter called {@link View#chooseNextCard}.
   */
  void cardSelected(Card card) {
    check(isMyTurn() && !cheatState.isCheater());
    if (selectedCards.contains(card)) {
      selectedCards.remove(card);
    } else if (!selectedCards.contains(card) && selectedCards.size() < 4) {
      selectedCards.add(card);
    }
    chooseNextCard();
  }

  /**
   * Finishes the card selection process.
   * The view can only call this method if the presenter called {@link View#chooseNextCard}
   * and more than one card was selected by calling {@link #cardSelected}.
   */
  void finishedSelectingCards() {
    check(isMyTurn() && !selectedCards.isEmpty());
    view.chooseRankForClaim(getPossibleRanks());
  }

  /**
   * Selects a rank and sends a claim.
   * The view can only call this method if the presenter called {@link View#chooseRankForClaim}.
   */
  void rankSelected(Rank rank) {
    check(isMyTurn() && !selectedCards.isEmpty() && getPossibleRanks().contains(rank));
    List<Integer> myCardIndices = cheatState.getWhiteOrBlack(cheatState.getTurn());
    List<Card> myCards = getMyCards();
    List<Integer> cardsToMoveToMiddle = Lists.newArrayList();
    for (Card card : selectedCards) {
      int cardIndex = myCardIndices.get(myCards.indexOf(card));
      cardsToMoveToMiddle.add(cardIndex);
    }
    container.sendMakeMove(cheatLogic.getMoveClaim(cheatState, rank, cardsToMoveToMiddle));
  }

  /**
   * Sends a move that the opponent is a cheater.
   * The view can only call this method if the presenter passed
   * CheaterMessage.IS_OPPONENT_CHEATING in {@link View#setPlayerState}.
   */
  void declaredCheater() {
    check(canDeclareCheater());
    container.sendMakeMove(cheatLogic.getMoveDeclareCheater(cheatState));
  }

  private void checkIfCheated() {
    container.sendMakeMove(cheatLogic.getMoveCheckIfCheated(cheatState));
  }

  private void sendInitialMove(List<Integer> playerIds) {
    container.sendMakeMove(cheatLogic.getMoveInitial(playerIds));
  }
}
