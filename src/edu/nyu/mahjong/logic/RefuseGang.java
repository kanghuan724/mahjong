package edu.nyu.mahjong.logic;

import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.*;
public class RefuseGang extends BCommand{
    private final String name;
    private final int source;
    public static RefuseGang fromRefuseGangEntryInGameState(@Nullable final ImmutableList<String> RefuseGangEntry) {
		if (RefuseGangEntry == null || RefuseGangEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id=Integer.valueOf(RefuseGangEntry.get(1));
		return new RefuseGang(Id);
	}
    public RefuseGang(int sourceId)
    {
    	name="RefuseGang";
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
