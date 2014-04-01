package org.mahjong.client;


import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.mahjong.client.ACommand;

public class RefusePeng extends ACommand{
	public static RefusePeng fromRefusePengEntryInGameState(@Nullable final ImmutableList<String> RefusePengEntry) {
		if (RefusePengEntry == null || RefusePengEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id=Integer.valueOf(RefusePengEntry.get(1));
		return new RefusePeng(Id);
	}
	public static boolean lastStateValid(MahJongState lastState)
    {
	   String lastOperation=lastState.getMove().getName();
       if (lastOperation=="WaitForPeng")
    	   return true;
       if (lastOperation=="RefusePeng")
         {
    	    RefusePeng lastPeng=(RefusePeng)(lastState.getMove());
    	    String source = String.valueOf(lastPeng.getSource());
    	    if (source.equals(lastState.getTurn()))
    	    	return false;
    	    else
    	    	return true;
    	    
         }
       else
    	  return false;
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

	public String getSource() {
		// TODO Auto-generated method stub
		return String.valueOf(source);
	}

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return Arrays.asList(name, source);
	}

}