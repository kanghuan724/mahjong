package edu.nyu.mahjong.logic;

import edu.nyu.mahjong.iface.*;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public class factory {
   public static Equality makeCommand(ImmutableList<String> tokens)
   {
	   if (tokens==null)
		   return Pick.fromPickEntryInGameState(tokens);
	   switch (tokens.get(0))
	   {
	   case ("PickUp"):
	     return Pick.fromPickEntryInGameState(tokens);
	   case("Throw"):
		   return Throw.fromThrowEntryInGameState(tokens);
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
	   case("Hu"):
	       return Hu.fromHuEntryInGameState(tokens);
	   
	   }
	   return null;
   }
}
