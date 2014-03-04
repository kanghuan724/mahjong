package org.mahjong.client;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;

public class GameEnd extends ACommand {
	//"Pick", "A7"
	@Nullable
	public static GameEnd fromEmptyEntryInGameState() {
		
		// "rankA"
		
		return new GameEnd();
	}

	@Nullable 
	public static List<String> toEmptyEntryInGameState(@Nullable final GameEnd gameEnd) {
	    return gameEnd == null ? null : ImmutableList.of("GameEnd");
	  }

	private final String name;
	public GameEnd() {

		this.name = "GameEnd";
	}
	
	@Override
	public String getName() {
		return name;
	}

	public Tile getTarget() {
		return null;
	}

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return Arrays.asList(name);
	}

}