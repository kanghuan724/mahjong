package mahJong;

import static org.junit.Assert.*;

import mahJong.MahJongLogic;
import org.cheat.client.GameApi.Delete;
import org.cheat.client.GameApi.Operation;
import org.cheat.client.GameApi.Set;
import org.cheat.client.GameApi.SetVisibility;
import org.cheat.client.GameApi.Shuffle;
import org.cheat.client.GameApi.VerifyMove;
import org.cheat.client.GameApi.VerifyMoveDone;
import java.util.List;
import java.util.Map;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


public class MahJongTester {
	private static final String PlayerOne="1";
	private static final String PlayerTwo="2";
	private static final String PlayerThree="3";
	private static final String PlayerFour="4";
    private static final tile threeWan=new tile(Suit.Characters,3);
    private static final tile FiveCircle=new tile(Suit.Circles,5);
    private static final tile South=new tile (Suit.East,2);
    private final List<Operation> IllegalChiOne=ImmutableList.Operationof(
    		new Set("turn",PlayerOne),
    		new Set("Pickup",PickrandomFromWall(PlayerOne)),
    		new Set("Throwout",ImmutableList.of(PlayerOne,5)),
    		new Set("Chi",ImmutableList.of(PlayerThree,8,9,10)),
    		new Set("turn",PlayerThree),
    		new Set("Throwout",ImmutableList.of(PlayerThree,3)),
    		new Set("turn",NextPlayer())
        );
    private final List<Operation> IllegalChiTwo=ImmutableList.Operationof(
    		new Set("turn",PlayerOne),
    		new Set("Pickup",PickrandomFromWall(PlayerOne)),
    		new Set("Throwout",ImmutableList.of(PlayerOne,5)),
    		new Set("Chi",ImmutableList.of(PlayerOne,8,9,10)),
    		new Set("turn",PlayerOne),
    		new Set("Throwout",ImmutableList.of(PlayerOne,3)),
    		new Set("turn",NextPlayer())
        );
    private final List<Operation> IllegalCHiThree=ImmutableList.Operationof(
    		new Set("turn",PlayerOne),
    		new Set("Pickup",PickrandomFromWall(PlayerOne)),
    		new Set("Throwout",ImmutableList.of(PlayerOne,5)),
    		new Set("Chi",ImmutableList.of(PlayerTwo,8,9,15)),
    		new Set("turn",PlayerTwo),
    		new Set("Throwout",ImmutableList.of(PlayerTwo,3)),
    		new Set("turn",NextPlayer())
        );
    private final List<Operation> LegalPengOne=ImmutableList.<Operation>of(
    		new Set ("Peng",ImmutableList.of(PlayerThree,10,10,10)),
    		new Set("turn",PlayerThree);
    		
    		);
    private final List<Operation> LegalPengTwo=ImmutableList.<Operation>of(
    		new Set ("Peng",ImmutableList.of(PlayerThree,5,5,5)),
    		new Set("turn",PlayerThree);
    		
    		);
    private final List<Operation> IllegalPengOne=ImmutableList.<Operation>of(
    		new Set ("Peng",ImmutableList.of(PlayerThree,9,10,10)),
    		new Set("turn",PlayerThree);
    		
    		);
    private final List<Operation> IllegalPengTwo=ImmutableList.<Operation>of(
    		new Set ("Peng",ImmutableList.of(PlayerThree,3,4,5)),
    		new Set("turn",PlayerThree);
    		
    		);
    private final List<Operation> PengofThree=ImmutableList.<Operation>of(
    		new Set("turn",PlayerOne),
    		new Set("Pickup",PickrandomFromWall(PlayerOne)),
    		new Set("Throwout",ImmutableList.of(PlayerOne,5)),
    		new Set("Peng",ImmutableList.of(PlayerThree,10,10,10)),
    		new Set("turn",PlayerThree),
    		new Set("Throwout",ImmutableList.of(PlayerThree,4)),
    		new Set("turn",NextPlayer()));
    
