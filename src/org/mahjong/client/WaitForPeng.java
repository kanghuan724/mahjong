package org.mahjong.client;

import java.util.Arrays;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;

public class WaitForPeng extends ACommand {
	public static WaitForPeng fromWaitForPengEntryInGameState(
			@Nullable final ImmutableList<String> WaitForPengEntry) {
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
			if (lastRefuse.getSource() != lastState.getTurn())
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

	public int getSource() {
		return source;
	}

	@Override
	public Object getId() {
		return Arrays.asList(name, source);
	}

}