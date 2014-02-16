package edu.nyu.mahjong.logic;

import java.util.Arrays;

public class Tile extends Equality {

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