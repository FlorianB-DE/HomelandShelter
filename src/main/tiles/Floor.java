package main.tiles;

import java.awt.Point;

import textures.Textures;

/**
 * TODO
 * 
 * @author Florian M. Becker
 * @version 1.0 04.04.2020
 */
public class Floor extends Tile {

	public static final Textures image = Textures.PATH;

	public Floor(Point p, int size) {
		super(p, size, image);
	}

	public Floor(int x, int y, int size) {
		super(x, y, size, image);
	}

	public Floor(int x, int y) {
		super(x, y, image);
	}

	public Floor(Point p) {
		this(p.x, p.y);
	}
}
