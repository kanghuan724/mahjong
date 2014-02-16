import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
/**
 * Representation of the MahJong game state.
 * The game state uses these keys: 
 *** tilesAtWall, tilesUsed, move
 *** tilesAtHandOfOne, tilesAtHandOfTwo, tilesAtHandOfThree, tilesAtHandOfFour,
 *** tilesAtDeclaredOfOne, tilesAtDeclaredOfTwo, tilesAtDeclaredOfThree, tilesAtDeclaredOfFour,
 *** T1...T136, which are mapped to the tiles
 */
public class MahJongState {
	  private final String move;
	  private final ImmutableList<Integer> playerIds;

	  /**
	   * Note that some of the entries will have null, meaning the card is not visible to us.
	   */
	  private final ImmutableList<Optional<Tile>> tiles;

	  /**
	   * Index of the tiles, each integer is in the range [0-135).
	   */
	  private final ImmutableList<Integer> tilesAtWall;
	  private final ImmutableList<Integer> tilesUsed;
	  private final ImmutableList<Integer> tilesAtHandOfOne;
	  private final ImmutableList<Integer> tilesAtDeclaredOfOne;
	  private final ImmutableList<Integer> tilesAtHandOfTwo;
	  private final ImmutableList<Integer> tilesAtDeclaredOfTwo;
	  private final ImmutableList<Integer> tilesAtHandOfThree;
	  private final ImmutableList<Integer> tilesAtDeclaredOfThree;
	  private final ImmutableList<Integer> tilesAtHandOfFour;
	  private final ImmutableList<Integer> tilesAtDeclaredOfFour;

	  public MahJongState(String move, ImmutableList<Integer> playerIds,
	      ImmutableList<Optional<Tiles>> tiles, ImmutableList<Integer> tilesAtWall,
	      ImmutableList<Integer> tilesUsed, 
	      ImmutableList<Integer> tilesAtHandOfOne, ImmutableList<Integer> tilesAtDeclaredOfOne,
	      ImmutableList<Integer> tilesAtHandOfTwo, ImmutableList<Integer> tilesAtDeclaredOfTwo,
	      ImmutableList<Integer> tilesAtHandOfThree, ImmutableList<Integer> tilesAtDeclaredOfThree,
	      ImmutableList<Integer> tilesAtHandOfFour, ImmutableList<Integer> tilesAtDeclaredOfFour) {
	    super();
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

	  public String getMove() {
	    return move;
	  }

	  public ImmutableList<Integer> getPlayerIds() {
	    return playerIds;
	  }

	  public ImmutableList<Optional<Tile>> getTiles() {
	    return tiles;
	  }

	  public ImmutableList<Integer> getTilesAtWall() {
	    return tilesAtWall;
	  }

	  public ImmutableList<Integer> getTilesUsed() {
	    return tilesUsed;
	  }

	  public ImmutableList<Integer> getTilesAtHand(int playerId) {
	    switch (playerId) {
	    case 1: 
	    	return tilesAtHandOfOne;	
	    case 2: 
	    	return tilesAtHandOfTwo;	
	    case 3: 
	    	return tilesAtHandOfThree;
	    case 4: 
	    	return tilesAtHandOfFour;
	    default:
	    	return null;
	    }
	  }

	  public ImmutableList<Integer> getTilesAtDeclared(int playerId) {
		    switch (playerId) {
		    case 1: 
		    	return tilesAtDeclaredOfOne;    	
		    case 2: 
		    	return tilesAtDeclaredOfTwo;
		    case 3: 
		    	return tilesAtDeclaredOfThree;
		    case 4: 
		    	return tilesAtDeclaredOfFour;
		    default: 
		    	return null;
		    }
		  }
}
