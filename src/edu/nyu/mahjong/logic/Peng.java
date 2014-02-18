package edu.nyu.mahjong.logic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.ACommand;

public class Peng extends ACommand {
	//"Peng", "A7"
	@Nullable
	public static Peng fromPengEntryInGameState(@Nullable final ImmutableList<String> PengEntry) {
		if (PengEntry == null || PengEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		Suit suit = Suit.fromFirstLetterLowerCase(PengEntry.get(1).substring(0,
				1));
		Rank rank = Rank.fromRankString(PengEntry.get(1).substring(1));
		Tile tile = new Tile(suit, rank);
		return new Peng(tile);
	}

	@Nullable 
	public static List<String> toPengEntryInGameState(@Nullable final Peng peng) {
	    return peng == null ? null : ImmutableList.of("Pick",peng.getTarget().toString());
	  }
	public static boolean lastStateValid(MahJongState lastState)
    {
		String lastOperation=lastState.getMove().getName();
	    if (lastOperation=="waitForPeng")
	         return true;	
	    if (lastOperation=="RefusePeng")
	       {
	    	   RefusePeng lastPeng=(RefusePeng)(lastState.getMove());
	   	       if (lastPeng.getSource()==lastState.getTurn())
	   	    	return false;
	   	       else
	   	    	return true;
	       }
	       else
	    	  return false;
    }
	private final String name = "Peng";
	private final Tile target;

	public Peng(Tile target) {

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
