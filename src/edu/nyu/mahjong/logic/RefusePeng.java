package edu.nyu.mahjong.logic;


import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.*;
public class RefusePeng extends BCommand{
	public static RefusePeng fromRefusePengEntryInGameState(@Nullable final ImmutableList<String> RefusePengEntry) {
		if (RefusePengEntry == null || RefusePengEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id=Integer.valueOf(RefusePengEntry.get(1));
		return new RefusePeng(Id);
	}
    private final String name;
    private final int source;
    public RefusePeng(int sourceId)
    {
    	name="RefusePeng";
    	source=sourceId;
    }
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public int getSource() {
		// TODO Auto-generated method stub
		return source;
	}

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return Arrays.asList(name, source);
	}

}