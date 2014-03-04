package org.mahjong.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;

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
	    return chi == null ? null : ImmutableList.of("Chi", chi.getTarget().toString());
	  }
	public static boolean lastStateValid(MahJongState lastState)
    {
	   String lastOperation=lastState.getMove().getName();
	   String lastPlayerId = String.valueOf(lastState.getTurn());
       if (lastOperation=="WaitForChi") 
         return true;	
       else
    	  return false;
    }
	
	public static boolean chiCorrect(MahJongState state, List<Integer> chiCombo){
        Tile[] combo = new Tile[3];
        System.out.println("checkChi");
		for (int i = 0; i<chiCombo.size(); i++) {
			combo[i] = state.getTile(chiCombo.get(i)).get();
			System.out.println("combo"+i);
			System.out.println(combo[i].toString());
			System.out.println("finished");
		}
		/*if ((!combo[0].getSuit().equals(combo[1].getSuit())) || (!combo[1].getSuit().equals(combo[2].getSuit()))) {
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
		}*/
		if ((combo[0].getSuit().equals(combo[1].getSuit()))&&(combo[1].getSuit().equals(combo[2].getSuit())))
				{
			         System.out.println("I am here true");
			         List<Integer> rankBuffer=new ArrayList<Integer> ();
			         for (int i=0;i<3;i++)
			        	 rankBuffer.add(combo[i].getRank().ordinal());
			         Collections.sort(rankBuffer);
			         for (int i=1;i<3;i++)
			         {
			        	 if (rankBuffer.get(i)!=rankBuffer.get(i-1)+1)
			        	 {
			        		 System.out.println("THey dont match");
			        		 return false;
			        	 }
			         }
			        	
			         
			
				}
		else
		{
			System.out.println("I am here false");
			return false;
		}
		return true;
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
