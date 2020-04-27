package main.entitiys;

import main.tiles.Tile;
import textures.Textures;

/**
 * TODO
 * 
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public class StairDown extends Entity {

	private static final Textures[] texture = { Textures.DOWNSTAIR_LEFT, Textures.DOWNSTAIR_RIGHT };

	public static final int priority = 2;

	public StairDown(Tile locatedAt) {
		super(locatedAt, priority, texture[(int) Math.round(Math.random() * texture.length - 1)]);
	}
}
