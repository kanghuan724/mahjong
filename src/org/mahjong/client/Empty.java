package org.mahjong.client;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;

public class Empty extends ACommand {
	//"Pick", "A7"
	@Nullable
	public static Empty fromEmptyEntryInGameState(@Nullable final List<String> PickEntry) {
		
		// "rankA"
		
		return new Empty();
	}

	@Nullable 
	public static List<String> toEmptyEntryInGameState(@Nullable final Empty empty) {
	    return empty == null ? null : ImmutableList.of("Empty");
	  }

	private final String name;
	public Empty() {

		this.name = "Empty";
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
