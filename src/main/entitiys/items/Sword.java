package main.entitiys.items;

import java.awt.Point;

import main.tiles.Tile;
import textures.Textures;

public class Sword extends Item {

	public Sword(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y, Textures.FIRE);
	}

	public Sword(Tile locatedAt, Point pos) {
		super(locatedAt, pos, Textures.FIRE);
	}
	
	

}
