package org.mahjong.client;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;

public class Discard extends ACommand {
	//"Throw", "A7"
	@Nullable
	public static Discard fromThrowEntryInGameState(@Nullable final ImmutableList<String> ThrowEntry) {
		if (ThrowEntry == null || ThrowEntry.isEmpty()) {
			return null;
		}
		Suit suit = Suit.fromFirstLetterLowerCase(ThrowEntry.get(1).substring(0,
				1));
		Rank rank = Rank.fromRankString(ThrowEntry.get(1).substring(1));
		Tile tile = new Tile(suit, rank);
		return new Discard(tile);
	}

	@Nullable 
	public static List<String> toThrowEntryInGameState(@Nullable final Discard t) {
	    return t == null ? null : ImmutableList.of("Discard", t.getTarget().toString());
	  }
	public static boolean lastStateValid(MahJongState lastState)
    {
	   String lastOperation=lastState.getMove().getName();
       if (lastOperation=="PickUp"||lastOperation=="Peng"||lastOperation=="Chi") 
         return true;	
       else
    	  return false;
    }
	private final String name = "Discard";
	private final Tile target;

	public Discard(Tile target) {

		this.target = target;
	}
   
	@Override
	public String getName() {
		return name;
	}

	public Tile getTarget() {
		return target;
	}

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return Arrays.asList(name, target);
	}

}
