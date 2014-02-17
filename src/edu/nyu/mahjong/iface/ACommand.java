package edu.nyu.mahjong.iface;

import edu.nyu.mahjong.logic.Tile;

public abstract class ACommand extends Equality{
	public abstract String getName();
	public abstract Tile getTarget();

}
