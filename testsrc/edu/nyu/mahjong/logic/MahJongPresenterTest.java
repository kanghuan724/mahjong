package edu.nyu.mahjong.logic;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.nyu.mahjong.logic.MahJongPresenter.MahJongMessage;
import edu.nyu.mahjong.logic.MahJongPresenter.View;
import edu.nyu.mahjong.logic.MahJongLogic.*;

import org.cheat.client.GameApi;
import org.cheat.client.GameApi.Container;
import org.cheat.client.GameApi.Operation;
import org.cheat.client.GameApi.SetTurn;
import org.cheat.client.GameApi.UpdateUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/** Tests for {@link MahJongPresenter}.
 * Test plan:
 * There are several interesting states:
 * 1) empty state
 * 2) empty tileUsed (no previous discard)
 * 3) non-empty tileUsed
 * 4) game-over
 * There are several interesting yourPlayerId:
 * 1) 1/2/..
 * 2) viewer
 * For each one of these states and for each yourPlayerId,
 * I will test what methods the presenters calls on the view and container.
 * In addition I will also test the interactions between the presenter and view, i.e.,
 * the view can call one of these methods:
 * 1) tileSelected
 * 2) tileDiscarded
 * 3) hu/gang/peng/chi
 */
@RunWith(JUnit4.class)
public class MahJongPresenterTest {
  /** The class under test. */
  private MahJongPresenter mahJongPresenter;
  private final MahJongLogic mahJongLogic = new MahJongLogic();
  private View mockView;
  private Container mockContainer;
  private MahJongState mahJongState;

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
  private final int viewerId = GameApi.VIEWER_ID;
  private final int aId = 0;
  private final int bId = 1;
  private final int cId = 2;
  private final int dId = 3;
  private final ImmutableList<Integer> playerIds = ImmutableList.of(aId, bId, cId, dId);
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
	
	/* The interesting states that I'll test. */
  private final ImmutableMap<String, Object> emptyState = ImmutableMap.<String, Object> of();
  private final ImmutableMap<String, Object> emptyTileUsedState = buildStateEmptyTileUsed();
  private final ImmutableMap<String, Object> nonEmptyTileUsedState = buildStateNonEmptyTileUsed();
  private final ImmutableMap<String, Object> gameOverState = buildStateGameOver();

  
	private ImmutableMap<String, Object> buildStateEmptyTileUsed() {
		Map<String, Object> image = new HashMap<String, Object>();
		image.put(TURN, PlayerOne);
		image.put(AtHandOne, MahJongLogic.concat(getIndicesInRange(0, 12),getIndicesInRange(52, 52)));
		image.put(AtHandTwo, getIndicesInRange(13, 25));
		image.put(AtHandThree, getIndicesInRange(26, 38));
		image.put(AtHandFour, getIndicesInRange(39, 51));
		image.put(DeclaredOne, getEmpty());
		image.put(DeclaredTwo, getEmpty());
		image.put(DeclaredThree, getEmpty());
		image.put(DeclaredFour, getEmpty());
		image.put(TAW, getIndicesInRange(53, 135));
		image.put(TU, getEmpty());
		image.put(M, ImmutableList.of(PU, String.valueOf(aId)));
		for (int i=0;i<136;i++)
			image.put(T+i,MahJongLogic.tileIdToString(i));
		return ImmutableMap.copyOf(image);

	}
	
	private ImmutableMap<String, Object> buildStateNonEmptyTileUsed() {
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
		image.put(TAW, getIndicesInRange(54, 135));
		image.put(TU, getIndicesInRange(52, 53));
		for (int i=0;i<136;i++)
			image.put(T+i,MahJongLogic.tileIdToString(i));
		image.put(M, ImmutableList.of(D, MahJongLogic.tileIdToString(52)));
		return ImmutableMap.copyOf(image);

	}
	
	private ImmutableMap<String, Object> buildStateGameOver() {
		Map<String, Object> image = new HashMap<String, Object>();
		image.put(TURN, PlayerOne);
		image.put(AtHandOne, getIndicesInRange(0, 12));
		image.put(AtHandTwo, getIndicesInRange(13, 25));
		image.put(AtHandThree, getIndicesInRange(26, 38));
		image.put(AtHandFour, getIndicesInRange(39, 51));
		image.put(DeclaredOne, getEmpty());
		image.put(DeclaredTwo, getEmpty());
		image.put(DeclaredThree, getEmpty());
		image.put(DeclaredFour, getEmpty());
		image.put(TAW, getEmpty());
		image.put(TU, getIndicesInRange(52, 135));
		image.put(M, ImmutableList.of(WG, String.valueOf(aId)));
		for (int i=0;i<136;i++)
			image.put(T+i,MahJongLogic.tileIdToString(i));
		return ImmutableMap.copyOf(image);

	}

