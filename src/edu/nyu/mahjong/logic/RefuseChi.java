package edu.nyu.mahjong.logic;

import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.*;
public class RefuseChi extends BCommand{
	public static RefuseChi fromRefuseChiEntryInGameState(@Nullable final ImmutableList<String> RefuseChiEntry) {
		if (RefuseChiEntry == null || RefuseChiEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id=Integer.valueOf(RefuseChiEntry.get(1));
		return new RefuseChi(Id);
	}
    private final String name;
    private final int source;
    public RefuseChi(int sourceId)
    {
    	name="RefuseChi";
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
