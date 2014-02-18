package edu.nyu.mahjong.logic;

import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.*;
public class RefuseGang extends ACommand{
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
	public static boolean lastStateValid(MahJongState lastState)
    {
	   String lastOperation=lastState.getMove().getName();
       if (lastOperation=="WaitForGang") 
         return true;
       if (lastOperation=="RefuseGang")
         {
    	    RefuseGang lastGang=(RefuseGang)(lastState.getMove());
    	    if (lastGang.getSource()==lastState.getTurn())
    	    	return false;
    	    else
    	    	return true;
    	    
         }
       else
    	  return false;
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
