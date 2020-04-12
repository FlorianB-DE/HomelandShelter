package main.tiles;

import java.awt.Point;

import textures.Textures;

/**
 * TODO
 * 
 * @author Florian M. Becker
 * @version 0.9 05.04.2020
 */
public class Wall extends Tile {

	public static final Textures image = Textures.WALL;

	public Wall(Point p, int size) {
		super(p, size, image);
	}

	public Wall(int x, int y, int size) {
		super(x, y, size, image);
	}

	public Wall(int x, int y) {
		super(x, y, image);
	}
}