    private final List<Operation> ChiofTwo=ImmutableList.<Operation>of(
    		new Set("turn",PlayerOne),
    		new Set("Pickup",PickrandomFromWall(PlayerOne)),
    		new Set("Throwout",ImmutableList.of(PlayerOne,5)),
    		new Set("Chi",ImmutableList.of(PlayerTwo,8,9,10)),
    		new Set("turn",PlayerTwo),
    		new Set("Throwout",ImmutableList.of(PlayerTwo,3)),
    		new Set("turn",NextPlayer()));
    private final List<Operation> PengChiCombined=ImmutableList.<Operation>of(
    		new Set("turn",PlayerOne),
    		new Set("Pickup",PickrandomFromWall(PlayerOne)),
    		new Set("Throwout",ImmutableList.of(PlayerOne,5)),
    		new Set("Peng",ImmutableList.of(PlayerThree,10,10,10)),
    		new Set("turn",PlayerThree),
    		new Set("Throwout",ImmutableList.of(PlayerThree,4)),
    		new Set("Chi",ImmutableList.of(PlayerFour,8,9,10)),
    		new Set("turn",PlayerFour),
    		new Set("Throwout",ImmutableList.of(PlayerFour,3)),
    		new Set("turn",NextPlayer()));
    private final Map<String, Object> EmptyTileAtWall= ImmutableMap.<String, Object>of(
  	      "turn", PlayerOne,
  	      "TilesAtHandOf1", getIndicesInRange(1001, 1014),
  	      "TilesDeclaredOf1", getIndicesInRange(1101,1114),
  	      "TilesAtHandOf2", getIndicesInRange(2001, 2014),
  	      "TilesDeclaredOf2", getIndicesInRange(2101,2114),
  	      "TilesAtHandOf3", getIndicesInRange(3001, 3014),
  	      "TilesDeclaredOf3", getIndicesInRange(3001,3014),
  	      "TilesAtHandOf4", getIndicesInRange(4001, 4014),
  	      "TilesDeclaredOf4", getIndicesInRange(4001,4014),
  	      "TilesAtWall", getIndicesInRange(0,0),
  	      "TilesUsed", getIndicesInRange(6001,6136);
  	      );
    private final Map<String, Object> turnOfOneBegin = ImmutableMap.<String, Object>of(
    	      "turn", PlayerOne,
    	      "TilesAtHandOf1", getIndicesInRange(1001, 1014),
    	      "TilesDeclaredOf1", getIndicesInRange(1101,1114),
    	      "TilesAtHandOf2", getIndicesInRange(2001, 2014),
    	      "TilesDeclaredOf2", getIndicesInRange(2101,2114),
    	      "TilesAtHandOf3", getIndicesInRange(3001, 3014),
    	      "TilesDeclaredOf3", getIndicesInRange(3001,3014),
    	      "TilesAtHandOf4", getIndicesInRange(4001, 4014),
    	      "TilesDeclaredOf4", getIndicesInRange(4001,4014),
    	      "TilesAtWall", getIndicesInRange(5001,5136),
    	      "TilesUsed", getIndicesInRange(6001,6136);
    	      );
    private final Map<String, Object> turnOfTwoBegin = ImmutableMap.<String, Object>of(
  	      "turn", 2,
  	      "TilesAtHandOf1", getIndicesInRange(1001, 1014),
  	      "TilesDeclaredOf1", getIndicesInRange(1101,1114),
  	      "TilesAtHandOf2", getIndicesInRange(2001, 2014),
  	      "TilesDeclaredOf2", getIndicesInRange(2101,2114),
  	      "TilesAtHandOf3", getIndicesInRange(3001, 3014),
  	      "TilesDeclaredOf3", getIndicesInRange(3001,3014),
  	      "TilesAtHandOf4", getIndicesInRange(4001, 4014),
  	      "TilesDeclaredOf4", getIndicesInRange(4001,4014),
  	      "TilesAtWall", getIndicesInRange(5001,5136),
  	      "TilesUsed", getIndicesInRange(6001,6136);
  	      );
    
	@Test	
	public void testCardToTile() {
		assertEquals(threeWan,cardIdToTile(12));
		assertEquals(FiveCircle,cardIdToTile(23));
		assertEquals(South,cardIdToTile(29));
	}
	
	@Test
	public void testTotalTileSize() {
		VerifyMove verifyMove=move(PlayerOne, turnOfOneBegin, PengofThree);
		assertEquals(136,verifyMove.getTileSize());
		verifyMove=move(PlayerOne,turnOfOneBegin,ChiofTwo);
		assertEquals(136,verifyMove.getTileSize());
		verifyMove=move(PlayerOne,turnOfOneBegin,PengChiCombined);
		assertEquals(136,verifyMove.getTileSize());
	}
	
