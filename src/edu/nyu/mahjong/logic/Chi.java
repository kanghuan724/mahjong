package edu.nyu.mahjong.logic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.ACommand;

public class Chi extends ACommand {
	//"Chi", "A7"
	@Nullable
	public static Chi fromChiEntryInGameState(@Nullable final ImmutableList<String> ChiEntry) {
		if (ChiEntry == null || ChiEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		Suit suit = Suit.fromFirstLetterLowerCase(ChiEntry.get(1).substring(0,
				1));
		Rank rank = Rank.fromRankString(ChiEntry.get(1).substring(1));
		Tile tile = new Tile(suit, rank);
		return new Chi(tile);
	}

	@Nullable 
	public static List<String> toChiEntryInGameState(@Nullable final Chi chi) {
	    return chi == null ? null : ImmutableList.of("Chi",chi.getTarget().toString());
	  }

	private final String name = "Chi";
	private final Tile target;

	public Chi(Tile target) {

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
