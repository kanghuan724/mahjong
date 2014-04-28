package org.mahjong.client;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.core.client.GWT;

public interface MahjongConstants extends Constants{	  
	  @DefaultStringValue("Your Turn To Pick Up A Tile")
	  String yourTurn();
	  
	  @DefaultStringValue("Are You Able To Hu? ")
	  String huMsg();
	  
	  @DefaultStringValue("Let me Hu!")
	  String huYes();
	  
	  @DefaultStringValue("No, not now.")
	  String deny();
	  
	  @DefaultStringValue("OK")
	  String accept();
	  
	  @DefaultStringValue("Please Wait For Other Players.....")
	  String waitFor();
	  
	  @DefaultStringValue("Please Discard One Tile")
	  String discardTile();
	  
	  @DefaultStringValue("Wanna Gang That Tile? ")
	  String gangMsg();
	  
	  @DefaultStringValue("Wanna Peng That Tile? ")
	  String pengMsg();
	  
	  @DefaultStringValue("Wanna Chi That Tile? ")
	  String chiMsg();
	  
	  @DefaultStringValue("Let me Peng!")
	  String pengYes();

	  @DefaultStringValue("Let me Gang!")
	  String gangYes();
	  
	  @DefaultStringValue("Let me Chi!")
	  String chiYes();
	  
	  @DefaultStringValue("Viewer")
	  String viewer();
}
