package main.entitiys;

import java.awt.Point;

import main.tiles.Tile;
import textures.Textures;

public class StairUp extends Entity {

	private final static Textures[] texture = {Textures.UPSTAIR_LEFT, Textures.UPSTAIR_RIGHT};

	public static final int priority = 2;

	public StairUp(Tile locatedAt, Point pos) {
		super(locatedAt, pos, priority, texture[(int) Math.round(Math.random() * texture.length)]);
	}

	public StairUp(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y, priority, texture[(int) Math.round(Math.random() * texture.length)]);
	}
}
