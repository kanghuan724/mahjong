import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
/**
 * Representation of the MahJong game state.
 * The game state uses these keys: 
 *** tilesAtWall, tilesUsed, move
 *** tilesInHandOf1, tilesInHandOf2, tilesInHandOf3, tilesInHandOf4,
 *** tilesDeclaredOf1, tilesDeclaredof2, tilesDeclaredOf3, tilesDeclaredOf4,
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
	  private final ImmutableList<Integer> tilesInHandOf1;
	  private final ImmutableList<Integer> tilesDeclaredOf1;
	  private final ImmutableList<Integer> tilesInHandOf2;
	  private final ImmutableList<Integer> tilesDeclaredOf2;
	  private final ImmutableList<Integer> tilesInHandOf3;
	  private final ImmutableList<Integer> tilesDeclaredOf3;
	  private final ImmutableList<Integer> tilesInHandOf4;
	  private final ImmutableList<Integer> tilesDeclaredOf4;

	  public MahJongState(String move, ImmutableList<Integer> playerIds,
	      ImmutableList<Optional<Tiles>> tiles, ImmutableList<Integer> tilesAtWall,
	      ImmutableList<Integer> tilesUsed, 
	      ImmutableList<Integer> tilesInHandOf1, ImmutableList<Integer> tilesDeclaredOf1,
	      ImmutableList<Integer> tilesInHandOf2, ImmutableList<Integer> tilesDeclaredOf2,
	      ImmutableList<Integer> tilesInHandOf3, ImmutableList<Integer> tilesDeclaredOf3,
	      ImmutableList<Integer> tilesInHandOf4, ImmutableList<Integer> tilesDeclaredOf4) {
	    super();
	    this.move = (move);
	    this.playerIds = checkNotNull(playerIds);
	    this.tiles = checkNotNull(tiles);
	    this.tilesAtWall = tilesAtWall;
	    this.tilesUsed = tilesUsed;
	    this.tilesInHandOf1 = checkNotNull(tilesInHandOf1);
	    this.tilesDeclaredOf1 = tilesDeclaredOf1;
	    this.tilesInHandOf2 = checkNotNull(tilesInHandOf2);
	    this.tilesDeclaredOf2 = tilesDeclaredOf2;
	    this.tilesInHandOf3 = checkNotNull(tilesInHandOf3);
	    this.tilesDeclaredOf3 = tilesDeclaredOf3;
	    this.tilesInHandOf4 = checkNotNull(tilesInHandOf4);
	    this.tilesDeclaredOf4 = tilesDeclaredOf4;

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

	  public ImmutableList<Integer> getTilesInHad(int playerId) {
	    switch (playerId) {
	    case 1: 
	    	return tilesInHandOf1;
	    	break;	    	
	    case 2: 
	    	return tilesInHandOf2;
	    	break;	    		    	
	    case 3: 
	    	return tilesInHandOf3;
	    	break;	    	
	    case 4: 
	    	return tilesInHandOf4;
	    	break;	    		
	    }
	  }

	  public ImmutableList<Integer> getTilesDeclared(int playerId) {
		    switch (playerId) {
		    case 1: 
		    	return tilesDeclaredOf1;
		    	break;	    	
		    case 2: 
		    	return tilesDeclaredOf2;
		    	break;	    		    	
		    case 3: 
		    	return tilesDeclaredOf3;
		    	break;	    	
		    case 4: 
		    	return tilesDeclaredOf4;
		    	break;	    		
		    }
		  }
}
