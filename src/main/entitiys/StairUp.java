package main.entitiys;

import main.tiles.Tile;
import textures.Textures;

import java.awt.Point;

public class StairUp extends Entity {

	public static final int priority = 2;
	private final static Textures[] texture =
			{Textures.UPSTAIR_LEFT, Textures.UPSTAIR_RIGHT};

	public StairUp(Tile locatedAt, Point pos) {
		super(locatedAt, pos, priority,
			  texture[(int) Math.round(Math.random() * texture.length)]);
	}

	public StairUp(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y, priority,
			  texture[(int) Math.round(Math.random() * texture.length)]);
	}
}
