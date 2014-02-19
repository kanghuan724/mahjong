package edu.nyu.mahjong.logic;

import java.util.Arrays;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.*;

public class WaitForHu extends ACommand {
	public static WaitForHu fromWaitForHuEntryInGameState(
			@Nullable final ImmutableList<String> WaitForHuEntry) {
		if (WaitForHuEntry == null || WaitForHuEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id = Integer.valueOf(WaitForHuEntry.get(1));
		return new WaitForHu(Id);
	}

	public static boolean lastStateValid(MahJongState lastState) {
		String lastOperation = lastState.getMove().getName();
		if (lastOperation == "Discard") {
			return true;
		} else {
			return false;
		}
	}

	private final String name;
	private final int source;

	public WaitForHu(int sourceId) {
		name = "WaitForHu";
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