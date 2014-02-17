package edu.nyu.mahjong.logic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.ACommand;

public class Throw extends ACommand {
	//"Throw", "A7"
	@Nullable
	public static Throw fromThrowEntryInGameState(@Nullable final ImmutableList<String> ThrowEntry) {
		if (ThrowEntry == null || ThrowEntry.isEmpty()) {
			return null;
		}
		Suit suit = Suit.fromFirstLetterLowerCase(ThrowEntry.get(1).substring(0,
				1));
		Rank rank = Rank.fromRankString(ThrowEntry.get(1).substring(1));
		Tile tile = new Tile(suit, rank);
		return new Throw(tile);
	}

	@Nullable 
	public static List<String> toThrowEntryInGameState(@Nullable final Throw t) {
	    return t == null ? null : ImmutableList.of("Throw",t.getTarget().toString());
	  }

	private final String name = "Throw";
	private final Tile target;

	public Throw(Tile target) {

		this.target = target;
	}
   
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Tile getTarget() {
		return target;
	}

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return Arrays.asList(name, target);
	}

}
