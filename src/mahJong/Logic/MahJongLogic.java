package mahJong;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import client.GameApi.Delete;
import client.GameApi.EndGame;
import client.GameApi.Operation;
import client.GameApi.Set;
import client.GameApi.SetTurn;
import client.GameApi.SetVisibility;
import client.GameApi.Shuffle;
import client.GameApi.VerifyMove;
import client.GameApi.VerifyMoveDone;
import client.Tile.Rank;
import client.Tile.Suit;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class MahJongLogic {
  /* The entries used in the MahJong game are:
   * turn, move, playerIds, tiles, tilesAtWall, tilesUsed,
 *** tilesAtHandOfOne, tilesAtHandOfTwo, tilesAtHandOfThree, tilesAtHandOfFour,
 *** tilesAtDeclaredOfOne, tilesAtDeclaredOfTwo, tilesAtDeclaredOfThree, tilesAtDeclaredOfFour,
 *** T1...T136
   * When we send operations on these keys, it will always be in the above order.
   */

  private static final String M = "move"; //
  private static final String PU = "PickUp"; //
  private static final String T = "T"; // Tile key (T0...T135)
  private static final String TAW = "tilesAtWall";
  private static final String TU = "tilesUsed";

  public VerifyMoveDone verify(VerifyMove verifyMove) {
    try {
      checkMoveIsLegal(verifyMove);
      return new VerifyMoveDone();
    } catch (Exception e) {
      return new VerifyMoveDone(verifyMove.getLastMovePlayerId(), e.getMessage());
    }
  }

  void checkMoveIsLegal(VerifyMove verifyMove) {
    List<Operation> lastMove = verifyMove.getLastMove();
    Map<String, Object> lastState = verifyMove.getLastState();
    // Checking the operations are as expected.
    List<Operation> expectedOperations = getExpectedOperations(
        lastState, lastMove, verifyMove.getPlayerIds(), verifyMove.getLastMovePlayerId());
    check(expectedOperations.equals(lastMove), expectedOperations, lastMove);
    // We use SetTurn, so we don't need to check that the correct player did the move.
    // However, we do need to check the first move is done by the [0] player (and then in the
    // first MakeMove we'll send SetTurn which will guarantee the correct player send MakeMove).
    if (lastState.isEmpty()) {
      check(verifyMove.getLastMovePlayerId() == verifyMove.getPlayerIds().get(0));
    }
  }


  /** Returns the operations for picking up a tile. */
  List<Operation> pickUp(MahJongState state, List<Integer> playerIds) {
    // picking up a tile
    check(state.getTilesAtWall().size() >= 1);
    int playerId = state.getTurn();

    List<Integer> lastAtWall = state.getTilesAtWall();
    List<Integer> newAtWall = lastAtWall.subList(1, lastAtWall.size()-1);
    List<Integer> lastAtHand = state.getTilesAtHand(playerId);
    List<Integer> newAtHand = concat(lastAtHand, lastAtWall.subList(0, 0));
    Integer tileIndex = lastAtWall.get(0);
    
    // 0) new SetTurn(0/1/2/3),
    // 1) new Set("move", "PickUp"),
    // 2) new Set("tilesAtWall", [...]),
    // 3) new Set("tilesAtHandOf1/2/3/4, [...]),
    // 4) new SetVisibility(T*, [1]/[2]/[3]/[4])
    List<Operation> expectedOperations = ImmutableList.<Operation>of(
    	new SetTurn(playerIds.indexOf(playerId) % 4 + 1),
    	new Set(M, PU),
    	new Set(TAW, newAtWall),
        new Set(getAtHandKey(playerId), newAtHand),
        new SetVisibility(T + tileIndex, ImmutableList.of(playerId)));
    return expectedOperations;
  }
  
  @SuppressWarnings("unchecked")
  List<Operation> getExpectedOperations(
      Map<String, Object> lastApiState, List<Operation> lastMove, List<Integer> playerIds,
      int lastMovePlayerId) {

    MahJongState lastState = gameApiStateToMahJongState(lastApiState, lastMovePlayerId, playerIds);
    return pickUp(lastState, playerIds);

  }

  <T> List<T> concat(List<T> a, List<T> b) {
    return Lists.newArrayList(Iterables.concat(a, b));
  }

  <T> List<T> subtract(List<T> removeFrom, List<T> elementsToRemove) {
    check(removeFrom.containsAll(elementsToRemove), removeFrom, elementsToRemove);
    List<T> result = Lists.newArrayList(removeFrom);
    result.removeAll(elementsToRemove);
    check(removeFrom.size() == result.size() + elementsToRemove.size());
    return result;
  }
  
  String getAtHandKey (int playerId) {
	  String index = null;
	  switch (playerId) {
	  case 0: 
		  index = "One";
		  break;
	  case 1:
		  index = "Two";
		  break;
	  case 2:
		  index = "Three";
		  break;
	  case 3:
		  index = "Four";
		  break;
	  }
	  return "tilesAtHandOf" + index;
  }
  
  String getAtDeclaredKey (int playerId) {
	  String index = null;
	  switch (playerId) {
	  case 0: 
		  index = "One";
		  break;
	  case 1:
		  index = "Two";
		  break;
	  case 2:
		  index = "Three";
		  break;
	  case 3:
		  index = "Four";
		  break;
	  }
	  return "tilesAtDeclaredOf" + index;
  }

  @SuppressWarnings({ "unchecked", "null" })
  private MahJongState gameApiStateToMahJongState(Map<String, Object> gameApiState,
      int turn, List<Integer> playerIds) {
    List<Optional<Tile>> tiles = Lists.newArrayList();
    for (int i = 0; i < 136; i++) {
      String tileString = (String) gameApiState.get(T + i);
      Tile tile;
      if (tileString == null) {
        tile = null;
      } else {
        Rank rank = Rank.fromRankString(tileString.substring(0, 1));
        Suit suit = Suit.fromFirstLetterLowerCase(tileString.substring(1));
        tile = new Tile(suit, rank);
      }
      tiles.add(Optional.fromNullable(tile));
    }
    List<Integer> tilesAtWall = (List<Integer>) gameApiState.get(TAW);
    List<Integer> tilesUsed = (List<Integer>) gameApiState.get(TU);
    List<Integer> atHand[] = null;
    List<Integer> atDeclared[] = null;
    for (int i = 0; i < 4; i++) {
    	atHand[i] = (List<Integer>) gameApiState.get(getAtHandKey(i));
    	atDeclared[i] = (List<Integer>) gameApiState.get(getAtDeclaredKey(i));
    }
    
    return new MahJongState(
        turn, "",
        ImmutableList.copyOf(playerIds),
        ImmutableList.copyOf(tiles),
        ImmutableList.copyOf(tilesAtWall), ImmutableList.copyOf(tilesUsed),
        ImmutableList.copyOf(atHand[0]), ImmutableList.copyOf(atDeclared[0]),
        ImmutableList.copyOf(atHand[1]), ImmutableList.copyOf(atDeclared[1]),
        ImmutableList.copyOf(atHand[2]), ImmutableList.copyOf(atDeclared[2]),
        ImmutableList.copyOf(atHand[3]), ImmutableList.copyOf(atDeclared[3]));
  }
  
  private void check(boolean val, Object... debugArguments) {
    if (!val) {
      throw new RuntimeException("We have a hacker! debugArguments="
          + Arrays.toString(debugArguments));
    }
  }
}