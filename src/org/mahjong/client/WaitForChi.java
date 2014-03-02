package org.mahjong.client;

import java.util.Arrays;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;
public class WaitForChi extends ACommand {
	public static WaitForChi fromWaitForChiEntryInGameState(
			@Nullable final ImmutableList<String> WaitForChiEntry) {
		if (WaitForChiEntry == null || WaitForChiEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id = Integer.valueOf(WaitForChiEntry.get(1));
		return new WaitForChi(Id);
	}

	public static boolean lastStateValid(MahJongState lastState) {
		String lastOperation = lastState.getMove().getName();
		if (lastOperation == "RefusePeng") {
			RefusePeng lastRefuse = (RefusePeng) lastState.getMove();
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

	public WaitForChi(int sourceId) {
		name = "WaitForChi";
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
