package edu.nyu.mahjong.logic;

import java.util.Arrays;
import java.util.List;

import edu.nyu.mahjong.logic.MahJongLogic.*;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.*;
public class WaitForPeng extends ACommand{
	public static WaitForPeng fromWaitForPengEntryInGameState(@Nullable final ImmutableList<String> WaitForPengEntry) {
		if (WaitForPengEntry == null || WaitForPengEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id=Integer.valueOf(WaitForPengEntry.get(1));
		return new WaitForPeng(Id);
	}
	public static boolean lastStateValid(MahJongState lastState)
    {
		String lastOperation=lastState.getMove().getName();
	    if (lastOperation=="RefuseGang")
	    {
	    	 RefuseGang lastRefuse=(RefuseGang) lastState.getMove();
	    	 if (lastRefuse.getSource()!=lastState.getTurn())
	           return false;
	    	 else
	    	   return true;
	     }
	       else
	    	  return false;
    }
    private final String name;
    private final int source;
    public WaitForPeng(int sourceId)
    {
    	name="WaitForPeng";
    	source=sourceId;
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