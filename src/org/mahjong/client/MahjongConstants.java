package org.mahjong.client;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.core.client.GWT;

public interface MahjongConstants extends Constants{	  
	  @DefaultStringValue("Your Turn To Pick Up A Tile")
	  String yourTurn();
	  
	  @DefaultStringValue("Are You Able To Hu? ")
	  String huMsg();
	  
	  @DefaultStringValue("Yes,Let me try!")
	  String huYes();
	  
	  @DefaultStringValue("No, not now.")
	  String deny();
	  @DefaultStringValue("Or Click this to Skip")
	  String denyChi();
	  
	  @DefaultStringValue("OK")
	  String accept();
	  
	  @DefaultStringValue("Please Wait For Other Players.....")
	  String waitFor();
	  
	  @DefaultStringValue("Congrats!You win!")
	  String youWin();
	  
	  @DefaultStringValue("Sorry,MahJong luck is not on you!")
	  String youLose();
	  
	  @DefaultStringValue("Please Discard One Tile")
	  String discardTile();
	  
	  @DefaultStringValue("Wanna Gang That Tile? ")
	  String gangMsg();
	  
	  @DefaultStringValue("Wanna Peng That Tile? ")
	  String pengMsg();
	  
	  @DefaultStringValue("Arrange Tiles to Chi ")
	  String chiMsg();
	  
	  @DefaultStringValue("Finish arranging tiles ")
	  String finish();
	  
	  @DefaultStringValue("Let me Peng!")
	  String pengYes();

	  @DefaultStringValue("Let me Gang!")
	  String gangYes();
	  @DefaultStringValue("Please arrange your Tiles")
	  String arrange();
	  @DefaultStringValue("Please select to legit tiles to Chi")
	  String chiYes();
	  @DefaultStringValue("Discard")
	  String discard();
	  @DefaultStringValue("Viewer")
	  String viewer();
	  @DefaultStringValue("Chi")
	  String chi();
	  @DefaultStringValue("Left tiles at wall:")
	  String atWall();
}
