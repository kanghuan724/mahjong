package org.mahjong.client;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.game_api.GameApi.*;
import org.mahjong.client.ACommand;

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
    private static final String W = "Winner";
	private static final String M = "Move";
	private static final String PU = "PickUp";
	private static final String D = "Discard";
	private static final String P = "Peng";
	private static final String C = "Chi";
	private static final String G = "Gang";
	private static final String H = "Hu";	
	private static final String RP = "RefusePeng";
	private static final String RC = "RefuseChi";
	private static final String RG = "RefuseGang";
	private static final String RH = "RefuseHu";
	private static final String T = "T"; // Tile key (T0...T135)
	private static final String TAW = "tilesAtWall";
	private static final String TU = "tilesUsed";
	private static final String WP = "WaitForPeng";
	private static final String WC = "WaitForChi";
	private static final String WG = "WaitForGang";
	private static final String WH = "WaitForHu";
	private static final String ENDGAME = "ENDGAME";
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
		//check(expectedOperations.equals(lastMove), expectedOperations, lastMove);
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

	List<Operation> getInitialMove(List<String> playerIds) {
		List<Operation> operations = Lists.newArrayList();
		// operations.add(new SetTurn(0));
		//operations.add(new Set(M, null));
		operations.add(new Set(M, ImmutableList.of()));
		operations.add(new Set(TAW, getIndicesInRange(0, 135)));
		//operations.add(new Set(TU, null));
		operations.add(new Set(TU, ImmutableList.of()));
		// set hands
		int playerNum = playerIds.size();
		for (int i = 0; i < playerNum; i++) {
			operations.add(new Set(getAtHandKey(String.valueOf(i)), getIndicesInRange(13 * i,
					13 * i + 12)));
			operations.add(new Set(getAtDeclaredKey(String.valueOf(i)),ImmutableList.of()));
		}
		operations.add(new Set(TAW, getIndicesInRange(13*playerNum, 135)));
		// sets all 136 tiles
		for (int i = 0; i < 136; i++) {
			operations.add(new Set(T + i, tileIdToString(i)));
		}
		// shuffle(T0,...,T135)
		operations.add(new Shuffle(getTilesInRange(0, 135)));
		// sets visibility
		for (int i = 0; i < playerNum; i++) {
			for (int j = 0; j < 13; j++) {
				operations.add(new SetVisibility(T + (i * 13 + j),
				// ImmutableList.of(i)));
						ImmutableList.of(playerIds.get(i))));
			}
		}
		operations.add(new SetTurn(playerIds.get(0)));
		// set the visibility of middle tile to null
		//for (int i = 52; i < 136; i++)
		//	operations.add(new SetVisibility(T + i, ImmutableList.of()));
		/*operations.add(new Set(M, ImmutableList.<String> of(PU)));
		operations.add(new SetTurn(playerIds.get(0)));
		operations.add(new Set(TAW, getIndicesInRange(53, 135)));
		operations.add(new Set(getAtHandKey(0), concat(getIndicesInRange(0, 12), getIndicesInRange(53, 53))));*/
		// new SetVisibility(T + tileIndex,ImmutableList.of(playerId)
		//operations.add(new SetVisibility(T + 52, ImmutableList.of(playerIds.get(0))));
		return operations;
	}
    public String idIndex(List<String> playerIds,String id)
    {
    	for (int i=0;i<playerIds.size();i++)
    	{
    		if (playerIds.get(i).equals(id))
    			return String.valueOf(i);
    	}
    	return "-1";
    }
	/** Returns the operations for picking up a tile. */
	public List<Operation> pickUp(MahJongState state, List<String> playerIds) {
		// picking up a tile
		check(state.getTilesAtWall().size() >= 1);
		String playerId = state.getTurn();
		//System.out.println("current Id:"+playerId);
		check(Pick.lastStateValid(state));
		List<Integer> lastAtWall = state.getTilesAtWall();
		List<Integer> newAtWall = new ArrayList<Integer>(lastAtWall.subList(1, lastAtWall.size()));
		List<Integer> lastAtHand = state.getTilesAtHand(idIndex(playerIds,playerId));
		List<Integer> newAtHand = concat(lastAtHand, lastAtWall.subList(0, 1));
		// Integer tileIndex = lastAtWall.get(0);
		int tileIndex = lastAtWall.get(0);
		// 0) new SetTurn(PlayerId),
		// 1) new Set("Move", "PickUp"),
		// 2) new Set("tilesAtWall", [...]),
		// 3) new Set("tilesAtHandOf1/2/3/4, [...]),
		// 4) new SetVisibility(T*, [1]/[2]/[3]/[4])
		//Collections.sort(newAtWall);
		//Collections.sort(newAtHand);
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(playerId),
				new Set(M, ImmutableList.<String> of(PU)), new Set(TAW,
						newAtWall), new Set(getAtHandKey(idIndex(playerIds,playerId)), newAtHand),
				// new SetVisibility(T + tileIndex,ImmutableList.of(playerId)
				new SetVisibility(T + tileIndex, ImmutableList.of(playerId)));
		return expectedOperations;
	}

	/** Returns the operations for discarding a tile. */
	//List<Operation> discard(MahJongState state, // List<Integer> tilesToDiscard,
	//		List<Operation> lastMove, List<Integer> playerIds) {
	public List<Operation> discard(MahJongState state, // List<Integer> tilesToDiscard,
				List<Integer> tilesToDiscard,List<String> playerIds) {
		// discarding up a tile
		String playerId = state.getTurn();
		check(state.getTilesAtHand(idIndex(playerIds,playerId)).size() >= 1);
		check(Discard.lastStateValid(state));
		List<Integer> lastAtHand = state.getTilesAtHand(idIndex(playerIds,playerId));
		//newAtHand gets subtracted from lastAtHand
		List<Integer> newAtHandTile=subtract(lastAtHand,tilesToDiscard);		 
		//Set newAtHand = (Set) lastMove.get(3);
		//List<Integer> newAtHandTile = (List<Integer>) newAtHand.getValue();
		//System.out.println(lastAtHand);
		//System.out.println(newAtHandTile);
		//List<Integer> tilesToDiscard = subtract(lastAtHand, newAtHandTile);
		check(tilesToDiscard.size() == 1);
		List<Integer> lastUsed = state.getTilesUsed();
		List<Integer> newUsed = concat(lastUsed, tilesToDiscard);

		// List<Integer> newAtHand = subtract(lastAtHand, tilesToDiscard);
		
		Integer tileIndex = tilesToDiscard.get(0);
		
		Optional<Tile> discardTile = state.getTiles().get(tileIndex);
		// 0) new SetTurn(0/1/2/3),
		// 1) new Set("move", "Discard"),
		// 2) new Set("tilesUsed", [...]),
		// 3) new Set("tilesAtHandOf1/2/3/4, [...]),
		// 4) new SetVisibility(T*, null)
		newUsed=new ArrayList<Integer> (newUsed);
		newAtHandTile=new ArrayList<Integer> (newAtHandTile);
		
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
		// new SetTurn(playerIds.indexOf(playerId) % 4 + 1),
				new SetTurn(playerId),
				// ?HOW TO SetTurn WHEN A CHI/PENG MAY HAPPEN?
				new Set(M, ImmutableList.<String> of(D, String.valueOf(tileIndex))), new Set(TU, newUsed), new Set(
						getAtHandKey(idIndex(playerIds,playerId)), newAtHandTile), new SetVisibility(T
						+ tileIndex));
		
		return expectedOperations;
	}

	//List<Operation> chi(MahJongState state, List<Operation> lastMove,
	//		List<Integer> playerIds)
	List<Operation> chi(MahJongState state,List<Integer> chiCombo,List<String> playerIds)
	{
		// chi a tile with two tiles at hand
		String playerId = state.getTurn();
		check(state.getTilesAtHand(idIndex(playerIds,playerId)).size() >= 4);
		check(state.getTilesUsed().size() >= 1);
		check(Chi.lastStateValid(state));
		List<Integer> lastUsed = state.getTilesUsed();
		List<Integer> newUsed = new ArrayList<Integer> ();
		if (lastUsed.size() > 1)
			newUsed = lastUsed.subList(0, lastUsed.size() - 2);
		List<Integer> tileToChi = lastUsed.subList(lastUsed.size() - 1,
				lastUsed.size() );

		Integer tileIndex = tileToChi.get(0);
		Optional<Tile> chiTile = state.getTiles().get(tileIndex);
		List<Integer> lastAtHand = state.getTilesAtHand(idIndex(playerIds,playerId));
		//List<Integer> newAtHand = (List<Integer>) ((Set) lastMove.get(3)).getValue();
	
		//List<Integer> tilesToChi = subtract(lastAtHand, newAtHand);
		List<Integer> tilesToChi=subtract(chiCombo,tileToChi);
		List<Integer> newAtHand=subtract(lastAtHand,tilesToChi);
		List<Integer> lastAtDeclared = state.getTilesAtDeclared(idIndex(playerIds,playerId));
		//List<Integer> chiCombo = concat(tileToChi, tilesToChi);
		check(chiCombo.size() == 3);
		//check(Chi.chiCorrect(state, chiCombo));
		// List<Integer> newAtDeclared = concat(lastAtDeclared,
		// lastUsed.subList(lastUsed.size() - 1, lastUsed.size() - 1));
		List<Integer> newAtDeclared = concat(lastAtDeclared, chiCombo);
		// Integer tileIndex[] = new Integer[3];
		// tileIndex[0] = lastUsed.get(lastUsed.size() - 1);
		// tileIndex[1] = tilesToChi.get(0);
		// tileIndex[2] = tilesToChi.get(1);

		// 0) new SetTurn(0/1/2/3),
		// 1) new Set("move", "Chi"),
		// 2) new Set("tilesUsed", [...]),
		// 3) new Set("tilesAtHandOf1/2/3/4, [...]),
		// 4) new Set("tilesAtDeclaredOf1/2/3/4, [...]),
		// 5) new SetVisibility(T*, null)
		newUsed=new ArrayList<Integer> (newUsed);
		newAtHand=new ArrayList<Integer> (newAtHand);
		newAtDeclared=new ArrayList<Integer> (newAtDeclared);
		chiCombo=new ArrayList<Integer> (chiCombo);
		Collections.sort(newUsed);
		Collections.sort(newAtHand);
		Collections.sort(newAtDeclared);
		Collections.sort(chiCombo);
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(playerId),
				new Set(M, ImmutableList.<String> of(C, chiTile.get()
						.toString())), new Set(TU, newUsed), new Set(
						getAtHandKey(idIndex(playerIds,playerId)), newAtHand), new Set(
						getAtDeclaredKey(idIndex(playerIds,playerId)), newAtDeclared),
				new SetVisibility(T + chiCombo.get(0)), new SetVisibility(T
						+ chiCombo.get(1)),
				new SetVisibility(T + chiCombo.get(2)));
		return expectedOperations;
	}

	//List<Operation> refusechi(MahJongState state, List<Operation> lastMove,
	//		List<Integer> playerIds) {
	public List<Operation> refusechi(MahJongState state, 
				List<String> playerIds) {
		check(RefuseChi.lastStateValid(state));
		String playerId = state.getTurn();
		int sourceId = ((WaitForChi) state.getMove()).getSource();
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(playerId),
				new Set(M, ImmutableList.<String> of(RC,
						String.valueOf(sourceId))));
		return expectedOperations;
	}

	//List<Operation> peng(MahJongState state, List<Operation> lastMove,
	//		List<Integer> playerIds) {
	List<Operation> peng(MahJongState state, List<Integer> PengCombo,
			List<String> playerIds) {
		// peng a tile with two tiles at hand
		String playerId = state.getTurn();
		check(state.getTilesAtHand(idIndex(playerIds,playerId)).size() >= 4);
		check(state.getTilesUsed().size() >= 1);
		check(Peng.lastStateValid(state));
		List<Integer> lastUsed = state.getTilesUsed();

		List<Integer> newUsed = new ArrayList<Integer> ();
		if (lastUsed.size() > 1)
			newUsed = new ArrayList<Integer> (lastUsed.subList(0, lastUsed.size() - 1));
		List<Integer> tileToPeng = lastUsed.subList(lastUsed.size() - 1,
				lastUsed.size());

		Integer tileIndex = tileToPeng.get(0);
		Optional<Tile> tilePeng = state.getTiles().get(tileIndex);
		List<Integer> lastAtHand = state.getTilesAtHand(idIndex(playerIds,playerId));
		// List<Integer> newAtHand = subtract(lastAtHand, tilesToPeng);
		List<Integer> tilesToPeng=subtract(PengCombo,tileToPeng);
		List<Integer> newAtHand=subtract(lastAtHand,tilesToPeng);
		//List<Integer> newAtHand = (List<Integer>) ((Set) lastMove.get(3))
		//		.getValue();
		newAtHand=new ArrayList<Integer> (newAtHand);
		//List<Integer> tilesToPeng = subtract(lastAtHand, newAtHand);
		List<Integer> lastAtDeclared = state.getTilesAtDeclared(idIndex(playerIds,playerId));
		lastAtDeclared=new ArrayList<Integer> (lastAtDeclared);
		//List<Integer> PengCombo = concat(tileToPeng, tilesToPeng);
		check(PengCombo.size() == 3);
		//check(Peng.pengCorrect(state, PengCombo));
		// List<Integer> newAtDeclared = concat(lastAtDeclared,
		// lastUsed.subList(lastUsed.size() - 1, lastUsed.size() - 1));
		List<Integer> newAtDeclared = concat(lastAtDeclared, PengCombo);

		Collections.sort(newUsed);
		Collections.sort(newAtHand);
		Collections.sort(newAtDeclared);
		Collections.sort(PengCombo);

		// 0) new SetTurn(0/1/2/3),
		// 1) new Set("move", "Peng"),
		// 2) new Set("tilesUsed", [...]),
		// 3) new Set("tilesAtHandOf1/2/3/4, [...]),
		// 4) new Set("tilesAtDeclaredOf1/2/3/4, [...]),
		// 5) new SetVisibility(T*, null)
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(playerId),
				new Set(M, ImmutableList.<String> of(P, tilePeng.get()
						.toString())), new Set(TU, newUsed), new Set(
						getAtHandKey(idIndex(playerIds,playerId)), newAtHand), new Set(
						getAtDeclaredKey(idIndex(playerIds,playerId)), newAtDeclared),
				new SetVisibility(T + String.valueOf(PengCombo.get(0))), new SetVisibility(T
						+ String.valueOf(PengCombo.get(1))),
				new SetVisibility(T + String.valueOf(PengCombo.get(2))));
		return expectedOperations;
	}

	//List<Operation> refusepeng(MahJongState state, List<Operation> lastMove,
	//		List<Integer> playerIds) {
	public 	List<Operation> refusepeng(MahJongState state,
				List<String> playerIds) {
		check(RefusePeng.lastStateValid(state));
		String playerId = state.getTurn();
		String sourceId;
		if (state.getMove().getName() == "WaitForPeng")
			sourceId = ((WaitForPeng) state.getMove()).getSource();
		else
			sourceId = ((RefusePeng) state.getMove()).getSource();
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(nextId(playerId, playerIds)),
				new Set(M, ImmutableList.<String> of(RP,
						String.valueOf(sourceId))));
		return expectedOperations;
	}
	
	List<Operation> hu(MahJongState state, 
			List<String> playerIds) {
        
		String playerId = state.getTurn();
		String id = idIndex(playerIds,playerId);
		check(Hu.lastStateValid(state));
		//List<Integer> lastUsed = state.getTilesUsed();
		//List<Integer> newUsed = new ArrayList<Integer> ();
		List<Operation> expectedOperations= new ArrayList<Operation> ();
		/*if (state.getMove().getName() != PU) 
			//Hu with a tile from others
		{
			
			if (lastUsed.size()>1)
				newUsed = lastUsed.subList(0,lastUsed.size() - 1);
			List<Integer> tileToHu = lastUsed.subList(lastUsed.size() - 1,
					lastUsed.size() );
			Integer tileIndex = tileToHu.get(0);
			Optional<Tile> huTile = state.getTiles().get(tileIndex);
			List<Integer> lastAtHand = state.getTilesAtHand(idIndex(playerIds,playerId));
			//List<Integer> newAtHand = (List<Integer>) ((Set) lastMove.get(3)).getValue();
			List<Integer> newAtHand=concat(lastAtHand,getIndicesInRange(tileIndex,tileIndex));
			//List<Integer> tilesToHu = subtract(lastAtHand, newAtHand);
			// List<Integer> newAtHand = subtract(lastAtHand, tilesToChi);
			List<Integer> lastAtDeclared = state.getTilesAtDeclared(playerId);
			//List<Integer> huCombo = concat(tileToHu, tilesToHu);
			//check(Hu.huCorrect(state, huCombo, newAtHand));
			// check(Hu.huCorrect(newAtHand));
			// List<Integer> newAtDeclared = concat(lastAtDeclared,
			// lastUsed.subList(lastUsed.size() - 1, lastUsed.size() - 1));
			List<Integer> newAtDeclared = concat(lastAtDeclared, newAtHand);
			// Integer tileIndex[] = new Integer[3];
			// tileIndex[0] = lastUsed.get(lastUsed.size() - 1);
			// tileIndex[1] = tilesToChi.get(0);
			// tileIndex[2] = tilesToChi.get(1);

			// 0) new Set("move", "Hu"),
			// 1) new Set("tilesUsed", [...]),
			// 2) new Set("tilesAtHandOf1/2/3/4, [...]),
			// 4) new SetVisibility(T*, null),
			// 5) new SetVisibility(tilesAtHandOf1/2/3/4, null),
			// 6) new SetVisibility(tilesAtDeclaredOf1/2/3/4, null),
			// 7) new EndGame
			newUsed=new ArrayList<Integer> (newUsed);
			newAtHand=new ArrayList<Integer> (newAtHand);
			newAtDeclared=new ArrayList<Integer> (newAtDeclared);
			//huCombo=new ArrayList<Integer> (huCombo);
			Collections.sort(newUsed);
			Collections.sort(newAtHand);
			Collections.sort(newAtDeclared);
			//Collections.sort(huCombo);
			expectedOperations.add(new Set(M,ImmutableList.of(H,huTile.get().toString())));
			expectedOperations.add(new Set(getAtHandKey(idIndex(playerIds,playerId)),newAtHand));
			
			
		}*/ 

			//Hu by self-helping
			//check(Hu.allSet(state, state.getTilesAtHand(playerId)));
		//	List<Integer> lastAtHand = state.getTilesAtHand(idIndex(playerIds,playerId));
			//check(Hu.huCorrect(lastAtHand));
		List<String> moveList = new ArrayList<String>();
		moveList.add(H);
		expectedOperations.add(new Set(M,moveList));
		/*
		for (int i = 0; i < state.getTilesAtHand(id).size(); i++) {
			expectedOperations.add(new SetVisibility(T + state.getTilesAtHand(idIndex(playerIds,playerId)).get(i)));
		}
		for (int i = 0; i < state.getTilesAtDeclared(id).size(); i++) {
			expectedOperations.add(new SetVisibility(T + state.getTilesAtDeclared(idIndex(playerIds,playerId)).get(i)));
		}*/
		for (int i=0;i<playerIds.size();i++)
		{
			for (int j = 0; j < state.getTilesAtHand(String.valueOf(i)).size(); j++) {
				expectedOperations.add(new SetVisibility(T + state.getTilesAtHand(String.valueOf(i)).get(j)));
			}
			for (int j = 0; j < state.getTilesAtDeclared(String.valueOf(i)).size(); j++) {
				expectedOperations.add(new SetVisibility(T + state.getTilesAtDeclared(String.valueOf(i)).get(j)));
			}
		}
		//List<String> winnerList = new ArrayList<String> ();
		//winnerList.add(playerId);
		//expectedOperations.add(new Set(W,winnerList));
		expectedOperations.add(new SetTurn(playerId));
		//expectedOperations.add(new EndGame(playerId));
		return expectedOperations;
	}
	
	//List<Operation> refusehu(MahJongState state, List<Operation> lastMove,
	//		List<Integer> playerIds) {
	public List<Operation> refusehu(MahJongState state, 
				List<String> playerIds) {
		check(RefuseHu.lastStateValid(state));
		String playerId = state.getTurn();
		String sourceId;
		if (state.getMove().getName() == "WaitForHu")
			sourceId = ((WaitForHu) state.getMove()).getSource();
		else
			sourceId = ((RefuseHu) state.getMove()).getSource();
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(nextId(playerId, playerIds)),
				new Set(M, ImmutableList.<String> of(RH,
						String.valueOf(sourceId))));
		return expectedOperations;
	}

	//List<Operation> refusegang(MahJongState state, List<Operation> lastMove,
	//		List<Integer> playerIds) {
	public List<Operation> refusegang(MahJongState state, 
					List<String> playerIds) {
		check(RefuseGang.lastStateValid(state));
	    String playerId = state.getTurn();
		String sourceId;
		if (state.getMove().getName() == "WaitForGang")
			sourceId = ((WaitForGang) state.getMove()).getSource();
		else
			sourceId = ((RefuseGang) state.getMove()).getSource();
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(nextId(playerId, playerIds)),
				new Set(M, ImmutableList.<String> of(RG,
						String.valueOf(sourceId))));
		return expectedOperations;
	}

	//List<Operation> WaitForChi(MahJongState state, List<Operation> lastMove,
	//		List<Integer> playerIds) {
	 List<Operation> WaitForChi(MahJongState state, 
				List<String> playerIds) {
		check(WaitForChi.lastStateValid(state));
		String playerId = state.getTurn();
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(nextId(playerId, playerIds)),
				new Set(M, ImmutableList.<String> of(WC,
						String.valueOf(playerId))));
		return expectedOperations;
	}

	//List<Operation> WaitForHu(MahJongState state, List<Operation> lastMove,
	//		List<Integer> playerIds) {
   public static List<Operation> GameEnd(List<String> playerIds)
	{
		Map<String,Integer> score=new HashMap<String,Integer> ();
		for (int i=0;i<playerIds.size();i++)
			score.put(playerIds.get(i), 0);
		List<Operation> expectedOperations=ImmutableList.<Operation> of(new EndGame(score));
		return expectedOperations;
	}
   public static List<Operation> gameEnd(String playerId)
   {
	   List<Operation> expectedOperations=new ArrayList<Operation> ();
	   expectedOperations.add(new EndGame(playerId));
	   List<String> moveList = new ArrayList<String>();
	   moveList.add(ENDGAME);
	   expectedOperations.add(new Set(M,moveList));
	   expectedOperations.add(new EndGame(playerId));
	   return expectedOperations;
   }
    List<Operation> WaitForHu(MahJongState state, 
				List<String> playerIds) {
		check(WaitForHu.lastStateValid(state));
		String playerId = state.getTurn();
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(nextId(playerId, playerIds)),
				new Set(M, ImmutableList.<String> of(WH,
						String.valueOf(playerId))));
		return expectedOperations;
	}

	//List<Operation> WaitForGang(MahJongState state, List<Operation> lastMove,
	//		List<Integer> playerIds) {
	List<Operation> WaitForGang(MahJongState state,
				List<String> playerIds) {
		check(WaitForGang.lastStateValid(state));
		List<Operation> expectedOperations = Lists.newArrayList();
		// End the game when no more tiles at wall
		String playerId = state.getTurn();
		expectedOperations.add(new SetTurn(nextId(playerId, playerIds)));
		expectedOperations.add(new Set(M, ImmutableList.<String> of(WG,
				String.valueOf(playerId))));
		if (state.getTilesAtWall().isEmpty()) {
			expectedOperations.add(new EndGame("0"));
		} 
		return expectedOperations;
	}

	//List<Operation> WaitForPeng(MahJongState state, List<Operation> lastMove,
	//		List<Integer> playerIds) {
	List<Operation> WaitForPeng(MahJongState state, List <String> playerIds) {
		check(WaitForPeng.lastStateValid(state));
		String playerId = state.getTurn();
		List<Operation> expectedOperations = ImmutableList.<Operation> of(
				new SetTurn(nextId(playerId, playerIds)),
				new Set(M, ImmutableList.<String> of(WP,
						String.valueOf(playerId))));
		return expectedOperations;
	}

	//List<Operation> gang(MahJongState state, List<Operation> lastMove,
	//		List<Integer> playerIds) {
	List<Operation> gang(MahJongState state, List<Integer> GangCombo,
				List<String> playerIds) {
		// peng a tile with two tiles at hand
		String playerId = state.getTurn();
		check(state.getTilesAtHand(idIndex(playerIds,playerId)).size() >= 4);
		check(state.getTilesUsed().size() >= 1);
		check(Gang.lastStateValid(state));
		List<Integer> lastUsed = state.getTilesUsed();
		List<Integer> newUsed = new ArrayList<Integer> ();
		if (lastUsed.size() > 1)
			newUsed = lastUsed.subList(0, lastUsed.size() - 2);

		List<Integer> tileToGang = lastUsed.subList(lastUsed.size() - 1,
				lastUsed.size());

		Integer tileIndex = tileToGang.get(0);
		Optional<Tile> gangTile = state.getTiles().get(tileIndex);
		List<Integer> lastAtHand = state.getTilesAtHand(idIndex(playerIds,playerId));
		// List<Integer> newAtHand = subtract(lastAtHand, tilesToPeng);
	
		List<Integer> tilesToGang=subtract(GangCombo,tileToGang);
		List<Integer> newAtHand=subtract(lastAtHand,tilesToGang);
		//List<Integer> newAtHand = (List<Integer>) ((Set) lastMove.get(3))
		//		.getValue();
		//List<Integer> tilesToGang = subtract(lastAtHand, newAtHand);
		List<Integer> lastAtDeclared = state.getTilesAtDeclared(idIndex(playerIds,playerId));
		//List<Integer> GangCombo = concat(tileToGang, tilesToGang);
		check(GangCombo.size() == 4);
		//check(Gang.gangCorrect(state, GangCombo));
		// List<Integer> newAtDeclared = concat(lastAtDeclared,
		// lastUsed.subList(lastUsed.size() - 1, lastUsed.size() - 1));
		List<Integer> newAtDeclared = concat(lastAtDeclared, GangCombo);
		/*
		 * Integer tileIndex[] = new Integer[3]; tileIndex[0] =
		 * lastUsed.get(lastUsed.size() - 1); tileIndex[1] = tilesToPeng.get(0);
		 * tileIndex[2] = tilesToPeng.get(1);
		 */

		// 0) new SetTurn(0/1/2/3),
		// 1) new Set("move", "Gang"),
		// 2) new Set("tilesUsed", [...]),
		// 3) new Set("tilesAtHandOf1/2/3/4, [...]),
		// 4) new Set("tilesAtDeclaredOf1/2/3/4, [...]),
		// 5) new SetVisibility(T*, null)
		newUsed=new ArrayList<Integer> (newUsed);
		newAtHand=new ArrayList<Integer> (newAtHand);
		newAtDeclared=new ArrayList<Integer> (newAtDeclared);
		GangCombo=new ArrayList<Integer> (GangCombo);
		Collections.sort(newUsed);
		Collections.sort(newAtHand);
		Collections.sort(newAtDeclared);
		Collections.sort(GangCombo);
		List<Operation> tempOperation=new ArrayList<Operation> ();
		tempOperation.add(new SetTurn(playerId));
		tempOperation.add(new Set(M, ImmutableList.<String> of(G, gangTile.get()
				.toString())));
		tempOperation.add(new Set(TU, newUsed));
		tempOperation.add(new Set(getAtHandKey(idIndex(playerIds,playerId)), newAtHand));
		tempOperation.add(new Set(getAtDeclaredKey(idIndex(playerIds,playerId)), newAtDeclared));
		tempOperation.add(new SetVisibility(T + GangCombo.get(0)));
		tempOperation.add(new SetVisibility(T + GangCombo.get(1)));
		tempOperation.add(new SetVisibility(T + GangCombo.get(2)));
		tempOperation.add(new SetVisibility(T + GangCombo.get(3)));
		List<Operation> expectedOperations=ImmutableList.<Operation>copyOf(tempOperation);
		/*List<Operation> expectedOperations = ImmutableList.<Operation> (
				new SetTurn(playerId),
				new Set(M, ImmutableList.<String> of(G, gangTile.get()
						.toString())), new Set(TU, newUsed), new Set(
						getAtHandKey(playerId), newAtHand), new Set(
						getAtDeclaredKey(playerId), newAtDeclared),
				new SetVisibility(T + GangCombo.get(0)), new SetVisibility(T
						+ GangCombo.get(1)),
				new SetVisibility(T + GangCombo.get(2), 
			    new SetVisibility(T+ GangCombo.get(3))
						));*/
		return expectedOperations;
	}

	// getExpectedOperations need to be developed
	private Operation getM(List<Operation> lastMove) {
		for (Operation operation : lastMove) {
			if (operation.getMessageName() == "Set") {
				Set sOperation = (Set) operation;
				if (sOperation.getKey() == M)
					return operation;
			}
		}
		throw new RuntimeException("Run Time Exception: No set operations");

	}

	List<Operation> getExpectedOperations(Map<String, Object> lastApiState,
			List<Operation> lastMove, List<String> playerIds,
			String lastMovePlayerId) {
		if (lastApiState.isEmpty()) {
			return getInitialMove(playerIds);
		}

		MahJongState lastState = gameApiStateToMahJongState(lastApiState,
				lastMovePlayerId, playerIds);
		Set lastSet = (Set) getM(lastMove);
		List<String> lastSetMove = (List<String>) lastSet.getValue();
		switch (lastSetMove.get(0)) {
		case ("GameEnd"):
			return GameEnd(playerIds);
		case (H):
			return hu(lastState,playerIds);
		case (PU):
			return pickUp(lastState, playerIds);
		case (D):
		{
			System.out.println("I've been here1");
			List<Integer> lastAtHand = lastState.getTilesAtHand(idIndex(playerIds,lastState.getTurn()));
			Set newAtHand = (Set) lastMove.get(3);
			List<Integer> newAtHandTile = (List<Integer>) newAtHand.getValue();
			List<Integer> tilesToDiscard = subtract(lastAtHand, newAtHandTile);
			System.out.println("I've been here2");
			return discard(lastState, tilesToDiscard, playerIds);
		}
		case (C):
		{
			List<Integer> lastUsed = lastState.getTilesUsed();
			List<Integer> tileToChi = lastUsed.subList(lastUsed.size() - 1,
					lastUsed.size() );
			List<Integer> newAtHand = (List<Integer>) ((Set) lastMove.get(3)).getValue();
			List<Integer> lastAtHand = lastState.getTilesAtHand(idIndex(playerIds,lastState.getTurn()));
			List<Integer> tilesToChi = subtract(lastAtHand, newAtHand);
			List<Integer> chiCombo=concat(tileToChi,tilesToChi);
			System.out.println("chiCombosize: "+chiCombo.size());
			System.out.println("tilleToChi: "+tileToChi.size());
			System.out.println("tilesToChi: "+tilesToChi.size());
			//chi(lastState, lastMove, playerIds);
			return chi(lastState,chiCombo,playerIds);
		}
		case (P):
		{
			List<Integer> lastUsed = lastState.getTilesUsed();
			List<Integer> tileToPeng = lastUsed.subList(lastUsed.size() - 1,
					lastUsed.size() );
			List<Integer> newAtHand = (List<Integer>) ((Set) lastMove.get(3)).getValue();
			List<Integer> lastAtHand = lastState.getTilesAtHand(idIndex(playerIds,lastState.getTurn()));
			List<Integer> tilesToPeng = subtract(lastAtHand, newAtHand);
			List<Integer> pengCombo=concat(tileToPeng,tilesToPeng);
			return peng(lastState,pengCombo,playerIds);
			//return peng(lastState, lastMove, playerIds);
		}
		case (G):
		{
			List<Integer> lastUsed = lastState.getTilesUsed();
			List<Integer> tileToGang = lastUsed.subList(lastUsed.size() - 1,
					lastUsed.size() );
			List<Integer> newAtHand = (List<Integer>) ((Set) lastMove.get(3)).getValue();
			List<Integer> lastAtHand = lastState.getTilesAtHand(idIndex(playerIds,lastState.getTurn()));
			List<Integer> tilesToGang = subtract(lastAtHand, newAtHand);
			List<Integer> gangCombo=concat(tileToGang,tilesToGang);
			//return gang(lastState, lastMove, playerIds);
			return gang(lastState,gangCombo,playerIds);
		}
		case (RG):
			//return refusegang(lastState, lastMove, playerIds);
			return refusegang(lastState,playerIds);
		case (RP):
			//return refusepeng(lastState, lastMove, playerIds);
			return refusepeng(lastState,playerIds);
		case (RC):
			//return refusechi(lastState, lastMove, playerIds);
		    return refusechi(lastState,  playerIds);
		case (WC):
			//return WaitForChi(lastState, lastMove, playerIds);
			return WaitForChi(lastState,  playerIds);
		case (WP):
			//return WaitForPeng(lastState, lastMove, playerIds);
			return WaitForPeng(lastState,  playerIds);
		case (WG):
			//return WaitForGang(lastState, lastMove, playerIds);
		    return WaitForGang(lastState,  playerIds);
		case (WH):
			//return WaitForHu(lastState, lastMove, playerIds);
		    return WaitForHu(lastState,  playerIds);
		case (RH):
			//return refusehu(lastState, lastMove, playerIds);
		    return refusehu(lastState,  playerIds);
		}
		return null;

	}

	public static String tileIdToString(int tileId) {
		checkArgument(tileId >= 0 && tileId < 136);
		/*
		 * Tile 0-35 are ACHARACTERS Tile 0-8 are ACHARACTER 1-9 Tile 9-17 are
		 * ACHARACTER 1-9 Tile 18-26 are ACHARACTER 1-9 Tile 27-35 are
		 * ACHARACTER 1-9 Tile 36-71 are Bamboos TIle 72-107 are Circles Tile
		 * 108-135 are other categories
		 */
		int rank;
		int suit;
		/*
		 * if (tileId < 108) { rank = (tileId / 12 + 1); if (tileId % 3 < 4) {
		 * suit = 0; } else if (tileId % 3 < 8) { suit = 1; } else { suit = 2; }
		 * }
		 */
		if (tileId < 108) {
			suit = tileId / 36;
			rank = tileId % 9 + 1;

		} else {
			rank = 0;
			suit = tileId / 4 - 24;
		}
		String rankString = Rank.values()[rank].getRankString();
		String suitString = Suit.values()[suit].getFirstLetterLowerCase();
		// return rankString + suitString;
		return suitString + rankString;
	}

	public static <T> List<T> concat(List<T> a, List<T> b) {
		return Lists.newArrayList(Iterables.concat(a, b));
	}

	<T> List<T> subtract(List<T> removeFrom, List<T> elementsToRemove) {
		check(removeFrom.containsAll(elementsToRemove), removeFrom,
				elementsToRemove);
		List<T> result = Lists.newArrayList(removeFrom);
		for (T element: elementsToRemove)
		  result.remove(element);
		check(removeFrom.size() == result.size() + elementsToRemove.size());
		return result;
	}

	public static ImmutableList<Integer> getIndicesInRange(int fromInclusive,
			int toInclusive) {
		List<Integer> keys = Lists.newArrayList();
		for (int i = fromInclusive; i <= toInclusive; i++) {
			keys.add(i);
		}
		ImmutableList<Integer> iKeys = ImmutableList.copyOf(keys);
		return iKeys;
	}

	public static List<String> getTilesInRange(int fromInclusive, int toInclusive) {
		List<String> keys = Lists.newArrayList();
		for (int i = fromInclusive; i <= toInclusive; i++) {
			keys.add(T + i);
		}
		return keys;
	}

	public static String getAtHandKey(String playerId) {
		String index = null;
		switch (playerId) {
		case "0":
			index = "One";
			break;
		case "1":
			index = "Two";
			break;
		case "2":
			index = "Three";
			break;
		case "3":
			index = "Four";
			break;
		}
		return "tilesAtHandOf" + index;
	}

	public static String getAtDeclaredKey(String playerId) {
		String index = null;
		switch (playerId) {
		case "0":
			index = "One";
			break;
		case "1":
			index = "Two";
			break;
		case "2":
			index = "Three";
			break;
		case "3":
			index = "Four";
			break;
		}
		return "tilesAtDeclaredOf" + index;
	}

	@SuppressWarnings({ "unchecked" })
	public static MahJongState gameApiStateToMahJongState(
			Map<String, Object> gameApiState, String turn, List<String> playerIds) {
		List<Optional<Tile>> tiles = Lists.newArrayList();
		int playerNum = playerIds.size();
		for (int i = 0; i < 136; i++) {
			String tileString = (String) gameApiState.get(T + i);
			Tile tile;
			if (tileString == null) {
				
				tile = null;
			} else {
				/*
				 * Rank rank = Rank.fromRankString(tileString.substring(0, 1));
				 * Suit suit = Suit.fromFirstLetterLowerCase(tileString
				 * .substring(1));
				 */
				
				Suit suit = Suit.fromFirstLetterLowerCase(tileString.substring(
						0, 1));
				Rank rank = Rank.fromRankString(tileString.substring(1));
				tile = new Tile(suit, rank);
			}
			tiles.add(Optional.fromNullable(tile));
		}
		List<Integer> tilesAtWall = (List<Integer>) gameApiState.get(TAW);
		List<Integer> tilesUsed = (List<Integer>) gameApiState.get(TU);
		List<List<Integer>> atHand = new ArrayList<List<Integer>>();
		List<List<Integer>> atDeclared = new ArrayList<List<Integer>>();
		for (int i = 0; i < playerNum; i++) {
			atHand.add((List<Integer>) gameApiState.get(getAtHandKey(String.valueOf(i))));
			atDeclared.add((List<Integer>) gameApiState
					.get(getAtDeclaredKey(String.valueOf(i))));
		}
		ACommand status = factory
				.makeCommand((List<String>) gameApiState.get(M));
		//System.out.println(((ImmutableList<String>)(gameApiState.get(M))).get(0));
		if (playerNum==4)
		  return new MahJongState(turn, status, ImmutableList.copyOf(playerIds),
				ImmutableList.copyOf(tiles), ImmutableList.copyOf(tilesAtWall),
				ImmutableList.copyOf(tilesUsed), ImmutableList.copyOf(atHand
						.get(0)), ImmutableList.copyOf(atDeclared.get(0)),
				ImmutableList.copyOf(atHand.get(1)),
				ImmutableList.copyOf(atDeclared.get(1)),
				ImmutableList.copyOf(atHand.get(2)),
				ImmutableList.copyOf(atDeclared.get(2)),
				ImmutableList.copyOf(atHand.get(3)),
				ImmutableList.copyOf(atDeclared.get(3)));
		else
			return new MahJongState(turn, status,
					ImmutableList.copyOf(playerIds),
					ImmutableList.copyOf(tiles),
					ImmutableList.copyOf(tilesAtWall),
					ImmutableList.copyOf(tilesUsed),
					ImmutableList.copyOf(atHand.get(0)),
					ImmutableList.copyOf(atDeclared.get(0)),
					ImmutableList.copyOf(atHand.get(1)),
					ImmutableList.copyOf(atDeclared.get(1)),
					ImmutableList.copyOf(new ArrayList()),
					ImmutableList.copyOf(new ArrayList()),
					ImmutableList.copyOf(new ArrayList()),
					ImmutableList.copyOf(new ArrayList()));
	}

	public static String nextId(String playerId, List<String> playerIds) {
		int index = playerIds.indexOf(playerId);
		int nextIndex = index + 1;
		if (nextIndex >= playerIds.size())
			nextIndex = 0;
		return playerIds.get(nextIndex);
	}

	private void check(boolean val, Object... debugArguments) {
		//if (!val) {
		//	throw new RuntimeException("We have a hacker! debugArguments="
	//				+ Arrays.toString(debugArguments));
		//}
	}
}