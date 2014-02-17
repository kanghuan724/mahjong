package edu.nyu.mahjong.logic;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


import org.cheat.client.GameApi.*;
import edu.nyu.mahjong.iface.*;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class MahJongLogic {
	/*
	 * The entries used in the MahJong game are: turn, move, playerIds, tiles,
	 * tilesAtWall, tilesUsed,** tilesAtHandOfOne, tilesAtHandOfTwo,
	 * tilesAtHandOfThree, tilesAtHandOfFour,** tilesAtDeclaredOfOne,
	 * tilesAtDeclaredOfTwo, tilesAtDeclaredOfThree, tilesAtDeclaredOfFour,**
	 * T1...T136 When we send operations on these keys, it will always be in the
	 * above order.
	 */

	private static final String M = "move";
	private static final String PU = "PickUp";
	private static final String D = "Discard";
	private static final String P = "Peng";
	private static final String C = "Chi";
	private static final String G=  "Gang";
	private static final String RP="RefusePeng";
	private static final String RC="RefuseChi";
	private static final String RG="RefuseGang";
	private static final String T = "T"; // Tile key (T0...T135)
	private static final String TAW = "tilesAtWall";
	private static final String TU = "tilesUsed";

	public VerifyMoveDone verify(VerifyMove verifyMove) {
		try {
			checkMoveIsLegal(verifyMove);
			return new VerifyMoveDone();
		} catch (Exception e) {
			return new VerifyMoveDone(verifyMove.getLastMovePlayerId(),
					e.getMessage());
		}
	}

	void checkMoveIsLegal(VerifyMove verifyMove) {
		List<Operation> lastMove = verifyMove.getLastMove();
		Map<String, Object> lastState = verifyMove.getLastState();
		// Checking the operations are as expected.
		List<Operation> expectedOperations = getExpectedOperations(lastState,
				lastMove, verifyMove.getPlayerIds(),
				verifyMove.getLastMovePlayerId());
		check(expectedOperations.equals(lastMove), expectedOperations, lastMove);
		// We use SetTurn, so we don't need to check that the correct player did
		// the move.
		// However, we do need to check the first move is done by the [0] player
		// (and then in the
		// first MakeMove we'll send SetTurn which will guarantee the correct
		// player send MakeMove).
		if (lastState.isEmpty()) {
			check(verifyMove.getLastMovePlayerId() == verifyMove.getPlayerIds()
					.get(0));
		}
	}

	List<Operation> getInitialMove(List<Integer> playerIds) {
		List<Operation> operations = Lists.newArrayList();
        //operations.add(new SetTurn(0));
		operations.add(new SetTurn(playerIds.get(0)));
		operations.add(new Set(M, null));
		operations.add(new Set(TAW, getIndicesInRange(0, 135)));
		operations.add(new Set(TU, null));
		// set hands
		for (int i = 0; i < 4; i++) {
			operations.add(new Set(getAtHandKey(i), getIndicesInRange(
					13 * i , 13 * i +12)));
		}
		operations.add(new Set(TAW, getIndicesInRange(52, 135)));
		// sets all 136 tiles
		for (int i = 0; i < 136; i++) {
			operations.add(new Set(T + i, tileIdToString(i)));
		}
		// shuffle(T0,...,T135)
		operations.add(new Shuffle(getTilesInRange(0, 135)));
		// sets visibility
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				operations.add(new SetVisibility(T + (i * 13 + j),
						//ImmutableList.of(i)));
						ImmutableList.of(playerIds.get(i))));
			}
		}
		//set the visibility of middle tile to null
		for (int i=52;i<136;i++)
			operations.add(new SetVisibility(T + i ,ImmutableList.of()));
		operations.add(new Set(M,ImmutableList.<String>of(P)));
		return operations;
	}

	/** Returns the operations for picking up a tile. */
	List<Operation> pickUp(MahJongState state, List<Integer> playerIds) {
		// picking up a tile
		check(state.getTilesAtWall().size() >= 1);
		int playerId = state.getTurn();

		List<Integer> lastAtWall = state.getTilesAtWall();
		List<Integer> newAtWall = lastAtWall.subList(1, lastAtWall.size() - 1);
		List<Integer> lastAtHand = state.getTilesAtHand(playerId);
		List<Integer> newAtHand = concat(lastAtHand, lastAtWall.subList(0, 0));
		//Integer tileIndex = lastAtWall.get(0);
        int tileIndex=lastAtWall.get(0);
		// 0) new SetTurn(PlayerId),
		// 1) new Set("Move", "PickUp"),
		// 2) new Set("tilesAtWall", [...]),
		// 3) new Set("tilesAtHandOf1/2/3/4, [...]),
		// 4) new SetVisibility(T*, [1]/[2]/[3]/[4])
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(playerId), new Set(M, ImmutableList.<String>of(PU)),
				new Set(TAW, newAtWall), new Set(getAtHandKey(playerId),
						newAtHand), 
			  //new SetVisibility(T + tileIndex,ImmutableList.of(playerId)
				new SetVisibility(T + tileIndex,ImmutableList.of(playerId))
						);
		return expectedOperations;
	}

	/** Returns the operations for discarding a tile. */
	List<Operation> discard(MahJongState state, //List<Integer> tilesToDiscard,
			List<Operation> lastMove,
			List<Integer> playerIds) {
		// discarding up a tile
		int playerId = state.getTurn();
		check(state.getTilesAtHand(playerId).size() >= 1);
		List<Integer> lastAtHand = state.getTilesAtHand(playerId);
		Set newAtHand=(Set)lastMove.get(3);
		List<Integer> newAtHandTile=(List<Integer>)newAtHand.getValue();
		List<Integer> tilesToDiscard=subtract(lastAtHand,newAtHandTile);
		check (tilesToDiscard.size()==1);
		List<Integer> lastUsed = state.getTilesUsed();
		List<Integer> newUsed = concat(lastUsed, tilesToDiscard);
		
		//List<Integer> newAtHand = subtract(lastAtHand, tilesToDiscard);
		
		
		Integer tileIndex = tilesToDiscard.get(0);
         Optional<Tile> discardTile=state.getTiles().get(tileIndex);
		// 0) new SetTurn(0/1/2/3),
		// 1) new Set("move", "Discard"),
		// 2) new Set("tilesUsed", [...]),
		// 3) new Set("tilesAtHandOf1/2/3/4, [...]),
		// 4) new SetVisibility(T*, null)
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				//new SetTurn(playerIds.indexOf(playerId) % 4 + 1),
				new SetTurn(nextId(playerId,playerIds)),
				// ?HOW TO SetTurn WHEN A CHI/PENG MAY HAPPEN?
				new Set(M, ImmutableList.<String>of(D,discardTile.get().toString())), new Set(TU, newUsed), new Set(
						getAtHandKey(playerId), newAtHand), new SetVisibility(T
						+ tileIndex));
		return expectedOperations;
	}

	List<Operation> chi(MahJongState state, List<Operation> lastMove,
			List<Integer> playerIds) {
		// chi a tile with two tiles at hand
		int playerId = state.getTurn();
		check(state.getTilesAtHand(playerId).size() >= 4);
        check(state.getTilesUsed().size()>=1);
		List<Integer> lastUsed = state.getTilesUsed();
		List<Integer> newUsed = lastUsed.subList(0, lastUsed.size()-2);
		List<Integer> tileToChi=lastUsed.subList(lastUsed.size()-1,lastUsed.size()-1);
		/*if (lastUsed.size() > 1) {
			newUsed = lastUsed.subList(0, lastUsed.size() - 2);
		}*/
		Integer tileIndex=tileToChi.get(0);
		Optional<Tile> chiTile=state.getTiles().get(tileIndex);
		List<Integer> lastAtHand = state.getTilesAtHand(playerId);
		List<Integer> newAtHand =  (List<Integer>)((Set)lastMove.get(3)).getValue();
		List<Integer> tilesToChi=subtract(lastAtHand,newAtHand);
		//List<Integer> newAtHand = subtract(lastAtHand, tilesToChi);
		List<Integer> lastAtDeclared = state.getTilesAtDeclared(playerId);
		List<Integer> chiCombo=concat(tileToChi,tilesToChi);
		check(chiCombo.size()==3);
		//List<Integer> newAtDeclared = concat(lastAtDeclared,
		//		lastUsed.subList(lastUsed.size() - 1, lastUsed.size() - 1));
		List<Integer> newAtDeclared=concat(lastAtDeclared,chiCombo);
		newAtDeclared = concat(newAtDeclared, tilesToChi);
		//Integer tileIndex[] = new Integer[3];
		//tileIndex[0] = lastUsed.get(lastUsed.size() - 1);
		//tileIndex[1] = tilesToChi.get(0);
		//tileIndex[2] = tilesToChi.get(1);
        
		// 0) new SetTurn(0/1/2/3),
		// 1) new Set("move", "Chi"),
		// 2) new Set("tilesUsed", [...]),
		// 3) new Set("tilesAtHandOf1/2/3/4, [...]),
		// 4) new Set("tilesAtDeclaredOf1/2/3/4, [...]),
		// 5) new SetVisibility(T*, null)
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(playerIds.indexOf(playerId)), new Set(M, ImmutableList.<String>of(C,chiTile.get().toString())),
				new Set(TU, newUsed),
				new Set(getAtHandKey(playerId), newAtHand), new Set(
						getAtDeclaredKey(playerId), newAtDeclared),
				new SetVisibility(T + chiCombo.get(0)), new SetVisibility(T
						+ chiCombo.get(1)), new SetVisibility(T + chiCombo.get(2)));
		return expectedOperations;
	}
	List<Operation> refusechi(MahJongState state,List<Operation> lastMove, List<Integer> playerIds)
	{
		int playerId=state.getTurn();
		List<Operation> expectedOperations=ImmutableList.<Operation>of(
				new SetTurn(playerId),
				new Set(M,ImmutableList.<String>of(RC)));
		return expectedOperations;
	}
	List<Operation> peng(MahJongState state, List<Operation> lastMove,
			List<Integer> playerIds) {
		// peng a tile with two tiles at hand
		int playerId = state.getTurn();
		check(state.getTilesAtHand(playerId).size() >= 4);
        check(state.getTilesUsed().size()>=1);
		List<Integer> lastUsed = state.getTilesUsed();
		List<Integer> newUsed = lastUsed.subList(0, lastUsed.size()-2);
		List<Integer> tileToPeng=lastUsed.subList(lastUsed.size()-1,lastUsed.size()-1);
		/*if (lastUsed.size() > 1) {
			newUsed = lastUsed.subList(0, lastUsed.size() - 2);
		}*/
		Integer tileIndex=tileToPeng.get(0);
		Optional<Tile> tilePeng=state.getTiles().get(tileIndex);
		List<Integer> lastAtHand = state.getTilesAtHand(playerId);
		//List<Integer> newAtHand = subtract(lastAtHand, tilesToPeng);
		List<Integer> newAtHand= (List<Integer>)((Set)lastMove.get(3)).getValue();
		List<Integer> tilesToPeng=subtract(lastAtHand,newAtHand);
		List<Integer> lastAtDeclared = state.getTilesAtDeclared(playerId);
		List<Integer> PengCombo=concat(tileToPeng,tilesToPeng);
		check(PengCombo.size()==3);
		//List<Integer> newAtDeclared = concat(lastAtDeclared,
		//		lastUsed.subList(lastUsed.size() - 1, lastUsed.size() - 1));
		List<Integer> newAtDeclared=concat(lastAtDeclared,PengCombo);
		

		// 0) new SetTurn(0/1/2/3),
		// 1) new Set("move", "Peng"),
		// 2) new Set("tilesUsed", [...]),
		// 3) new Set("tilesAtHandOf1/2/3/4, [...]),
		// 4) new Set("tilesAtDeclaredOf1/2/3/4, [...]),
		// 5) new SetVisibility(T*, null)
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(playerIds.indexOf(playerId)), new Set(M, ImmutableList.<String>of(P,tilePeng.get().toString())),
				new Set(TU, newUsed),
				new Set(getAtHandKey(playerId), newAtHand), new Set(
						getAtDeclaredKey(playerId), newAtDeclared),
				new SetVisibility(T + PengCombo.get(0)), new SetVisibility(T
						+ PengCombo.get(1)), new SetVisibility(T + PengCombo.get(2)));
		return expectedOperations;
	}
	List<Operation> refusepeng(MahJongState state,List<Operation> lastMove, List<Integer> playerIds)
	{
		
		int playerId=state.getTurn();
		List<Operation> expectedOperations=ImmutableList.<Operation>of(
				new SetTurn(nextId(playerId,playerIds)),
				new Set(M,ImmutableList.<String>of(RP)));
		return expectedOperations;
	}
		
	List<Operation> refusegang(MahJongState state,List<Operation> lastMove, List<Integer> playerIds)
	{
		int playerId=state.getTurn();
		List<Operation> expectedOperations=ImmutableList.<Operation>of(
				new SetTurn(nextId(playerId,playerIds)),
				new Set(M,ImmutableList.<String>of(RG)));
		return expectedOperations;
	}
	
	List<Operation> gang(MahJongState state, List<Operation> lastMove,
			List<Integer> playerIds) {
		// peng a tile with two tiles at hand
		int playerId = state.getTurn();
		check(state.getTilesAtHand(playerId).size() >= 4);
        check(state.getTilesUsed().size()>=1);
		List<Integer> lastUsed = state.getTilesUsed();
		List<Integer> newUsed = lastUsed.subList(0, lastUsed.size()-2);
		List<Integer> tileToGang=lastUsed.subList(lastUsed.size()-1,lastUsed.size()-1);
		/*if (lastUsed.size() > 1) {
			newUsed = lastUsed.subList(0, lastUsed.size() - 2);
		}*/
		Integer tileIndex=tileToGang.get(0);
		Optional<Tile> gangTile=state.getTiles().get(tileIndex);
		List<Integer> lastAtHand = state.getTilesAtHand(playerId);
		//List<Integer> newAtHand = subtract(lastAtHand, tilesToPeng);
		List<Integer> newAtHand= (List<Integer>)((Set)lastMove.get(3)).getValue();
		List<Integer> tilesToGang=subtract(lastAtHand,newAtHand);
		List<Integer> lastAtDeclared = state.getTilesAtDeclared(playerId);
		List<Integer> GangCombo=concat(tileToGang,tilesToGang);
		check (GangCombo.size()==4);
		//List<Integer> newAtDeclared = concat(lastAtDeclared,
		//		lastUsed.subList(lastUsed.size() - 1, lastUsed.size() - 1));
		List<Integer> newAtDeclared=concat(lastAtDeclared,GangCombo);
		/*Integer tileIndex[] = new Integer[3];
		tileIndex[0] = lastUsed.get(lastUsed.size() - 1);
		tileIndex[1] = tilesToPeng.get(0);
		tileIndex[2] = tilesToPeng.get(1);*/

		// 0) new SetTurn(0/1/2/3),
		// 1) new Set("move", "Gang"),
		// 2) new Set("tilesUsed", [...]),
		// 3) new Set("tilesAtHandOf1/2/3/4, [...]),
		// 4) new Set("tilesAtDeclaredOf1/2/3/4, [...]),
		// 5) new SetVisibility(T*, null)
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(playerIds.indexOf(playerId)), new Set(M, ImmutableList.<String>of(G,gangTile.get().toString())),
				new Set(TU, newUsed),
				new Set(getAtHandKey(playerId), newAtHand), new Set(
						getAtDeclaredKey(playerId), newAtDeclared),
				new SetVisibility(T + GangCombo.get(0)), new SetVisibility(T
						+ GangCombo.get(1)), new SetVisibility(T + GangCombo.get(2),new SetVisibility(T+GangCombo.get(3))));
		return expectedOperations;
	}
	// getExpectedOperations need to be developed
	List<Operation> getExpectedOperations(Map<String, Object> lastApiState,
			List<Operation> lastMove, List<Integer> playerIds,
			int lastMovePlayerId) {
		if (lastApiState.isEmpty()) {
		      return getInitialMove(playerIds);
		    }
		MahJongState lastState = gameApiStateToMahJongState(lastApiState,
				lastMovePlayerId, playerIds);
		
		if (lastState.getMove().getName())
		  return pickUp(lastState, playerIds);
		if (lastMove.contains(new Set(M,D)))
		  return discard(lastState,lastMove,playerIds);
		if (lastMove.contains(new Set(M,C)))
		  return chi(lastState,lastMove,playerIds);
		if (lastMove.contains(new Set(M,P)))
		  return peng(lastState,lastMove,playerIds);
		if (lastMove.contains(new Set(M,G)))
		  return gang(lastState,lastMove,playerIds);
		return null;

	}

	String tileIdToString(int tileId) {
		checkArgument(tileId >= 0 && tileId < 136);
		/*
		 * Tile 0-35 are ACHARACTERS
		 * Tile 0-8 are ACHARACTER 1-9 Tile 9-17 are ACHARACTER 1-9 Tile 18-26 are ACHARACTER 1-9 Tile 27-35 are ACHARACTER 1-9 
		 * Tile 36-71 are Bamboos
		 * TIle 72-107 are Circles
		 * Tile 108-135 are other categories
		 */
		int rank;
		int suit;
		/*if (tileId < 108) {
			rank = (tileId / 12 + 1);
			if (tileId % 3 < 4) {
				suit = 0;
			} else if (tileId % 3 < 8) {
				suit = 1;
			} else {
				suit = 2;
			}
		}*/
		if (tileId<108)
		{
			suit=tileId/36;
			rank=tileId%9+1;
			
		}
		else {
			rank = 0;
			suit = tileId / 4 - 24;
		}
		String rankString = Rank.values()[rank].getRankString();
		String suitString = Suit.values()[suit].getFirstLetterLowerCase();
		//return rankString + suitString;
		return suitString+rankString;
	}

	<T> List<T> concat(List<T> a, List<T> b) {
		return Lists.newArrayList(Iterables.concat(a, b));
	}

	<T> List<T> subtract(List<T> removeFrom, List<T> elementsToRemove) {
		check(removeFrom.containsAll(elementsToRemove), removeFrom,
				elementsToRemove);
		List<T> result = Lists.newArrayList(removeFrom);
		result.removeAll(elementsToRemove);
		check(removeFrom.size() == result.size() + elementsToRemove.size());
		return result;
	}

	List<Integer> getIndicesInRange(int fromInclusive, int toInclusive) {
		List<Integer> keys = Lists.newArrayList();
		for (int i = fromInclusive; i <= toInclusive; i++) {
			keys.add(i);
		}
		return keys;
	}

	List<String> getTilesInRange(int fromInclusive, int toInclusive) {
		List<String> keys = Lists.newArrayList();
		for (int i = fromInclusive; i <= toInclusive; i++) {
			keys.add(T + i);
		}
		return keys;
	}

	String getAtHandKey(int playerId) {
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

	String getAtDeclaredKey(int playerId) {
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
	private MahJongState gameApiStateToMahJongState(
		Map<String, Object> gameApiState, int turn, List<Integer> playerIds) {
		List<Optional<Tile>> tiles = Lists.newArrayList();
		for (int i = 0; i < 136; i++) {
			String tileString = (String) gameApiState.get(T + i);
			Tile tile;
			if (tileString == null) {
				tile = null;
			} else {
				/*Rank rank = Rank.fromRankString(tileString.substring(0, 1));
				Suit suit = Suit.fromFirstLetterLowerCase(tileString
						.substring(1));*/
				Suit suit=Suit.fromFirstLetterLowerCase(tileString.substring(0, 1));
				Rank rank=Rank.fromRankString(tileString.substring(1));
				tile = new Tile(suit, rank);
			}
			tiles.add(Optional.fromNullable(tile));
		}
		List<Integer> tilesAtWall = (List<Integer>) gameApiState.get(TAW);
		List<Integer> tilesUsed = (List<Integer>) gameApiState.get(TU);
		List<List<Integer>> atHand = new ArrayList<List<Integer>> ();
		List<List<Integer>> atDeclared = new ArrayList<List<Integer>> ();
		for (int i = 0; i < 4; i++) {
			atHand.add ((List<Integer>) gameApiState.get(getAtHandKey(i)));
			atDeclared.add ((List<Integer>) gameApiState
					.get(getAtDeclaredKey(i)));
		}
        Equality status=factory.makeCommand((ImmutableList<String>)gameApiState.get(M));
		return new MahJongState(turn, status, ImmutableList.copyOf(playerIds),
				ImmutableList.copyOf(tiles), ImmutableList.copyOf(tilesAtWall),
				ImmutableList.copyOf(tilesUsed),
				ImmutableList.copyOf(atHand.get(0)),
				ImmutableList.copyOf(atDeclared.get(0)),
				ImmutableList.copyOf(atHand.get(1)),
				ImmutableList.copyOf(atDeclared.get(1)),
				ImmutableList.copyOf(atHand.get(2)),
				ImmutableList.copyOf(atDeclared.get(2)),
				ImmutableList.copyOf(atHand.get(3)),
				ImmutableList.copyOf(atDeclared.get(3)));
	}
    private int nextId(int playerId,List<Integer> playerIds)
    {
    	int index=playerIds.indexOf(playerId);
    	int nextIndex=index+1;
    	if (nextIndex>=playerIds.size())
    		nextIndex=0;
    	return playerIds.get(nextIndex);
    }
	private void check(boolean val, Object... debugArguments) {
		if (!val) {
			throw new RuntimeException("We have a hacker! debugArguments="
					+ Arrays.toString(debugArguments));
		}
	}
}