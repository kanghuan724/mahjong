package org.mahjong.client;

import java.util.Arrays;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;

public class WaitForGang extends ACommand {
	public static WaitForGang fromWaitForGangEntryInGameState(
			@Nullable final ImmutableList<String> WaitForGangEntry) {
		if (WaitForGangEntry == null || WaitForGangEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id = Integer.valueOf(WaitForGangEntry.get(1));
		return new WaitForGang(Id);
	}

	public static boolean lastStateValid(MahJongState lastState) {
		String lastOperation = lastState.getMove().getName();
		if (lastOperation == "RefuseHu") {
			RefuseHu lastRefuse = (RefuseHu) lastState.getMove();
			if (lastRefuse.getSource() != lastState.getTurn()) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private final String name;
	private final int source;

	public WaitForGang(int sourceId) {
		name = "WaitForGang";
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