  @Before
  public void runBefore() {
    mockView = Mockito.mock(View.class);
    mockContainer = Mockito.mock(Container.class);
    mahJongPresenter = new MahJongPresenter(mockView, mockContainer);
    verify(mockView).setPresenter(mahJongPresenter);
  }

  @After
  public void runAfter() {
    // This will ensure I didn't forget to declare any extra interaction the mocks have.
    verifyNoMoreInteractions(mockContainer);
    verifyNoMoreInteractions(mockView);
  }

  @Test
  public void testEmptyStateFor1() {
    mahJongPresenter.updateUI(createUpdateUI(aId, 0, emptyState));
    verify(mockContainer).sendMakeMove(mahJongLogic.getInitialMove(playerIds));
  }

  @Test
  public void testEmptyStateFor2() {
    mahJongPresenter.updateUI(createUpdateUI(bId, 0, emptyState));
  }

  @Test
  public void testEmptyStateForViewer() {
    mahJongPresenter.updateUI(createUpdateUI(viewerId, 0, emptyState));
  }

  @Test
  public void testEmptyTileUsedFor1TurnOf1() {
	mahJongState=MahJongLogic.gameApiStateToMahJongState(emptyTileUsedState,aId,playerIds);
    mahJongPresenter.updateUI(createUpdateUI(aId, aId, emptyTileUsedState));    

    
    verify(mockView).setPlayerState(13, 13, 13, getEmptyTile(), getEmptyTile(), getEmptyTile(),
    		83, getEmptyTile(),
    		getTiles(MahJongLogic.concat(getIndicesInRange(0, 12), getIndicesInRange(52, 52))), 
    		getEmptyTile(), MahJongMessage.INVISIBLE);
    //verify(mockView).chooseTile(ImmutableList.<Tile>of().get(0), getTiles(getIndicesInRange(0, 12)));
  }

  

  @Test
  public void testEmptyTileUsedStateFor2TurnOf1() {
    mahJongPresenter.updateUI(createUpdateUI(bId, aId, emptyTileUsedState));
    verify(mockView).setPlayerState(14, 13, 13, getEmptyTile(), getEmptyTile(), getEmptyTile(),
    		83, getEmptyTile(),
    		getTiles(13, 26), 
    		getEmptyTile(), MahJongMessage.INVISIBLE);
  }

  @Test

  public void testEmptyMiddleStateForViewerTurnOf1() {
	mahJongState=MahJongLogic.gameApiStateToMahJongState(emptyTileUsedState,aId,playerIds);
    mahJongPresenter.updateUI(createUpdateUI(viewerId, aId, emptyTileUsedState));
    verify(mockView).setViewerState(14, 13, 13, 13, 
    		getEmptyTile(), getEmptyTile(), getEmptyTile(), getEmptyTile(),
    		83, getEmptyTile(),
    		MahJongMessage.INVISIBLE);
  }

  @Test

  public void testNonEmptyMiddleStateFor1TurnOf2() {
	mahJongState=MahJongLogic.gameApiStateToMahJongState(nonEmptyTileUsedState,aId,playerIds);

    mahJongPresenter.updateUI(createUpdateUI(aId, bId, nonEmptyTileUsedState));
    verify(mockView).setPlayerState(13, 13, 13, 
    		getEmptyTile(), getEmptyTile(), getEmptyTile(),
    		82, getTiles(52,54),
    		getTiles(0, 13), getEmptyTile(),
    		MahJongMessage.INVISIBLE);
  }

  @Test

  public void testNonEmptyTileUsedFor2TurnOf2() {
	mahJongState=MahJongLogic.gameApiStateToMahJongState(nonEmptyTileUsedState,aId,playerIds);

    mahJongPresenter.updateUI(createUpdateUI(bId, bId, nonEmptyTileUsedState));
    verify(mockView).setPlayerState(13, 13, 13, 
    		getEmptyTile(), getEmptyTile(), getEmptyTile(),
    		82, getTiles(52, 54),
    		getTiles(13, 26), getEmptyTile(),
    		MahJongMessage.INVISIBLE);
  }

  @Test

  public void testNonEmptyMiddleStateForViewerTurnOf2() {
	mahJongState=MahJongLogic.gameApiStateToMahJongState(nonEmptyTileUsedState,aId,playerIds);

    mahJongPresenter.updateUI(createUpdateUI(viewerId, bId, nonEmptyTileUsedState));
    verify(mockView).setViewerState(13, 13, 13, 13,
    		getEmptyTile(), getEmptyTile(), getEmptyTile(), getEmptyTile(),
    		82, getTiles(52, 54), MahJongMessage.INVISIBLE);
  }

