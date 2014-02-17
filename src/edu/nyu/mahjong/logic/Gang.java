package edu.nyu.mahjong.logic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.ACommand;

public class Gang extends ACommand {
	//"Gang", "A7"
	@Nullable
	public static Gang fromGangEntryInGameState(@Nullable final ImmutableList<String> GangEntry) {
		if (GangEntry == null || GangEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		Suit suit = Suit.fromFirstLetterLowerCase(GangEntry.get(1).substring(0,
				1));
		Rank rank = Rank.fromRankString(GangEntry.get(1).substring(1));
		Tile tile = new Tile(suit, rank);
		return new Gang(tile);
	}

	@Nullable 
	public static List<String> toGangEntryInGameState(@Nullable final Gang gang) {
	    return gang == null ? null : ImmutableList.of("Gang",gang.getTarget().toString());
	  }

	private final String name = "Pick";
	private final Tile target;

	public Gang(Tile target) {

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