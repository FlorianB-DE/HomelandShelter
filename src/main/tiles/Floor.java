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

	public static final Textures texture = Textures.PATH;

	public Floor(Point p, int size) {
		super(p, size, texture);
	}

	public Floor(int x, int y, int size) {
		super(x, y, size, texture);
	}

	public Floor(int x, int y) {
		super(x, y, texture);
	}

	public Floor(Point p) {
		this(p.x, p.y);
	}

	@Override
	public boolean isWalkable() {
		return true;
	}
}