	@Test
	public void testTileofPlayer()
	{
		VerifyMove verifyMove=move(PlayerOne, turnOfOneBegin, PengofThree);
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerOne));
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerTwo));
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerThree));
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerFour));
		verifyMove=move(PlayerOne,turnOfOneBegin,ChiofTwo);
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerOne));
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerTwo));
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerThree));
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerFour));
		verifyMove=move(PlayerOne,turnOfOneBegin,PengChiCombined);
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerOne));
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerTwo));
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerThree));
		assertEquals(13,verifyMove.getTileOfPlayer(PlayerFour));
	}
	@Test
	//Legal Cases
	public void testPengisLegal()
	{
		assertEquals(true,MoveIsLegal(move(PlayerOne,turnOfOneBegin,LegalPengOne)));
		assertEquals(true,MoveIsLegal(move(PlayerOne,turnOfOneBegin,LegalPengTwo)));
	}
	
	@Test
	//Illegal Cases
	public void testPengisIllegal()
	{
		assertEquals(false,MoveIsLegal(move(PlayerOne,turnOfOneBegin,IllegalPengOne)));
		assertEquals(false,MoveIsLegal(move(PlayerOne,turnOfOneBegin,IllegalPengTwo)));
	}
	@Test
	//Legal Cases
	public void testCHiisLegal()
	{
		assertEquals(true,MoveIsLegal(move(PlayerOne,turnOfOneBegin,ChiofTwo)));
	}
	@Test
	//Illegal Cases
	public void testCHiisIllegal()
	{
		assertEquals(false,MoveIsLegal(move(PlayerOne,turnOfOneBegin,IllegalChiOne)));
		assertEquals(false,MoveIsLegal(move(PlayerOne,turnOfOneBegin,IllegalChiTwo)));
		assertEquals(false,MoveIsLegal(move(PlayerOne,turnOfOneBegin,IllegalChiThree)));
	}
	
	@Test
	//Illegal Cases
	public void testHuIsLegal()
	{
		assertEquals(true,Hu(1,1,1,2,2,3,4,5,6,6,6,10,10,10));
		assertEquals(true,Hu(1,1,1,2,2,3,3,3,6,6,6,10,10,10));
	}
	
	@Test
	//Legal Cases
	public void testHuisIllegal()
	{
		assertEquals(false,Hu(1,1,1,2,3,3,4,5,6,6,6,10,10,10));
		assertEquals(false,Hu(1,1,1,2,2,3,4,5,6,6,8,10,10,10));
	}
	@Test
	//Legal Cases
	public void testGameEnd()
	{
		assertEquals(true,GameEnd(EmptyTileAtWall));
		
	}
	@Test
	public void testGameNotEnd()
	{
		assertEquals(false,GameEnd(turnOfOneBegin));
		assertEquals(false,GameEnd(turnOfTwoBegin));
	}
	private final List<Operation> IllegalTwoPick=ImmutableList.<Operation>of(
	    		new Set("turn",PlayerOne),
	    		new Set("Pickup",PickrandomFromWall(PlayerOne)),
	            new Set("Pickup",PickrandomFromWall(PlayerOne)));
	private final List<Operation> IllegalTwoThrow=ImmutableList.<Operation>of(
    		new Set("turn",PlayerOne),
    		new Set("Pickup",PickrandomFromWall(PlayerOne)),
    		new Set("Throwout",ImmutableList.of(PlayerOne,5)),
    		new Set("Throwout",ImmutableList.of(PlayerOne,6)));
	private final List<Operation> LegalPick=ImmutableList.<Operation>of(
    		new Set("turn",PlayerOne),
    		new Set("Pickup",PickrandomFromWall(PlayerOne)));
	private final List<Operation> LegalThrow=ImmutableList.<Operation>of(
    		new Set("turn",PlayerOne),
    		new Set("Throwout",ImmutableList.of(PlayerOne,5)));
	@Test 
	testValidThrow()
	{
		assertEquals(true,MoveIsLegal(move(PlayerOne,turnOfOneBegin,LegalThrow)));
	}
	@Test
	InvalidThrow()
	{
		assertEquals(false,MoveIsLegal(move(PlayerOne,turnOfOneBegin,IllegalTwoThrow)));
	}
	@Test
	testValidPick()
	{
		assertEquals(true,MoveIsLegal(move(PlayerOne,turnOfOneBegin,LegalPick)));
	}
	@Test
	testInvalidPick()
	{
		assertEquals(true,MoveIsLegal(move(PlayerOne,turnOfOneBegin,IllegalTwoPick)));
	}
	

}
