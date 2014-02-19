package edu.nyu.mahjong.logic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.ACommand;

public class Hu extends ACommand {
	//"Hu", "A7"
	@Nullable
	public static Hu fromHuEntryInGameState(@Nullable final ImmutableList<String> HuEntry) {
		if (HuEntry == null || HuEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		Suit suit = Suit.fromFirstLetterLowerCase(HuEntry.get(1).substring(0,
				1));
		Rank rank = Rank.fromRankString(HuEntry.get(1).substring(1));
		Tile tile = new Tile(suit, rank);
		return new Hu(tile);
	}

	@Nullable 
	public static List<String> toHuEntryInGameState(@Nullable final Hu hu) {
	    return hu == null ? null : ImmutableList.of("Hu",hu.getTarget().toString());
	  }
	public static boolean lastStateValid(MahJongState lastState)
    {
		String lastOperation=lastState.getMove().getName();
		if (lastOperation=="Pick")
			return true;
	    if (lastOperation=="WaitForHu")
	         return true;	
	    if (lastOperation=="RefuseHu")
	       {
	    	   RefuseHu lastHu=(RefuseHu)(lastState.getMove());
	   	       if (lastHu.getSource()==lastState.getTurn())
	   	    	return false;
	   	       else
	   	    	return true;
	       }
	       else
	    	  return false;
    }
	private final String name = "Hu";
	private final Tile target;

	public Hu(Tile target) {

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
