package edu.nyu.mahjong.logic;

import edu.nyu.mahjong.iface.*;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public class factory {
   public static ACommand makeCommand(ImmutableList<String> tokens)
   {
	   if (tokens==null)
		   return Pick.fromPickEntryInGameState(tokens);
	   switch (tokens.get(0))
	   {
	   case ("PickUp"):
	     return Pick.fromPickEntryInGameState(tokens);
	   case("Discard"):
		   return Discard.fromThrowEntryInGameState(tokens);
	   case("Peng"):
		   return Peng.fromPengEntryInGameState(tokens);
	   case("Chi"):
		   return Chi.fromChiEntryInGameState(tokens);
	   case ("Gang"):
		   return Gang.fromGangEntryInGameState(tokens);
	   case ("RefusePeng"):
		   return RefusePeng.fromRefusePengEntryInGameState(tokens);
	   case ("RefuseGang"):
		   return RefuseGang.fromRefuseGangEntryInGameState(tokens);
	   case("RefuseChi"):
		   return RefuseChi.fromRefuseChiEntryInGameState(tokens);
	   case("WaitForChi"):
		   return WaitForChi.fromWaitForChiEntryInGameState(tokens);
	   case("WaitForPeng"):
		   return WaitForPeng.fromWaitForPengEntryInGameState(tokens);
	   case("WaitForGang"):
		   return WaitForGang.fromWaitForGangEntryInGameState(tokens);	   
	   case("Hu"):
	       return Hu.fromHuEntryInGameState(tokens);
	   
	   }
	   return null;
   }
}