
import java.util.Arrays;
import java.util.Comparator;

public class Tile extends Equality {
  public enum Suit  {
    WCHARACTERS, BAMBOOS, CIRCLES, EAST, WEST, SOUTH, NORTH, RED, GREEN, WHITE;

    private static final Suit[] VALUES = values();

    public static Suit fromFirstLetterLowerCase(String firstLetterLowerCase) {
      for (Suit suit : VALUES) {
        if (suit.getFirstLetterLowerCase().equals(firstLetterLowerCase)) {
          return suit;
        }
      }
      throw new IllegalArgumentException(
          "Did not find firstLetterLowerCase=" + firstLetterLowerCase);
    }

    public String getFirstLetterLowerCase() {
      return name().substring(0, 1).toLowerCase();
    }

    public Suit getNext() {
      if (this == VALUES[VALUES.length - 1]) {
        return VALUES[0];
      }
      return values()[ordinal() + 1];
    }

    public Suit getPrev() {
      if (this == VALUES[0]) {
        return VALUES[VALUES.length - 1];
      }
      return values()[ordinal() - 1];
    }
  }

  public enum Rank {
    ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;

    private static final Rank[] VALUES = values();

    public static final Comparator<Rank> COMPARATOR = new Comparator<Rank>() {
      @Override
      public int compare(Rank o1, Rank o2) {
    	  
        int ord1 =  o1.ordinal();
        int ord2 =  o2.ordinal();
        if ((o1 == VALUES[0] || o2 == VALUES[0]) && (o1 != o2)) {
        	return 10;
        } else {
        	return ord1 - ord2;
        }
      }
    };

    public static Rank fromRankString(String rankString) {
      return VALUES[Integer.valueOf(rankString)];
    }

    public String getRankString() {
      return String.valueOf(this.ordinal());
    }

    public Rank getNext() {
      if (this == VALUES[VALUES.length - 1]) {
        return VALUES[0];
      }
      return values()[ordinal() + 1];
    }

    public Rank getPrev() {
      if (this == VALUES[0]) {
        return VALUES[VALUES.length - 1];
      }
      return values()[ordinal() - 1];
    }
  }

  private Suit suitValue;
  private Rank rankValue;


  /**
   * Creates a new playing tile.
   *
   * @param suit the suit value of this card.
   * @param rank the rank value of this card.
   */
  public Tile(Suit suit, Rank rank) {
    suitValue = suit;
    rankValue = rank;
  }

  /**
   * Returns the suit of the tile.
   *
   * @return a Suit constant representing the suit value of the tile.
   */
  public Suit getSuit() {
    return suitValue;
  }


  /**
   * Returns the rank of the tile.
   *
   * @return a Rank constant representing the rank value of the tile.
   */
  public Rank getRank() {
    return rankValue;
  }

  /**
   * Returns a description of this tile.
   *
   * @return the name of the tile.
   */
  @Override
  public String toString() {
    return rankValue.toString() + " of " + suitValue.toString();
  }

  @Override
  public Object getId() {
    return Arrays.asList(getSuit(), getRank());
  }
}