package main.entitiys;

import main.tiles.Tile;
import textures.Textures;

import java.awt.Point;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public class StairDown extends Entity {

	public static final int priority = 2;
	private static final Textures[] texture =
			{Textures.DOWNSTAIR_LEFT, Textures.DOWNSTAIR_RIGHT};

	public StairDown(Tile locatedAt, Point pos) {
		super(locatedAt, pos, priority,
			  texture[(int) Math.round(Math.random() * texture.length)]);
	}

	public StairDown(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y, priority,
			  texture[(int) Math.round(Math.random() * texture.length)]);
	}
}
