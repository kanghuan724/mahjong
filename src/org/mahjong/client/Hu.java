package org.mahjong.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.mahjong.client.ACommand;
import org.mahjong.client.MahJongLogic;

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
	@SuppressWarnings("null")
	public static boolean huCorrect(MahJongState state, List<Integer> huCombo, List<Integer> atHand){
        Tile[] combo = new Tile[3];
		for (int i = 0; i<huCombo.size(); i++) {
			combo[i] = state.getTile(huCombo.get(i)).get();
		}
		if (huCombo.size() == 2 && combo[0].equals(combo[1]) && Hu.allSet(state, atHand)) {
			return true;
		}
		if (Peng.pengCorrect(state, huCombo) || Chi.chiCorrect(state, huCombo)) {
			for (int i = 0; i<atHand.size(); i++) {
				Tile left = state.getTile(atHand.get(i)).get();
				for (int j = i+1; j<atHand.size(); j++) {
					Tile right = state.getTile(atHand.get(j)).get();
					if (left.equals(right)) {
						List<Integer> pair = null;
						pair.add(i);
						pair.add(j);
						List<Integer> atHandNoPair = atHand;
						atHandNoPair.removeAll(pair);
						if (atHandNoPair.size() == 0 || allSet(state, atHandNoPair)) {
							return true;
						}
					}
				}
			}

		}
		return false;
	}
	/*public static boolean huCorrect(List<Integer> atHand)
	{
		return true;
	}*/
	@SuppressWarnings("null")
	public static boolean allSet(MahJongState state, List<Integer> atHand) {
		List<Integer> AtHand = atHand;
		Collections.sort(AtHand);
		int comboNum = AtHand.size() / 3;
		for (int i = 0; i < comboNum; i++) {
			List<Integer> combo = null;
			combo.add(i*3);
			combo.add(i*3+1);
			combo.add(i*3+2);
			if ((!Peng.pengCorrect(state, combo)) && (!Chi.chiCorrect(state, combo))) {
				return false;
			}
		}
	    return true;
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
