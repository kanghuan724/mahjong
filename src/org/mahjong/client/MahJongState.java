package org.mahjong.client;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.mahjong.client.ACommand;
/**
 * Representation of the MahJong game state.
 * The game state uses these keys: 
 *** turn, move, playerIds, tiles, tilesAtWall, tilesUsed
 *** tilesAtHandOfOne, tilesAtHandOfTwo, tilesAtHandOfThree, tilesAtHandOfFour,
 *** tilesAtDeclaredOfOne, tilesAtDeclaredOfTwo, tilesAtDeclaredOfThree, tilesAtDeclaredOfFour,
 *** T1...T136, which are mapped to the tiles
 */
public class MahJongState {
	  //the playerId
	  private final String turn;
	  private final ACommand move;
	  private final ImmutableList<String> playerIds;

	  /**
	   * Note that some of the entries will have null, meaning the card is not visible to us.
	   */
	  private final ImmutableList<Optional<Tile>> tiles;

	  /**
	   * Index of the tiles, each integer is in the range [0-135).
	   */
	  private final ImmutableList<Integer> tilesAtWall;
	  private final ImmutableList<Integer> tilesUsed;
	  private  ImmutableList<Integer> tilesAtHandOfOne;
	  private final ImmutableList<Integer> tilesAtDeclaredOfOne;
	  private  ImmutableList<Integer> tilesAtHandOfTwo;
	  private final ImmutableList<Integer> tilesAtDeclaredOfTwo;
	  private  ImmutableList<Integer> tilesAtHandOfThree;
	  private final ImmutableList<Integer> tilesAtDeclaredOfThree;
	  private  ImmutableList<Integer> tilesAtHandOfFour;
	  private final ImmutableList<Integer> tilesAtDeclaredOfFour;

	  public MahJongState(String turn, ACommand move, ImmutableList<String> playerIds,
	      ImmutableList<Optional<Tile>> tiles, ImmutableList<Integer> tilesAtWall,
	      ImmutableList<Integer> tilesUsed, 
	      ImmutableList<Integer> tilesAtHandOfOne, ImmutableList<Integer> tilesAtDeclaredOfOne,
	      ImmutableList<Integer> tilesAtHandOfTwo, ImmutableList<Integer> tilesAtDeclaredOfTwo,
	      ImmutableList<Integer> tilesAtHandOfThree, ImmutableList<Integer> tilesAtDeclaredOfThree,
	      ImmutableList<Integer> tilesAtHandOfFour, ImmutableList<Integer> tilesAtDeclaredOfFour) {
	    super();
	    this.turn = turn;
	    this.move = (move);
	    this.playerIds = checkNotNull(playerIds);
	    this.tiles = checkNotNull(tiles);
	    this.tilesAtWall = tilesAtWall;
	    this.tilesUsed = tilesUsed;
	    this.tilesAtHandOfOne = checkNotNull(tilesAtHandOfOne);
	    this.tilesAtDeclaredOfOne = tilesAtDeclaredOfOne;
	    this.tilesAtHandOfTwo = checkNotNull(tilesAtHandOfTwo);
	    this.tilesAtDeclaredOfTwo = tilesAtDeclaredOfTwo;
	    this.tilesAtHandOfThree = checkNotNull(tilesAtHandOfThree);
	    this.tilesAtDeclaredOfThree = tilesAtDeclaredOfThree;
	    this.tilesAtHandOfFour = checkNotNull(tilesAtHandOfFour);
	    this.tilesAtDeclaredOfFour = tilesAtDeclaredOfFour;
	  
	  }

	  public String getTurn() {
	    return turn;
	  }
	  
	  public ACommand getMove() {
	    return move;
	  }

	  public ImmutableList<String> getPlayerIds() {
	    return playerIds;
	  }

	  public ImmutableList<Optional<Tile>> getTiles() {
	    return tiles;
	  }
	  
	  public Optional<Tile> getTile(int index) {
		  return tiles.get(index);
	  }

	  public ImmutableList<Integer> getTilesAtWall() {
	    return tilesAtWall;
	  }

	  public ImmutableList<Integer> getTilesUsed() {
	    return tilesUsed;
	  }
      public void changeTileSequence(int playerId, ImmutableList<Integer> target)
      {
    	  switch(playerId) {
    	  case 0:
    	  {
    		  tilesAtHandOfOne = target;
    		  break;
    	  }
    	  case 1:
    	  {
    		  tilesAtHandOfTwo = target;
    		  break;
    	  }
    	  case 2:
    	  {
    		  tilesAtHandOfThree = target;
    		  break;
    	  }
    	  case 3:
    	  {
    		  tilesAtHandOfFour = target;
    		  break;
    	  }
    		
    	  default:
    		  break;
    	  }
      }

	  public ImmutableList<Integer> getTilesAtHand(String playerId) {

	    switch (playerId) {
	    case "0": 
	    	return tilesAtHandOfOne;	
	    case "1": 
	    	return tilesAtHandOfTwo;	
	    case "2": 
	    	return tilesAtHandOfThree;
	    case "3": 
	    	return tilesAtHandOfFour;
	    default:
	    	return null;
	    }
	  }

	  public ImmutableList<Integer> getTilesAtDeclared(String playerId) {
		    switch (playerId) {
		    case "0": 
		    	return tilesAtDeclaredOfOne;    	
		    case "1": 
		    	return tilesAtDeclaredOfTwo;
		    case "2": 
		    	return tilesAtDeclaredOfThree;
		    case "3": 
		    	return tilesAtDeclaredOfFour;
		    default: 
		    	return null;
		    }
		  }
}
