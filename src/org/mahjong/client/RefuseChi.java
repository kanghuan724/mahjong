package org.mahjong.client;

import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;
public class RefuseChi extends ACommand{
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
    public static boolean lastStateValid(MahJongState lastState)
    {
	   String lastOperation=lastState.getMove().getName();
       if (lastOperation=="WaitForChi")
    	   return true;
       else
    	  return false;
    }
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}


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
