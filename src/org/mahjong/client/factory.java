package org.mahjong.client;

import java.util.List;

import org.mahjong.client.Pick;

import com.google.common.collect.ImmutableList;

public class factory {
	public static ACommand makeCommand(List<String> tokens) {
		if (tokens.size()==0)
			return Empty.fromEmptyEntryInGameState(tokens);
		switch (tokens.get(0)) {
		case ("GameEnd"):
			return GameEnd.fromEmptyEntryInGameState();
		case ("PickUp"):
			return Pick.fromPickEntryInGameState(tokens);
		case ("Discard"):
			return Discard.fromThrowEntryInGameState(tokens);
		case ("Peng"):
			return Peng.fromPengEntryInGameState(tokens);
		case ("Chi"):
			return Chi.fromChiEntryInGameState(tokens);
		case ("Gang"):
			return Gang.fromGangEntryInGameState(tokens);
		case ("RefusePeng"):
			return RefusePeng.fromRefusePengEntryInGameState(tokens);
		case ("RefuseGang"):
			return RefuseGang.fromRefuseGangEntryInGameState(tokens);
		case ("RefuseChi"):
			return RefuseChi.fromRefuseChiEntryInGameState(tokens);
		case ("WaitForChi"):
			return WaitForChi.fromWaitForChiEntryInGameState(tokens);
		case ("WaitForPeng"):
			return WaitForPeng.fromWaitForPengEntryInGameState(tokens);
		case ("WaitForGang"):
			return WaitForGang.fromWaitForGangEntryInGameState(tokens);
		case ("WaitForHu"):
			return WaitForHu.fromWaitForHuEntryInGameState(tokens);
		case ("Hu"):
			return Hu.fromHuEntryInGameState(tokens);
		case ("RefuseHu"):
			return RefuseHu.fromRefuseHuEntryInGameState(tokens);

		}
		return null;
	}
}