  @Test
  public void testGameOverStateFor1() {
	mahJongState=MahJongLogic.gameApiStateToMahJongState(gameOverState,aId,playerIds);
    mahJongPresenter.updateUI(createUpdateUI(aId, aId, gameOverState));
    verify(mockView).setPlayerState(13, 13, 13, getEmptyTile(), getEmptyTile(), getEmptyTile(),
    		0, getTiles(52, 136),
    		getTiles(0, 13), getEmptyTile(),
    		MahJongMessage.INVISIBLE);
  }

  @Test
  public void testGameOverStateFor2() {
	mahJongState=MahJongLogic.gameApiStateToMahJongState(gameOverState,aId,playerIds);
    mahJongPresenter.updateUI(createUpdateUI(bId, aId, gameOverState));
    verify(mockView).setPlayerState(13, 13, 13, getEmptyTile(), getEmptyTile(), getEmptyTile(),
    		0, getTiles(52, 136),
    		getTiles(13, 26), getEmptyTile(),
    		MahJongMessage.INVISIBLE);
  }

  @Test
  public void testGameOverStateForViewer() {
	mahJongState=MahJongLogic.gameApiStateToMahJongState(gameOverState,aId,playerIds);
    mahJongPresenter.updateUI(createUpdateUI(viewerId, aId, gameOverState));
    verify(mockView).setViewerState(13, 13, 13, 13, 
    		getEmptyTile(), getEmptyTile(), getEmptyTile(), getEmptyTile(),
    		0, getTiles(52, 136), MahJongMessage.INVISIBLE);
  }

  /* Tests for preparing a peng. */
  /*  @Test
    public void testNonEmptyTileUsedStateFor3TurnOf3PengAvailable() {
      UpdateUI updateUI = createUpdateUI(cId, cId, nonEmptyTileUsedState);
      MahJongState mahJongState =
          mahJongLogic.gameApiStateToMahJongState(updateUI.getState(), cId, playerIds);
      mahJongPresenter.updateUI(updateUI);
      List<Tile> myTiles = getTiles(26, 38);
      mahJongPresenter.pengAvailable(getTiles(53, 53).get(0),
    		  getTiles(26, 27));
      verify(mockView).setPlayerState(13, 13, 13, getEmptyTile(), getEmptyTile(), getEmptyTile(),
    		  82, getTiles(52, 53), getTiles(26, 38), getEmptyTile(), MahJongMessage.PENG);
      verify(mockView).pengAvailable(getTiles(53, 53).get(0), getTiles(26, 27));
      List<Integer> selectedComboIndex = Lists.newArrayList();
	  selectedComboIndex.add(26);
	  selectedComboIndex.add(27);
	  selectedComboIndex.add(53);
      verify(mockContainer).sendMakeMove(mahJongLogic.peng(mahJongState, 
    		  selectedComboIndex, mahJongState.getPlayerIds()));
    }*/

  private UpdateUI createUpdateUI(
      int yourPlayerId, int turnOfPlayerId, Map<String, Object> state) {
    // Our UI only looks at the current state
    // (we ignore: lastState, lastMovePlayerId, playerIdToNumberOfTokensInPot)
    return new UpdateUI(yourPlayerId, playersInfo, state,
        emptyState, // we ignore lastState
        ImmutableList.<Operation>of(new SetTurn(turnOfPlayerId)),
        0,
        ImmutableMap.<Integer, Integer>of());
  }
  
	private List<Integer> getIndicesInRange(int fromInclusive, int toInclusive) {
		return MahJongLogic.getIndicesInRange(fromInclusive, toInclusive);
	}

	private List<Integer> getEmpty() {
		return new ArrayList<Integer>();
	}
	
	private List<Tile> getEmptyTile() {
		return new ArrayList<Tile>();
	}
	
	  private List<Tile> getTiles(List<Integer> targetIndices) {
		  List<Tile> targetTiles = Lists.newArrayList();
		  ImmutableList<Optional<Tile>> tiles = mahJongState.getTiles();
		  for (Integer tileIndex : targetIndices) {
			  targetTiles.add(tiles.get(tileIndex).get());
		  }
		  return targetTiles;	  
	  }
	 
	  private List<Tile> getTiles(int fromInclusive, int toExclusive) {
		  List<Tile> tiles = Lists.newArrayList();
		  for (int i = fromInclusive; i < toExclusive; i++) {
			  Rank rank;
			  Suit suit;
				if (i < 108) {
					suit = Suit.values()[i / 36];
					rank = Rank.values()[i % 9 + 1];

				} else {
					rank = Rank.values()[0];
					suit =  Suit.values()[i / 4 - 24];
				}

			  tiles.add(new Tile(suit, rank));
		  }
		  return tiles;
   }
}