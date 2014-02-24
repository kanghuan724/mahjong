package edu.nyu.mahjong.logic;

import static org.junit.Assert.*;

import org.cheat.client.GameApi.Delete;
import org.cheat.client.GameApi.Operation;
import org.cheat.client.GameApi.Set;
import org.cheat.client.GameApi.SetTurn;
import org.cheat.client.GameApi.SetVisibility;
import org.cheat.client.GameApi.Shuffle;
import org.cheat.client.GameApi.VerifyMove;
import org.cheat.client.GameApi.VerifyMoveDone;
import org.cheat.client.GameApi.EndGame;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import edu.nyu.mahjong.logic.MahJongLogic;

@RunWith(JUnit4.class)
    public class MahJongLogicTest {

	private static final String H = "Hu";
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
	private static final String T = "T"; // Tile key (T0...T135)
	private static final String TAW = "tilesAtWall";
	private static final String TU = "tilesUsed";
	private static final String WP = "WaitForPeng";
	private static final String WC = "WaitForChi";
	private static final String WG = "WaitForGang";
	private static final String WH = "WaitForHu";
	private static final String TURN = "turn";
	private static final String PLAYER_ID = "playerId";
	private static final String PlayerOne = "0";
	private static final String PlayerTwo = "1";
	private static final String PlayerThree = "2";
	private static final String PlayerFour = "3";
	private static final String EndGame = "EndGame";
	private static final String AtHandOne = "tilesAtHandOfOne";
	private static final String AtHandTwo = "tilesAtHandOfTwo";
	private static final String AtHandThree = "tilesAtHandOfThree";
	private static final String AtHandFour = "tilesAtHandOfFour";
	private static final String DeclaredOne = "tilesAtDeclaredOfOne";
	private static final String DeclaredTwo = "tilesAtDeclaredOfTwo";
	private static final String DeclaredThree = "tilesAtDeclaredOfThree";
	private static final String DeclaredFour = "tilesAtDeclaredOfFour";
	private final int aId = 0;
	private final int bId = 1;
	private final int cId = 2;
	private final int dId = 3;
	private final ImmutableList<Integer> visibleToOne = ImmutableList.of(aId);
	private final ImmutableList<Integer> visibleToTwo = ImmutableList.of(bId);
	private final ImmutableList<Integer> visibleToThree = ImmutableList.of(cId);
	private final ImmutableList<Integer> visibleToFour = ImmutableList.of(dId);
	private final ImmutableMap<String, Object> aInfo = ImmutableMap
			.<String, Object> of(PLAYER_ID, aId);
	private final ImmutableMap<String, Object> bInfo = ImmutableMap
			.<String, Object> of(PLAYER_ID, bId);
	private final ImmutableMap<String, Object> cInfo = ImmutableMap
			.<String, Object> of(PLAYER_ID, cId);
	private final ImmutableMap<String, Object> dInfo = ImmutableMap
			.<String, Object> of(PLAYER_ID, dId);
	private final ImmutableList<Map<String, Object>> playersInfo = ImmutableList
			.<Map<String, Object>> of(aInfo, bInfo, cInfo, dInfo);
	private final ImmutableMap<String, Object> emptyState = ImmutableMap
			.<String, Object> of();
	private final ImmutableMap<String, Object> nonemptyState = ImmutableMap
			.<String, Object> of("a", "b");
	private final ImmutableList<Operation> InvalidPengofOne = ImmutableList
			.<Operation> of(new SetTurn(aId),
					new Set(M, ImmutableList.<String> of(P, "a1")), new Set(
							TU, ImmutableList.<Integer> of()), new Set(
							AtHandOne, getIndicesInRange(2, 12)), new Set(
							DeclaredTwo, ImmutableList.<Integer> of(0, 1, 52)),
					new SetVisibility("T0"), new SetVisibility("T1"),
					new SetVisibility("T52"));
	private final ImmutableList<Operation> PengofTwo = ImmutableList
			.<Operation> of(
					new SetTurn(bId),
					new Set(M, ImmutableList.<String> of(P, "a1")),
					new Set(TU, ImmutableList.<Integer> of()),
					new Set(AtHandTwo, getIndicesInRange(15, 25)),
					new Set(DeclaredTwo, ImmutableList.<Integer> of(13, 14, 52)),
					new SetVisibility("T13"), new SetVisibility("T14"),
					new SetVisibility("T52"));
	private final ImmutableList<Operation> GangofTwo = ImmutableList
			.<Operation> of(
					new SetTurn(bId),
					new Set(M, ImmutableList.<String> of(G, "a1")),
					new Set(TU, ImmutableList.<Integer> of()),
					new Set(AtHandTwo, getIndicesInRange(16, 25)),
					new Set(DeclaredTwo, ImmutableList.<Integer> of(13,14,15,52)),
					new SetVisibility("T13"), new SetVisibility("T14"),
					new SetVisibility("T15"),
					new SetVisibility("T52"));
	private final ImmutableList<Operation> ChiofTwo = ImmutableList
			.<Operation> of(
					new SetTurn(bId),
					new Set(M, ImmutableList.<String> of(C, "a5")),
					new Set(TU, ImmutableList.<Integer> of()),
					new Set(AtHandTwo, getIndicesInRange(15, 25)),
					new Set(DeclaredTwo, ImmutableList.<Integer> of(13, 14, 52)),
					new SetVisibility("T13"), new SetVisibility("T14"),
					new SetVisibility("T52"));
	private final ImmutableList<Operation> PickUpofTwo = ImmutableList
			.<Operation> of(
					new SetTurn(bId),
					new Set(M, ImmutableList.<String> of(PU)),
					new Set(TAW, getIndicesInRange(54, 135)),
					new Set(AtHandTwo, concat(getIndicesInRange(13,25),getIndicesInRange(53,53))),
					new SetVisibility("T53",ImmutableList.of(bId)));
	private final ImmutableList<Operation> PickUpofTwoWithWrongMiddle = ImmutableList
			.<Operation> of(
					new SetTurn(bId),
					new Set(M, ImmutableList.<String> of(PU)),
					new Set(TAW, getIndicesInRange(55, 135)),
					new Set(AtHandTwo, concat(getIndicesInRange(13,25),getIndicesInRange(53,53))),
					new SetVisibility("T53",ImmutableList.of(bId)));
	private final ImmutableList<Operation> PickUpofTwoWithWrongHand = ImmutableList
			.<Operation> of(
					new SetTurn(bId),
					new Set(M, ImmutableList.<String> of(PU)),
					new Set(TAW, getIndicesInRange(54, 135)),
					new Set(AtHandTwo, concat(getIndicesInRange(13,26),getIndicesInRange(53,53))),
					new SetVisibility("T53",ImmutableList.of(bId)));
	
	private final ImmutableList<Operation> RefuseGangofTwo = ImmutableList
			.<Operation> of(
					new SetTurn(cId),
					new Set(M, ImmutableList.<String> of(RG,String.valueOf(aId))));
	private final ImmutableList<Operation> RefuseGangofThree = ImmutableList
			.<Operation> of(
					new SetTurn(dId),
					new Set(M, ImmutableList.<String> of(RG,String.valueOf(aId))));
	private final ImmutableList<Operation> RefuseGangofOne = ImmutableList
			.<Operation> of(
					new SetTurn(bId),
					new Set(M, ImmutableList.<String> of(RG,String.valueOf(aId))));
	private final ImmutableList<Operation> GameEndWithNoTile= ImmutableList
			.<Operation> of(
					new SetTurn(bId),
					new Set(M, ImmutableList.<String> of(WG,String.valueOf(aId))),
					new EndGame(0));
	private final ImmutableList<Operation> GameContinues= ImmutableList
			.<Operation> of(
					new SetTurn(bId),
					new Set(M, ImmutableList.<String> of(WG,String.valueOf(aId))));
	private final String getAtHandKey(int id)
	{
		return MahJongLogic.getAtHandKey(id);
	}
	private final String tileIdToString(int index)
	{
		return MahJongLogic.tileIdToString(index);
	}
	private final List<String> getTilesInRange(int fromInclusive,int toInclusive)
	{
		return MahJongLogic.getTilesInRange(fromInclusive, toInclusive);
	}
	private final List<Integer> concat(List<Integer> a, List<Integer> b)
	{
		return MahJongLogic.concat(a,b);
	}
	private final ImmutableList<Operation> validInitialMove()
	{
		List<Operation> mockList=new ArrayList<Operation> ();
		mockList.add(new Set(M, null));
		mockList.add(new Set(TAW, getIndicesInRange(0, 135)));
		mockList.add(new Set(TU, null));
		for (int i = 0; i < 4; i++) {
			mockList.add(new Set(getAtHandKey(i), getIndicesInRange(13 * i,
					13 * i + 12)));
		}
		mockList.add(new Set(TAW, getIndicesInRange(52, 135)));
		for (int i = 0; i < 136; i++) {
			mockList.add(new Set(T + i, tileIdToString(i)));
		}
		mockList.add(new Shuffle(getTilesInRange(0, 135)));
		// sets visibility
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				mockList.add(new SetVisibility(T + (i * 13 + j),
				// ImmutableList.of(i)));
						ImmutableList.of(i)));
			}
		}
		// set the visibility of middle tile to null
		//for (int i = 52; i < 136; i++)
		//	mockList.add(new SetVisibility(T + i, ImmutableList.of()));
		mockList.add(new Set(M, ImmutableList.<String> of(PU)));
		mockList.add(new SetTurn(0));
		mockList.add(new Set(TAW,getIndicesInRange(53, 135)));
		mockList.add(new Set(getAtHandKey(0),concat(getIndicesInRange(0, 12),getIndicesInRange(53, 53))));
		// new SetVisibility(T + tileIndex,ImmutableList.of(playerId)
		mockList.add(new SetVisibility(T + 52, ImmutableList.of(0)));
		return ImmutableList.<Operation>copyOf(mockList);
		
	}
	private final ImmutableList<Operation> invalidInitialMoveWithExtraMove()
	{
		List<Operation> mockList=new ArrayList<Operation> ();
		mockList.add(new Set(M, null));
		mockList.add(new Set(TAW, getIndicesInRange(0, 135)));
		mockList.add(new Set(TU, null));
		for (int i = 0; i < 4; i++) {
			mockList.add(new Set(getAtHandKey(i), getIndicesInRange(13 * i,
					13 * i + 12)));
		}
		mockList.add(new Set(TAW, getIndicesInRange(52, 135)));
		for (int i = 0; i < 136; i++) {
			mockList.add(new Set(T + i, tileIdToString(i)));
		}
		mockList.add(new Shuffle(getTilesInRange(0, 135)));
		// sets visibility
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				mockList.add(new SetVisibility(T + (i * 13 + j),
				// ImmutableList.of(i)));
						ImmutableList.of(i)));
			}
		}
		// set the visibility of middle tile to null
		//for (int i = 52; i < 136; i++)
		//	mockList.add(new SetVisibility(T + i, ImmutableList.of()));
		mockList.add(new Set(M, ImmutableList.<String> of(PU)));
		mockList.add(new SetTurn(0));
		mockList.add(new Set(TAW,getIndicesInRange(53, 135)));
		mockList.add(new Set(getAtHandKey(0),concat(getIndicesInRange(0, 12),getIndicesInRange(53, 53))));
		// new SetVisibility(T + tileIndex,ImmutableList.of(playerId)
		mockList.add(new SetVisibility(T + 52, ImmutableList.of(0)));
		mockList.add(new SetTurn(1));
		return ImmutableList.<Operation>copyOf(mockList);
	}
	
	ImmutableList<Operation> DiscardOfTwo(ImmutableMap<String, Object> lastState)
	{
		String target=(String)lastState.get("T53");
		return ImmutableList
				.<Operation> of(
						new SetTurn(bId),
						new Set(M, ImmutableList.<String> of(D,target)),
						new Set(TU,getIndicesInRange(52,53)),			
						new Set(AtHandTwo, getIndicesInRange(13,25)),
						new SetVisibility("T53"));
	}
	ImmutableList<Operation> DiscardOfTwoWithWrongMiddle(ImmutableMap<String, Object> lastState)
	{
		String target=(String)lastState.get("T53");
		return ImmutableList
				.<Operation> of(
						new SetTurn(bId),
						new Set(M, ImmutableList.<String> of(D,target)),
						new Set(TU,getIndicesInRange(52,54)),			
						new Set(AtHandTwo, getIndicesInRange(13,25)),
						new SetVisibility("T53"));
	}
	
	//Set up all the essnetial test state maps
    private MahJongLogic mahjong;
	private ImmutableMap<String, Object> validStateBeforePeng;
	private ImmutableMap<String, Object> validStateBeforePengForThree;
	private ImmutableMap<String, Object> validStateBeforeGang;
	private ImmutableMap<String, Object> InvalidStateBeforeGang;
	private ImmutableMap<String, Object> validStateBeforeChi;
	private ImmutableMap<String, Object> validStateBeforeRefuseGang;
	private ImmutableMap<String, Object> validStateBeforeRefuseGang2;
	private ImmutableMap<String, Object> validStateBeforeNoTile;
	private ImmutableMap<String, Object> noTileAtWall;
	private ImmutableMap<String, Object> oneTileAtWall;
	private ImmutableMap<String, Object> buildValidStateBeforePeng2() {
		Map<String, Object> image = new HashMap<String, Object>();
		image.put(TURN, PlayerTwo);
		image.put(AtHandOne, getIndicesInRange(0, 12));
		image.put(AtHandTwo, getIndicesInRange(13, 25));
		image.put(AtHandThree, getIndicesInRange(26, 38));
		image.put(AtHandFour, getIndicesInRange(39, 51));
		image.put(DeclaredOne, getEmpty());
		image.put(DeclaredTwo, getEmpty());
		image.put(DeclaredThree, getEmpty());
		image.put(DeclaredFour, getEmpty());
		image.put(TAW, getIndicesInRange(53, 135));
		image.put(TU, getIndicesInRange(52, 52));
		image.put(M, ImmutableList.of(RP, String.valueOf(bId)));
		addTileInfoToMap(image, shuffleForLegalPengofTwo());
		return ImmutableMap.copyOf(image);
	}
	private ImmutableMap<String, Object> buildValidStateBeforeickUp() {
		Map<String, Object> image = new HashMap<String, Object>();
		image.put(TURN, PlayerTwo);
		image.put(AtHandOne, getIndicesInRange(0, 12));
		image.put(AtHandTwo, getIndicesInRange(13, 25));
		image.put(AtHandThree, getIndicesInRange(26, 38));
		image.put(AtHandFour, getIndicesInRange(39, 51));
		image.put(DeclaredOne, getEmpty());
		image.put(DeclaredTwo, getEmpty());
		image.put(DeclaredThree, getEmpty());
		image.put(DeclaredFour, getEmpty());
		image.put(TAW, getIndicesInRange(53, 135));
		image.put(TU, getIndicesInRange(52, 52));
		image.put(M, ImmutableList.of(RC, String.valueOf(bId)));
		addTileInfoToMap(image, shuffleForLegalPengofTwo());
		return ImmutableMap.copyOf(image);
		
	}
	private ImmutableMap<String, Object> buildValidStateBeforeDiscard() {
		Map<String, Object> image = new HashMap<String, Object>();
		image.put(TURN, PlayerTwo);
		image.put(AtHandOne, getIndicesInRange(0, 12));
		image.put(AtHandTwo, concat(getIndicesInRange(13, 25),getIndicesInRange(53, 53)));
		image.put(AtHandThree, getIndicesInRange(26, 38));
		image.put(AtHandFour, getIndicesInRange(39, 51));
		image.put(DeclaredOne, getEmpty());
		image.put(DeclaredTwo, getEmpty());
		image.put(DeclaredThree, getEmpty());
		image.put(DeclaredFour, getEmpty());
		image.put(TAW, getIndicesInRange(54, 135));
		image.put(TU, getIndicesInRange(52, 52));
		image.put(M, ImmutableList.of(PU, String.valueOf(bId)));
		addTileInfoToMap(image, shuffleForLegalPengofTwo());
		return ImmutableMap.copyOf(image);
		
	}

	private final ImmutableMap<String, Object> ValidStateBeforePeng2 = buildValidStateBeforePeng2();
		
	@Before
	public void setUp() throws Exception {
		mahjong = new MahJongLogic();

		Map<String, Object> mockMap = new HashMap<String, Object>();
		mockMap.put(TURN, PlayerOne);
		mockMap.put(AtHandOne, getIndicesInRange(0, 12));
		mockMap.put(AtHandTwo, getIndicesInRange(13, 25));
		mockMap.put(AtHandThree, getIndicesInRange(26, 38));
		mockMap.put(AtHandFour, getIndicesInRange(39, 51));
		mockMap.put(DeclaredOne, getEmpty());
		mockMap.put(DeclaredTwo, getEmpty());
		mockMap.put(DeclaredThree, getEmpty());
		mockMap.put(DeclaredFour, getEmpty());
		mockMap.put(TAW, getIndicesInRange(53, 135));
		mockMap.put(TU, getIndicesInRange(52, 52));
		mockMap.put(M, ImmutableList.of(WP, String.valueOf(aId)));
		
		Map<String, Object> validMap = new HashMap<String,Object>(mockMap);
		addTileInfoToMap(validMap, shuffleForLegalPengofTwo());
		validStateBeforePeng = ImmutableMap.copyOf(validMap);

		Map<String, Object> validMap2 = new HashMap<String,Object>(mockMap);
		addTileInfoToMap(validMap2, shuffleForLegalPengofThree());
		validStateBeforePengForThree= ImmutableMap.copyOf(validMap2);
		
		Map<String, Object> validMap3 = new HashMap<String,Object>(mockMap);
		validMap3.put(M, ImmutableList.of(WG, String.valueOf(aId)));
		addTileInfoToMap(validMap3, shuffleForLegalGangofThree());
		validStateBeforeGang = ImmutableMap.copyOf(validMap3);
		
		Map<String, Object> validMap4=new HashMap<String,Object>(validMap3);
		addTileInfoToMap(validMap4, shuffleForLegalPengofThree());
		InvalidStateBeforeGang =  ImmutableMap.copyOf(validMap4);
		
		Map<String, Object> validMap5=new HashMap<String,Object>(mockMap);
		addTileInfoToMap(validMap5, shuffleForLegalPengofThree());
		validMap5.put(M, ImmutableList.of(WG, String.valueOf(aId)));
		validStateBeforeRefuseGang= ImmutableMap.copyOf(validMap5);
		
		Map<String,Object> validMap6=new HashMap<String,Object>(validMap5);	
		validMap6.put(TURN, PlayerTwo);
		validMap6.put(M, ImmutableList.of(RG, String.valueOf(aId)));
		validStateBeforeRefuseGang2= ImmutableMap.copyOf(validMap6);
		
		Map<String, Object> validMap7 = new HashMap<String,Object>(mockMap);
		addTileInfoToMap(validMap7, shuffleForLegalChiofTwo());
		validMap7.put(M, ImmutableList.of(WC, String.valueOf(aId)));
		validStateBeforeChi = ImmutableMap.copyOf(validMap7);
		
		Map<String, Object> noTile = new HashMap<String, Object>();
		noTile.put(TURN, PlayerFour);
		noTile.put(AtHandOne, getIndicesInRange(0, 12));
		noTile.put(AtHandTwo, getIndicesInRange(13, 25));
		noTile.put(AtHandThree, getIndicesInRange(26, 38));
		noTile.put(AtHandFour, getIndicesInRange(39, 51));
		noTile.put(DeclaredOne, getEmpty());
		noTile.put(DeclaredTwo, getEmpty());
		noTile.put(DeclaredThree, getEmpty());
		noTile.put(DeclaredFour, getEmpty());
		noTile.put(TAW, getEmpty());
		noTile.put(TU, getIndicesInRange(52, 135));
		noTile.put(M, ImmutableList.of(RH, String.valueOf(aId)));
		noTileAtWall=ImmutableMap.copyOf(noTile);
		
		noTile.put(TAW, getIndicesInRange(52,52));
		noTile.put(TU, getIndicesInRange(53,135));
		oneTileAtWall=ImmutableMap.copyOf(noTile);
		
	}
    //Below are auxiliary functions
	private void assertMoveOk(VerifyMove verifyMove) {
		mahjong.checkMoveIsLegal(verifyMove);
	}

	private void assertHacker(VerifyMove verifyMove) {
		VerifyMoveDone verifyDone = mahjong.verify(verifyMove);
		assertEquals(verifyMove.getLastMovePlayerId(),
				verifyDone.getHackerPlayerId());
	}

	private void putToGivenPosition(List<Integer> tiles, int index, int position) {
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i) == index) {
				int temp = tiles.get(position);
				tiles.set(position, index);
				tiles.set(i, temp);
				break;
			}
		}
	}
	private List<String> shuffleForLegalGangofThree() {
		List<Integer> tileIndex = new ArrayList<Integer>();
		for (int i = 0; i < 136; i++) {
			tileIndex.add(i);
		}
		Collections.shuffle(tileIndex);
		putToGivenPosition(tileIndex, 0, 13);
		putToGivenPosition(tileIndex, 9, 14);
		putToGivenPosition(tileIndex, 18, 52);
		putToGivenPosition(tileIndex, 27, 15);
		List<String> tileString = new ArrayList<String>();
		for (int i = 0; i < tileIndex.size(); i++) {
			tileString.add(MahJongLogic.tileIdToString(tileIndex.get(i)));
		}
		return tileString;
	}
	private List<String> shuffleForLegalPengofTwo() {
		List<Integer> tileIndex = new ArrayList<Integer>();
		for (int i = 0; i < 136; i++) {
			tileIndex.add(i);
		}
		Collections.shuffle(tileIndex);
		putToGivenPosition(tileIndex, 0, 13);
		putToGivenPosition(tileIndex, 9, 14);
		putToGivenPosition(tileIndex, 18, 52);
		List<String> tileString = new ArrayList<String>();
		for (int i = 0; i < tileIndex.size(); i++) {
			tileString.add(MahJongLogic.tileIdToString(tileIndex.get(i)));
		}
		return tileString;
	}
	private List<String> shuffleForLegalPengofThree() {
		List<Integer> tileIndex = new ArrayList<Integer>();
		for (int i = 0; i < 136; i++) {
			tileIndex.add(i);
		}
		Collections.shuffle(tileIndex);
		putToGivenPosition(tileIndex, 0, 26);
		putToGivenPosition(tileIndex, 9, 27);
		putToGivenPosition(tileIndex, 18, 52);
		List<String> tileString = new ArrayList<String>();
		for (int i = 0; i < tileIndex.size(); i++) {
			tileString.add(MahJongLogic.tileIdToString(tileIndex.get(i)));
		}
		return tileString;
	}
	private List<String> shuffleForLegalChiofTwo() {
		List<Integer> tileIndex = new ArrayList<Integer>();
		for (int i = 0; i < 136; i++) {
			tileIndex.add(i);
		}
		Collections.shuffle(tileIndex);
		putToGivenPosition(tileIndex, 2, 13);
		putToGivenPosition(tileIndex, 3, 14);
		putToGivenPosition(tileIndex, 4, 52);
			List<String> tileString = new ArrayList<String>();
		for (int i = 0; i < tileIndex.size(); i++) {
			tileString.add(MahJongLogic.tileIdToString(tileIndex.get(i)));
		}
		return tileString;
	}

	private void addTileInfoToMap(Map<String, Object> target,
			List<String> tileInfo) {
		for (int i = 0; i < tileInfo.size(); i++) {
			target.put(T + i, tileInfo.get(i));
		}
	}

	private List<Integer> getIndicesInRange(int fromInclusive, int toInclusive) {
		return MahJongLogic.getIndicesInRange(fromInclusive, toInclusive);
	}

	private List<Integer> getEmpty() {
		return new ArrayList<Integer>();
	}
	private ImmutableMap<String,Object> getEmptyMap()
	{
		return ImmutableMap.<String,Object> of();
	}

	private VerifyMove move(int lastMovePlayerId,
			Map<String, Object> lastState, List<Operation> lastMove) {
		return new VerifyMove(playersInfo,
		// in cheat we never need to check the resulting state (the server makes
		// it, and the game
		// doesn't have any hidden decisions such in Battleships)
				emptyState, lastState, lastMove, lastMovePlayerId,
				ImmutableMap.<Integer, Integer> of());

	}


	//Below are test cases
	
	@Test
	public void testNormalPeng() {
		//test normal peng operation
		assertMoveOk(move(bId, validStateBeforePeng, PengofTwo));
	}

	@Test
	public void testInvalidPengofThree() {
		ImmutableList<Operation> pengOfThree = ImmutableList.<Operation> of(
				new SetTurn(cId),
				new Set(M, ImmutableList.<String> of(P, "a1")), new Set(TU,
						ImmutableList.<Integer> of()), new Set(AtHandThree,
						getIndicesInRange(28, 38)), new Set(DeclaredThree,
						ImmutableList.<Integer> of(26, 27, 52)),
				new SetVisibility("T26"), new SetVisibility("T27"),
				new SetVisibility("T52"));
        //test invalid peng operation with wrong peng player
		assertHacker(move(cId, ValidStateBeforePeng2, pengOfThree));
	}
	@Test
	public void testValidPengofThree() {
		ImmutableList<Operation> pengOfThree = ImmutableList.<Operation> of(
				new SetTurn(cId),
				new Set(M, ImmutableList.<String> of(P, "a1")), new Set(TU,
						ImmutableList.<Integer> of()), new Set(AtHandThree,
						getIndicesInRange(28, 38)), new Set(DeclaredThree,
						ImmutableList.<Integer> of(26, 27, 52)),
				new SetVisibility("T26"), new SetVisibility("T27"),
				new SetVisibility("T52"));
        //test valid peng operation
		assertMoveOk(move(cId, validStateBeforePengForThree, pengOfThree));
	}

	@Test
	public void testInvalidPeng() {
		//test invalidpeng with wrong tiles
		assertHacker(move(aId, ValidStateBeforePeng2, InvalidPengofOne));
	}
	
	@Test
	public void testNormalGang() {
		//test normal gang
		assertMoveOk(move(bId, validStateBeforeGang, GangofTwo));
	}
	
	@Test
	public void testInvalidGang()
	{ 
		//test invalid gangoperation with invalid previous state
		assertHacker(move(bId, InvalidStateBeforeGang, GangofTwo));
		//test hacker with wrong gang tiles
		assertHacker(move(bId, validStateBeforePeng, GangofTwo));
	}
	
	@Test
	public void testNormalChi()
	{
		//Test normal chi operation
		assertMoveOk(move(bId, validStateBeforeChi, ChiofTwo));
	}
	@Test 
	public void testInvalidChi()
	{
		assertHacker(move(bId, validStateBeforePeng, GangofTwo));
	}
	@Test
	public void testRefuseGang()
	{
		//test valid refusegang operation
		assertMoveOk(move(bId, validStateBeforeRefuseGang, RefuseGangofTwo));
		assertMoveOk(move(cId, validStateBeforeRefuseGang2, RefuseGangofThree));
		//test invalid refusegang operation with the wrong id and status
		assertHacker(move(cId, validStateBeforeRefuseGang, RefuseGangofTwo));
		assertHacker(move(aId, validStateBeforeRefuseGang, RefuseGangofOne));
	}
	@Test
	public void testInitialMove()
	{
		//test valid initializations
		assertMoveOk(move(aId,getEmptyMap(),validInitialMove()));
		//test invalid initializations with the wrong beginning id
		assertHacker(move(bId,getEmptyMap(),validInitialMove()));
		//test invalid initializations with extra move
		assertHacker(move(aId,getEmptyMap(),invalidInitialMoveWithExtraMove()));	
		
	}
	@Test
	public void testPick()
	{   
		//Test a valid pickup operation
		assertMoveOk(move(bId,buildValidStateBeforeickUp(),PickUpofTwo));
		//Test an invalid pickup operation with the wrong middle tiles
		assertHacker(move(bId,buildValidStateBeforeickUp(),PickUpofTwoWithWrongMiddle));
		//Test an invalid pickup operation with the wrong hand tiles
		assertHacker(move(bId,buildValidStateBeforeickUp(),PickUpofTwoWithWrongHand));
	}
	@Test
	public void testDiscard()
	{
		
		ImmutableMap<String, Object> buildValidStateBeforeDiscard=buildValidStateBeforeDiscard();
		List<Operation> DiscardOfTwo=DiscardOfTwo(buildValidStateBeforeDiscard);
		List<Operation> DiscardOfTwoWithWrongMiddle=DiscardOfTwoWithWrongMiddle(buildValidStateBeforeDiscard);
		//Test a valid discard operation
		assertMoveOk(move(bId,buildValidStateBeforeDiscard,DiscardOfTwo));
		//Test an invalid discard operation with wrong middle tiles
		assertHacker(move(bId,buildValidStateBeforeDiscard,DiscardOfTwoWithWrongMiddle));
	}
	
	@Test
	public void testCardIdToString() {
	  //Tile Id to String transformation
	  assertEquals("a1", MahJongLogic.tileIdToString(0));
	  assertEquals("a2", MahJongLogic.tileIdToString(1));
	  assertEquals("b1", MahJongLogic.tileIdToString(36));
	  assertEquals("c1", MahJongLogic.tileIdToString(72));
	  assertEquals("d0", MahJongLogic.tileIdToString(135));
	}
	@Test
	public void testEndGame(){
		//Game ends with no tile at wall
		assertMoveOk(move(aId,noTileAtWall,GameEndWithNoTile));
		//Invalid game ends when there is still tile at wall
		assertHacker(move(aId,oneTileAtWall,GameEndWithNoTile));
		//Game continues when there is no tile at wall;
		assertHacker(move(aId,noTileAtWall,GameContinues));
	}
	
	}
	

	

