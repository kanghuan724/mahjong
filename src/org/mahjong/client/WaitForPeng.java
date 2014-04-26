package org.mahjong.client;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;

public class WaitForPeng extends ACommand {
	public static WaitForPeng fromWaitForPengEntryInGameState(
			@Nullable final List<String> WaitForPengEntry) {
		if (WaitForPengEntry == null || WaitForPengEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id = Integer.valueOf(WaitForPengEntry.get(1));
		return new WaitForPeng(Id);
	}

	public static boolean lastStateValid(MahJongState lastState) {
		String lastOperation = lastState.getMove().getName();
		if (lastOperation == "RefuseGang") {
			RefuseGang lastRefuse = (RefuseGang) lastState.getMove();
			String source = String.valueOf(lastRefuse.getSource());
    	    if (source.equals(lastState.getTurn())==false)
				return false;
			else
				return true;
		} else
			return false;
	}

	private final String name;
	private final int source;

	public WaitForPeng(int sourceId) {
		name = "WaitForPeng";
		source = sourceId;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getSource() {
		return String.valueOf(source);
	}

	@Override
	public Object getId() {
		return Arrays.asList(name, source);
	}

}