package edu.nyu.mahjong.logic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
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
	public static boolean lastStateValid(MahJongState lastState)
    {
	   String lastOperation=lastState.getMove().getName();
       if (lastOperation=="WaitForChi") 
         return true;	
       else
    	  return false;
    }
	
	@SuppressWarnings("null")
	public static boolean chiCorrect(MahJongState state, List<Integer> chiCombo){
        Tile[] combo = null;
		for (int i = 0; i<chiCombo.size(); i++) {
			combo[i] = state.getTile(chiCombo.get(i)).get();
		}
		if ((!combo[0].getSuit().equals(combo[1].getSuit())) || (!combo[1].getSuit().equals(combo[2].getSuit()))) {
			return false;
		} else {	
     		if ((Rank.COMPARATOR.compare(combo[0].getRank(), combo[1].getRank()) == 1 
     				&& Rank.COMPARATOR.compare(combo[1].getRank(), combo[2].getRank()) == 1) ||
     			(Rank.COMPARATOR.compare(combo[0].getRank(), combo[2].getRank()) == 1 
     				&& Rank.COMPARATOR.compare(combo[2].getRank(), combo[1].getRank()) == 1) ||
         		(Rank.COMPARATOR.compare(combo[1].getRank(), combo[0].getRank()) == 1 
     				&& Rank.COMPARATOR.compare(combo[0].getRank(), combo[2].getRank()) == 1) ||
         		(Rank.COMPARATOR.compare(combo[1].getRank(), combo[2].getRank()) == 1 
     				&& Rank.COMPARATOR.compare(combo[2].getRank(), combo[0].getRank()) == 1) ||
         		(Rank.COMPARATOR.compare(combo[2].getRank(), combo[0].getRank()) == 1 
     				&& Rank.COMPARATOR.compare(combo[0].getRank(), combo[1].getRank()) == 1) ||
         		(Rank.COMPARATOR.compare(combo[2].getRank(), combo[1].getRank()) == 1 
     				&& Rank.COMPARATOR.compare(combo[1].getRank(), combo[0].getRank()) == 1)) {
     			return true;
     		} else {
     			return false;
     		}
		}	
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

	public Tile getTarget() {
		return target;
	}

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return Arrays.asList(name, target);
	}

}
