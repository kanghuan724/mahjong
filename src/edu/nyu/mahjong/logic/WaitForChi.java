package edu.nyu.mahjong.logic;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.*;
public class WaitForChi extends ACommand{
	public static WaitForChi fromWaitForChiEntryInGameState(@Nullable final ImmutableList<String> WaitForChiEntry) {
		if (WaitForChiEntry == null || WaitForChiEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id=Integer.valueOf(WaitForChiEntry.get(1));
		return new WaitForChi(Id);
	}
	public static boolean lastStateValid(MahJongState lastState)
    {
	   String lastOperation=lastState.getMove().getName();
       if (lastOperation=="RefusePeng")
       {
    	 RefusePeng lastRefuse=(RefusePeng) lastState.getMove();
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
    public WaitForChi(int sourceId)
    {
    	name="WaitForChi";
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

