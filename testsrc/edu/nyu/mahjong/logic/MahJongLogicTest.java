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

@RunWith(JUnit4.class)
public class MahJongLogicTest {
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
					new Set(M, ImmutableList.<String> of(P, "aONE")), new Set(
							TU, ImmutableList.<Integer> of()), new Set(
							AtHandOne, getIndicesInRange(2, 12)), new Set(
							DeclaredTwo, ImmutableList.<Integer> of(0, 1, 42)),
					new SetVisibility("T0"), new SetVisibility("T1"),
					new SetVisibility("T42"));
	private final ImmutableList<Operation> PengofTwo = ImmutableList
			.<Operation> of(
					new SetTurn(bId),
					new Set(M, ImmutableList.<String> of(P, "aONE")),
					new Set(TU, ImmutableList.<Integer> of()),
					new Set(AtHandTwo, getIndicesInRange(15, 25)),
					new Set(DeclaredTwo, ImmutableList.<Integer> of(13, 14, 42)),
					new SetVisibility("T13"), new SetVisibility("T14"),
					new SetVisibility("T42"));
	private final ImmutableList<Operation> GangofTwo = ImmutableList
			.<Operation> of(
					new SetTurn(bId),
					new Set(M, ImmutableList.<String> of(G, "aONE")),
					new Set(TU, ImmutableList.<Integer> of()),
					new Set(AtHandTwo, getIndicesInRange(16, 25)),
					new Set(DeclaredTwo, ImmutableList.<Integer> of(13,14,15,42)),
					new SetVisibility("T13"), new SetVisibility("T14"),
					new SetVisibility("T15"),
					new SetVisibility("T42"));
	private final ImmutableList<Operation> RefuseGangofTwo = ImmutableList
			.<Operation> of(
					new SetTurn(cId),
					new Set(M, ImmutableList.<String> of(RG,String.valueOf(aId))));
	
	private MahJongLogic mahjong;
	private ImmutableMap<String, Object> validStateBeforePeng;
	private ImmutableMap<String, Object> validStateBeforePengForThree;
	private ImmutableMap<String, Object> validStateBeforeGang;
	private ImmutableMap<String, Object> InvalidStateBeforeGang;
	private ImmutableMap<String, Object> validStateBeforeRefuseGang;

	@Before
	public void setUp() throws Exception {
		mahjong = new MahJongLogic();

		Map<String, Object> mockMap = new HashMap<String, Object>();
		mockMap.put(TURN, PlayerOne);
		mockMap.put(AtHandOne, getIndicesInRange(0, 12));
		mockMap.put(AtHandTwo, getIndicesInRange(13, 25));
		mockMap.put(AtHandThree, getIndicesInRange(26, 38));
		mockMap.put(AtHandFour, getIndicesInRange(39, 41));
		mockMap.put(DeclaredOne, getEmpty());
		mockMap.put(DeclaredTwo, getEmpty());
		mockMap.put(DeclaredThree, getEmpty());
		mockMap.put(DeclaredFour, getEmpty());
		mockMap.put(TAW, getIndicesInRange(43, 135));
		mockMap.put(TU, getIndicesInRange(42, 42));
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
		
		
		
	}

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
		putToGivenPosition(tileIndex, 18, 42);
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
		putToGivenPosition(tileIndex, 18, 42);
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
		putToGivenPosition(tileIndex, 18, 42);
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

	private VerifyMove move(int lastMovePlayerId,
			Map<String, Object> lastState, List<Operation> lastMove) {
		return new VerifyMove(playersInfo,
		// in cheat we never need to check the resulting state (the server makes
		// it, and the game
		// doesn't have any hidden decisions such in Battleships)
				emptyState, lastState, lastMove, lastMovePlayerId,
				ImmutableMap.<Integer, Integer> of());

	}

	// Initial states and final states
	/*
	 * private final Map<String, Object> InitialState= ImmutableMap.<String,
	 * Object>of( TURN, PlayerOne, AtHandOne, getIndicesInRange(0, 12),
	 * AtHandTwo, getIndicesInRange(13,25), AtHandThree,
	 * getIndicesInRange(26,38), AtHandFour, getIndicesInRange(39,41),
	 * DeclaredOne, getEmpty(), DeclaredTwo, getEmpty(), DeclaredThree,
	 * getEmpty(), DeclaredFour, getEmpty(), TilesAtWall,
	 * getIndicesInRange(42,135), TilesUsed, getEmpty()); private final
	 * Map<String, Object> InitialStateAfterPickUp= ImmutableMap.<String,
	 * Object>of( TURN, PlayerOne, AtHandOne, combine(getIndicesInRange(0,
	 * 12),getIndicesInRange(42,42)), AtHandTwo, getIndicesInRange(13,25),
	 * AtHandThree, getIndicesInRange(26,38), AtHandFour,
	 * getIndicesInRange(39,41), DeclaredOne, getEmpty(), DeclaredTwo,
	 * getEmpty(), DeclaredThree, getEmpty(), DeclaredFour, getEmpty(),
	 * TilesAtWall, getIndicesInRange(43,135), TilesUsed, getEmpty()); private
	 * final Map<String, Object> NoTileAtWall= ImmutableMap.<String, Object>of(
	 * TURN, PlayerOne, AtHandOne, getIndicesInRange(0, 12), AtHandTwo,
	 * getIndicesInRange(13,25), AtHandThree, getIndicesInRange(26,38),
	 * AtHandFour, getIndicesInRange(39,41), DeclaredOne, getEmpty(),
	 * DeclaredTwo, getEmpty(), DeclaredThree, getEmpty(), DeclaredFour,
	 * getEmpty(), TilesUsed, getIndicesInRange(42,135), TilesAtWall,
	 * getEmpty());
	 */

	private ImmutableMap<String, Object> buildValidStateBeforePeng2() {
		Map<String, Object> image = new HashMap<String, Object>();
		image.put(TURN, PlayerTwo);
		image.put(AtHandOne, getIndicesInRange(0, 12));
		image.put(AtHandTwo, getIndicesInRange(13, 25));
		image.put(AtHandThree, getIndicesInRange(26, 38));
		image.put(AtHandFour, getIndicesInRange(39, 41));
		image.put(DeclaredOne, getEmpty());
		image.put(DeclaredTwo, getEmpty());
		image.put(DeclaredThree, getEmpty());
		image.put(DeclaredFour, getEmpty());
		image.put(TAW, getIndicesInRange(43, 135));
		image.put(TU, getIndicesInRange(42, 42));
		image.put(M, ImmutableList.of(RP, String.valueOf(bId)));
		addTileInfoToMap(image, shuffleForLegalPengofTwo());
		return ImmutableMap.copyOf(image);
	}

	private final ImmutableMap<String, Object> ValidStateBeforePeng2 = buildValidStateBeforePeng2();

	/*
	 * private final Map<String,Object> ValidStateBeforePeng=
	 * ImmutableMap.<String,Object> builder() .put(TURN, PlayerOne)
	 * .put(AtHandOne, getIndicesInRange(0, 12)) .put(AtHandTwo,
	 * getIndicesInRange(13,25)) .put(AtHandThree, getIndicesInRange(26,38))
	 * .put(AtHandFour, getIndicesInRange(39,41)) .put(DeclaredOne, getEmpty())
	 * .put(DeclaredTwo, getEmpty()) .put(DeclaredThree, getEmpty())
	 * .put(DeclaredFour, getEmpty()) .put(TilesAtWall,
	 * getIndicesInRange(43,135)) .put(TilesUsed, getIndicesInRange(42,42))
	 * .put(M,getCommand(WP,String.valueOf(bId))) .build();
	 */

	@Test
	public void testNormalPeng() {
		assertMoveOk(move(bId, validStateBeforePeng, PengofTwo));
	}

	@Test
	public void testInvalidPengofThree() {
		ImmutableList<Operation> pengOfThree = ImmutableList.<Operation> of(
				new SetTurn(cId),
				new Set(M, ImmutableList.<String> of(P, "aONE")), new Set(TU,
						ImmutableList.<Integer> of()), new Set(AtHandThree,
						getIndicesInRange(28, 38)), new Set(DeclaredThree,
						ImmutableList.<Integer> of(26, 27, 42)),
				new SetVisibility("T26"), new SetVisibility("T27"),
				new SetVisibility("T42"));

		assertHacker(move(cId, ValidStateBeforePeng2, pengOfThree));
	}
	@Test
	public void testValidPengofThree() {
		ImmutableList<Operation> pengOfThree = ImmutableList.<Operation> of(
				new SetTurn(cId),
				new Set(M, ImmutableList.<String> of(P, "aONE")), new Set(TU,
						ImmutableList.<Integer> of()), new Set(AtHandThree,
						getIndicesInRange(28, 38)), new Set(DeclaredThree,
						ImmutableList.<Integer> of(26, 27, 42)),
				new SetVisibility("T26"), new SetVisibility("T27"),
				new SetVisibility("T42"));

		assertMoveOk(move(cId, validStateBeforePengForThree, pengOfThree));
	}

	@Test
	public void testInvalidPeng() {
		assertHacker(move(aId, ValidStateBeforePeng2, InvalidPengofOne));
	}
	
	@Test
	public void testNormalGang() {
		assertMoveOk(move(bId, validStateBeforeGang, GangofTwo));
	}
	
	@Test
	public void testInvalidGang()
	{
		assertHacker(move(bId, InvalidStateBeforeGang, GangofTwo));
		assertHacker(move(bId, validStateBeforePeng, GangofTwo));
	}
	
	@Test
	public void testRefuseGang()
	{
		assertMoveOk(move(bId, validStateBeforeRefuseGang, RefuseGangofTwo));
		
	}
	}
	
	/*
	 * private final ImmutableList<Operation> PickOfOne=
	 * ImmutableList.<Operation> of( new
	 * Set(AtHandOne,combine(getIndicesInRange(0,12),getIndicesInRange(42,42))),
	 * new Set(TilesAtWall,getIndicesInRange(43,135)), new
	 * Set(PickUp,ImmutableList.of(42)) ); private final
	 * ImmutableList<Operation> ThrowOfOne= ImmutableList.<Operation> of( new
	 * Set(AtHandOne,combine(getIndicesInRange(0,12),getIndicesInRange(42,42))),
	 * new Set(TilesAtWall,getIndicesInRange(43,135)), new
	 * Set(PickUp,ImmutableList.of(42)), new
	 * Set(Turn,mahjong.nextPlayer(PlayerOne)), new
	 * Set(AtHandOne,getIndicesInRange(0,11)), new
	 * Set(TilesUsed,getIndicesInRange(12,12)), new
	 * Set(Throw,ImmutableList.of(12))
	 * 
	 * ); private final ImmutableList<Operation> illegalPickOfOnewithWrongHand=
	 * ImmutableList.<Operation> of( new
	 * Set(AtHandOne,combine(getIndicesInRange(0,12),getIndicesInRange(42,43))),
	 * new Set(TilesAtWall,getIndicesInRange(44,135)), new
	 * Set(PickUp,ImmutableList.of(42))
	 * 
	 * ); private final ImmutableList<Operation> illegalPickOfOnewithWrongWall=
	 * ImmutableList.<Operation> of( new
	 * Set(AtHandOne,combine(getIndicesInRange(0,12),getIndicesInRange(42,42))),
	 * new Set(TilesAtWall,getIndicesInRange(44,135)), new
	 * Set(PickUp,ImmutableList.of(42)) ); private final
	 * ImmutableList<Operation> illegalPickOfOnewithWrongMatch=
	 * ImmutableList.<Operation> of( new
	 * Set(AtHandOne,combine(getIndicesInRange(0,12),getIndicesInRange(42,42))),
	 * new
	 * Set(TilesAtWall,combine(getIndicesInRange(0,42),getIndicesInRange(44,135
	 * ))), new Set(PickUp,ImmutableList.of(42)) ); private final
	 * ImmutableList<Operation> illegalThrowOfOnewithWrongHand=
	 * ImmutableList.<Operation> of( new
	 * Set(AtHandOne,combine(getIndicesInRange(0,12),getIndicesInRange(42,42))),
	 * new Set(TilesAtWall,getIndicesInRange(43,135)), new
	 * Set(PickUp,ImmutableList.of(42)), new
	 * Set(Turn,mahjong.nextPlayer(PlayerOne)), new
	 * Set(AtHandOne,getIndicesInRange(0,11)), new
	 * Set(TilesUsed,getIndicesInRange(12,13)), new
	 * Set(Throw,ImmutableList.of(12,13))
	 * 
	 * ); private final ImmutableList<Operation> illegalThrowOfOnewithWrongWall=
	 * ImmutableList.<Operation> of( new
	 * Set(AtHandOne,combine(getIndicesInRange(0,12),getIndicesInRange(42,42))),
	 * new Set(TilesAtWall,getIndicesInRange(43,135)), new
	 * Set(PickUp,ImmutableList.of(42)), new
	 * Set(Turn,mahjong.nextPlayer(PlayerOne)), new
	 * Set(AtHandOne,getIndicesInRange(0,12)), new
	 * Set(TilesUsed,getIndicesInRange(14,14)), new
	 * Set(Throw,ImmutableList.of(12)) ); private final ImmutableList<Operation>
	 * PengOfTwo= ImmutableList.<Operation> of( new
	 * Set(AtHandOne,getIndicesInRange(0,12)), new
	 * Set(TilesUsed,getIndicesInRange(13,13)), new
	 * Set(Throw,ImmutableList.of(13)), new
	 * Set(DeclaredTwo,getIndicesInRange(13,15)), new
	 * Set(AtHandTwo,getIndicisInRange(16,25)), new Set(TileUsed,getEmpty()),
	 * new SetVisibility("13"), new SetVisibility("14"), new
	 * SetVisibility("15"), new Set(Peng,ImmutableList.of(13,14,15)) ); private
	 * final ImmutableList<Operation> illegalPengOfTwo=
	 * ImmutableList.<Operation> of( new Set(AtHandOne,getIndicesInRange(0,12)),
	 * new Set(TilesUsed,getIndicesInRange(13,13)), new
	 * Set(Throw,ImmutableList.of(13)), new
	 * Set(DeclaredTwo,combine(getIndicesInRange
	 * (14,15),getIndicesInRange(16,16))), new
	 * Set(AtHandTwo,getIndicisInRange(17,25)), new Set(TileUsed,getEmpty()),
	 * new SetVisibility("16"), new SetVisibility("14"), new
	 * SetVisibility("15"), new Set(Peng,ImmutableList.of(13,14,15)) ); private
	 * final ImmutableList<Operation> ChiOfTwo=ImmutableList.<Operation> of( new
	 * Set(AtHandOne,getIndicesInRange(0,12)), new
	 * Set(TilesUsed,getIndicesInRange(13,13)), new
	 * Set(Throw,ImmutableList.of(13)), new
	 * Set(DeclaredTwo,getIndicesInRange(13,15)), new
	 * Set(AtHandTwo,getIndicisInRange(16,25)), new Set(TileUsed,getEmpty()),
	 * new SetVisibility("13"), new SetVisibility("14"), new
	 * SetVisibility("15"), new Set(Chi,ImmutableList.of(13,14,15)) ); private
	 * final ImmutableList<Operation>
	 * illegalChiOfThree=ImmutableList.<Operation> of( new
	 * Set(AtHandOne,getIndicesInRange(0,12)), new
	 * Set(TilesUsed,getIndicesInRange(13,13)), new
	 * Set(Throw,ImmutableList.of(13)), new
	 * Set(DeclaredThree,getIndicesInRange(13,15)), new
	 * Set(AtHandThree,getIndicisInRange(16,25)), new Set(TileUsed,getEmpty()),
	 * new SetVisibility("13"), new SetVisibility("14"), new
	 * SetVisibility("15"), new Set(Chi,ImmutableList.of(13,14,15)) ); private
	 * final List<Operation> PengChiCombined=ImmutableList.<Operation>of ( new
	 * Set(AtHandOne,getIndicesInRange(0,12)), new
	 * Set(TilesUsed,getIndicesInRange(42,42)), new
	 * Set(Throw,ImmutableList.of(12)), new
	 * Set(DeclaredTwo,getIndicesInRange(12,14)), new
	 * Set(AtHandTwo,getIndicesInRange(15,25)), new Set(TileUsed,getEmpty()),
	 * new SetVisibility("12"), new SetVisibility("13"), new
	 * SetVisibility("14"), new Set(Peng,ImmutableList.of(12,13,14)), new
	 * Set(AtHandTwo,getIndicesInRange(16,25)), new
	 * Set(TilesUsed,getIndicesInRange(15,15)), new
	 * Set(Throw,ImmutableList.of(15)), new
	 * Set(DeclaredThree,combine(getIndicesInRange
	 * (15,15),getIndicesInRange(26,27))), new
	 * Set(AtHandThree,getIndicesInRange(28,38)), new Set(TileUsed,getEmpty()),
	 * new SetVisibility("15"), new SetVisibility("26"), new
	 * SetVisibility("27"), new Set("turn",NextPlayer(PlayerThree)));
	 * 
	 * private VerifyMove move(int lastMovePlayerId, Map<String, Object>
	 * lastState, List<Operation> lastMove) { return new VerifyMove(aId,
	 * playersInfo, // in cheat we never need to check the resulting state (the
	 * server makes it, and the game // doesn't have any hidden decisions such
	 * in Battleships) emptyState, lastState, lastMove, lastMovePlayerId); }
	 * 
	 * @Test public void testGetIndicesInRange() {
	 * 
	 * assertEquals(ImmutableList.of(3, 4,5,6), cheatLogic.getIndicesInRange(3,
	 * 6)); }
	 * 
	 * private static final mahjong.tile threeWan=new
	 * mahjong.tile(Suit.Wcharacters,3); private static final mahjong.tile
	 * FiveCircle=new mahjong.tile(Suit.Circles,5); private static final
	 * mahjong.tile South=new mahjong.tile (Suit.East,2);
	 * 
	 * @Test public void testCardToTile() {
	 * assertEquals(threeWan,cardIdToTile(12));
	 * assertEquals(FiveCircle,cardIdToTile(23));
	 * assertEquals(South,cardIdToTile(29)); } private List<Operation>
	 * getInitialOperations() { return mahjong.getInitialMove(aId, bId,cId,dId);
	 * }
	 * 
	 * @Test public void testInitialMove() { assertMoveOk(move(aId, emptyState,
	 * getInitialOperations())); }
	 * 
	 * @Test public void testInitialMoveByWrongPlayer() { assertHacker(move(bId,
	 * emptyState, getInitialOperations())); }
	 * 
	 * @Test public void testInitialMoveFromNonEmptyState() {
	 * assertHacker(move(aId, nonEmptyState, getInitialOperations())); }
	 * 
	 * @Test public void testInitialMoveWithExtraOperation() { List<Operation>
	 * initialOperations = getInitialOperations(); initialOperations.add(new
	 * Set(PlayerOne, ImmutableList.of())); assertHacker(move(aId, emptyState,
	 * initialOperations)); }
	 * 
	 * @Test public void testNormalPick() { assertMoveOk(move(aId, InitialState
	 * , PickOfOne)); }
	 * 
	 * @Test public void testNormalThrow() { assertMoveOk(move(aId, InitialState
	 * , ThrowOfOne)); }
	 * 
	 * @Test public void testNormalPeng() { assertMoveOk(move(aId,
	 * InitialStateAfterPickUp , PengOfTwo)); }
	 * 
	 * @Test public void testNormalChi() { assertMoveOk(move(aId,
	 * InitialStateAfterPickUp , ChiOfTwo)); }
	 * 
	 * @Test public void testNormalPengChiCombined() { assertMoveOk(move(aId,
	 * InitialStateAfterPickUp , PengChiCombined)); }
	 * 
	 * @Test public void testIllegalPick() { assertHakcer(move(aId, InitialState
	 * , illegalPickOfOnewithWrongHand)); assertHakcer(move(aId, InitialState ,
	 * illegalPickOfOnewithWrongWall)); assertHakcer(move(aId, InitialState ,
	 * illegalPickOfOnewithWrongMatch));
	 * 
	 * }
	 * 
	 * @Test public void testIllegalThrow() { assertHakcer(move(aId,
	 * InitialState , illegalThrowOfOnewithWrongHand)); assertHakcer(move(aId,
	 * InitialState , illegalThrowOfOnewithWrongWall)); assertHakcer(move(aId,
	 * InitialState , illegalPickOfOnewithWrongMatch)); }
	 * 
	 * @Test public void testIllegalPeng() { assertHakcer(move(aId, InitialState
	 * , illegalPengOfTwo));
	 * 
	 * }
	 * 
	 * @Test public void testIllegalChi() { assertHakcer(move(aId, InitialState
	 * , illegalChiOfThree));
	 * 
	 * }
	 * 
	 * 
	 * 
	 * private List<Integer> getIndicesInRange(int fromInclusive, int
	 * toInclusive) { return MahJongLogic.getIndicesInRange(fromInclusive,
	 * toInclusive); } private List<Integer> combine(List<Integer>
	 * former,List<Integer> latter) { List<Integer> result=new List<Integer> ();
	 * for (int element:former) result.add(element); for (int element:latter)
	 * result.add(element); return result; } private List<Integer> getEmpty() {
	 * return new ArrayList<Integer> (); } private List<String>
	 * getCommand(String a,String b) { List<String> result=new ArrayList<String>
	 * (); result.add(a); result.add(b); return result; }
	 * 
	 * @Test public void testTotalTileSize() { VerifyMove verifyMove=move(aId,
	 * InitialStateAfterPickUp, PengOfTwo);
	 * assertEquals(136,verifyMove.getTileSize());
	 * verifyMove=move(aId,InitialStateAfterPickUp,ChiOfTwo);
	 * assertEquals(136,verifyMove.getTileSize());
	 * verifyMove=move(PlayerOne,InitialStateAfterPickUp,PengChiCombined);
	 * assertEquals(136,verifyMove.getTileSize()); }
	 * 
	 * 
	 * @Test public void testValidPeng() {
	 * assertEquals(true,mahjong.checkPeng(ImmutableList.of(1,1,1)));
	 * assertEquals(true,mahjong.checkPeng(ImmutableList.of(5,5,5)));
	 * 
	 * }
	 * 
	 * @Test public void testInvalidPeng() {
	 * assertEquals(false,mahjong.checkPeng(ImmutableList.of(1,1,2)));
	 * assertEquals(false,mahjong.checkPeng(ImmutableList.of(5,6,6)));
	 * 
	 * }
	 * 
	 * @Test public void testValidChi() {
	 * assertEquals(true,mahjong.checkChi(ImmutableList.of(1,2,3)));
	 * assertEquals(true,mahjong.checkChi(ImmutableList.of(6,5,7)));
	 * 
	 * }
	 * 
	 * @Test public void testInValidChi() {
	 * assertEquals(false,mahjong.checkChi(ImmutableList.of(1,1,3)));
	 * assertEquals(false,mahjong.checkChi(ImmutableList.of(6,9,7)));
	 * 
	 * }
	 * 
	 * @Test //Illegal Cases public void testValidHu() {
	 * assertEquals(true,mahjong
	 * .checkHu(ImmutableList.of(1,1,1,2,2,3,4,5,6,6,6,10,10,10)));
	 * assertEquals(
	 * true,mahjong.checkHu(ImmutableList.of(1,1,1,2,2,3,3,3,6,6,6,10,10,10)));
	 * }
	 * 
	 * @Test //Legal Cases public void testInValidHu() {
	 * assertEquals(false,mahjong
	 * .checkHu(ImmutableList.of(1,1,1,2,3,3,4,5,6,6,6,10,10,10)));
	 * assertEquals(
	 * false,mahjong.checkHu(ImmutableList.of(1,1,1,2,2,3,4,5,6,6,8,10,10,10)));
	 * }
	 * 
	 * @Test public void testGameEnd() {
	 * assertEquals(true,GameEnd(NoTileAtWall)); }
	 * 
	 * @Test public void testGameNotEnd() {
	 * assertEquals(false,GameEnd(InitialState));
	 * assertEquals(false,GameEnd(InitialStateAfterPickUp)); } private final
	 * Map<String, Object> IntermediateState= ImmutableMap.<String, Object>of(
	 * TURN, PlayerOne, AtHandOne, getIndicesInRange(0, 12), AtHandTwo,
	 * getIndicesInRange(13,25), AtHandThree, getIndicesInRange(26,38),
	 * AtHandFour, getIndicesInRange(39,41), DeclaredOne, getEmpty(),
	 * DeclaredTwo, getEmpty(), DeclaredThree, getEmpty(), DeclaredFour,
	 * getEmpty(), TilesAtWall, getIndicesInRange(42,50), TilesUsed,
	 * getEmpty());
	 * 
	 * @Test public void gameInvalidContinue() {
	 * assertHakcer(move(aId,NoTileAtWall, PickOfOne));
	 * assertHakcer(move(aId,NoTileAtWall, ThrowOfOne));
	 * assertMoveOk(move(aId,IntermediateState,PickOfOne)); }
	 * 
	 * @Test public void InvalidGameEnd() { assertHakcer(move(aId,InitialState,
	 * StopGameWithHu)); }
	 * 
	 * 
	 * @Test public void gameValidStop() { assertMoveOk(move(aId,NoTileAtWall,
	 * StopGameWithTie));
	 * assertMoveOk(move(aId,IntermediateState,StopGameWithHu)); }
	 * 
	 * private final ImmutableList<Operation>
	 * StopGameWithTie=ImmutableList.<Operation>of (new EndGame(0)); private
	 * final ImmutableList<Operation> StopGameWithHu=ImmutableList.<Operation>of
	 * ( new Set(Hu,ImmutableList.of(1)), new EndGame(1));
	 */

