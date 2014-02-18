package edu.nyu.mahjong.logic;

import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import edu.nyu.mahjong.iface.*;
public class RefuseHu extends ACommand{
    private final String name;
    private final int source;
    public static RefuseHu fromRefuseHuEntryInGameState(@Nullable final ImmutableList<String> RefuseHuEntry) {
		if (RefuseHuEntry == null || RefuseHuEntry.isEmpty()) {
			return null;
		}
		// "rankA"
		int Id=Integer.valueOf(RefuseHuEntry.get(1));
		return new RefuseHu(Id);
	}
    public RefuseHu(int sourceId)
    {
    	name="RefuseHu";
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
       if (lastOperation=="WaitForHu") 
         return true;
       if (lastOperation=="RefuseHu")
         {
    	    RefuseHu lastHu=(RefuseHu)(lastState.getMove());
    	    if (lastHu.getSource()==lastState.getTurn())
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
