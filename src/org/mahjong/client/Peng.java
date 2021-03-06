package org.mahjong.client;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;

public class Peng extends ACommand {
	//"Peng", "A7"
	@Nullable
	public static Peng fromPengEntryInGameState(@Nullable final List<String> PengEntry) {
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
	    if (lastOperation=="WaitForPeng")
	         return true;	
	    if (lastOperation=="RefusePeng")
	       {
	    	   RefusePeng lastPeng=(RefusePeng)(lastState.getMove());
	    	   String source = String.valueOf(lastPeng.getSource());
	   	       if (source.equals(lastState.getTurn()))
	   	    	return false;
	   	       else
	   	    	return true;
	       }
	       else
	    	  return false;
    }
	
	public static boolean pengCorrect(MahJongState state, List<Integer> pengCombo){
        Tile[] combo = new Tile[3];
		for (int i = 0; i<pengCombo.size(); i++) {
			//getTile returns the certain tile with index of (pengCombo.get(i))
			combo[i] = state.getTile(pengCombo.get(i)).get();
		}
		if (combo[0].equals(combo[1]) && combo[1].equals(combo[2])) {
			return true;
		} else {	
     		return false;
		}	
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
		return Arrays.asList(name, target);
	}

}
