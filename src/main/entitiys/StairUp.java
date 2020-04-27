package main.entitiys;

import main.tiles.Tile;
import textures.Textures;

public class StairUp extends Entity {

	public static final int priority = 2;
	private final static Textures[] texture = { Textures.UPSTAIR_LEFT, Textures.UPSTAIR_RIGHT };

	public StairUp(Tile locatedAt) {
		super(locatedAt, priority, texture[(int) Math.round(Math.random() * texture.length - 1)]);
	}
}
