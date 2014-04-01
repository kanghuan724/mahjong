package org.mahjong.client;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;

public class Gang extends ACommand {
	// "Gang", "A7"
	@Nullable
	public static Gang fromGangEntryInGameState(
			@Nullable final ImmutableList<String> GangEntry) {
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
		return gang == null ? null : ImmutableList.of("Gang", gang.getTarget()
				.toString());
	}

	public static boolean lastStateValid(MahJongState lastState) {
		String lastOperation = lastState.getMove().getName();
		if (lastOperation == "WaitForGang")
			return true;
		if (lastOperation == "RefuseGang") {
			RefuseGang lastGang = (RefuseGang) (lastState.getMove());
			String source = String.valueOf(lastGang.getSource());
			if (source.equals (lastState.getTurn()))
				return false;
			else
				return true;
		} 
		else
		  return false;
	}

	@SuppressWarnings("null")
	public static boolean gangCorrect(MahJongState state,
			List<Integer> gangCombo) {
		Tile[] combo = new Tile[4];
		for (int i = 0; i<gangCombo.size(); i++) {
			//getTile returns the certain tile with index of (pengCombo.get(i))
			combo[i] = state.getTile(gangCombo.get(i)).get();
			//System.out.println(combo[i].toString());
		}
		if (combo[0].equals(combo[1]) && combo[1].equals(combo[2])&&combo[2].equals(combo[3])) {
			return true;
		} else {	
     		return false;
		}	
	}

	private final String name = "Gang";
	private final Tile target;

	public Gang(Tile target) {

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