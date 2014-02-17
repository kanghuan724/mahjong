package edu.nyu.mahjong.logic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.ACommand;

public class Pick extends ACommand {
	//"Pick", "A7"
	@Nullable
	public static Pick fromPickEntryInGameState(@Nullable final List<String> PickEntry) {
		if (PickEntry == null || PickEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		
		return new Pick();
	}

	@Nullable 
	public static List<String> toPickEntryInGameState(@Nullable final Pick pick) {
	    return pick == null ? null : ImmutableList.of("Pickup");
	  }

	private final String name;
    private final Tile target;
	public Pick() {

		this.name = "PickUp";
		target=null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Tile getTarget() {
		return null;
	}

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return Arrays.asList(name, target);
	}

}
