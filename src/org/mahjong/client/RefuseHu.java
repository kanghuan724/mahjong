package org.mahjong.client;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;

public class RefuseHu extends ACommand {
	private final String name;
	private final int source;

	public static RefuseHu fromRefuseHuEntryInGameState(
			@Nullable final List<String> RefuseHuEntry) {
		if (RefuseHuEntry == null || RefuseHuEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id = Integer.valueOf(RefuseHuEntry.get(1));
		return new RefuseHu(Id);
	}

	public RefuseHu(int sourceId) {
		name = "RefuseHu";
		source = sourceId;
	}

	@Override
	public String getName() {
		return name;
	}

	public static boolean lastStateValid(MahJongState lastState) {
		String lastOperation = lastState.getMove().getName();
		if (lastOperation == "WaitForHu")
			return true;
		if (lastOperation == "RefuseHu") {
			RefuseHu lastHu = (RefuseHu) (lastState.getMove());
			
			String source = String.valueOf(lastHu.getSource());
    	    if (source.equals(lastState.getTurn())) {
				return false;
			} else {
				return true;
			}

		} else
			return false;
	}

	public String getSource() {
		return String.valueOf(source);
	}

	@Override
	public Object getId() {
		return Arrays.asList(name, source);
	}

}
