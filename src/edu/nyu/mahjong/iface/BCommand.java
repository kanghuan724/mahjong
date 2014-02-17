package edu.nyu.mahjong.iface;

import edu.nyu.mahjong.logic.Tile;

public abstract class BCommand extends Equality{
	public abstract String getName();
	public abstract int getSource();

}

