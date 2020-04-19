package main.tiles;

import java.awt.Point;

import textures.Textures;

/**
 * @author Tim Bauer and Florian M. Becker
 * @version 1.0 2020-04-03
 */
public class RoomFloor extends Tile {

	public static final Textures texture = Textures.PATH;

	public RoomFloor(Point p, int size) {
		super(p, size, texture);
	}

	public RoomFloor(int x, int y, int size) {
		// this(new Point(x, y), size);
		super(x, y, size, texture);
	}

	public RoomFloor(int x, int y) {
		super(x, y, texture);
	}

	@Override
	public boolean isWalkable() {
		return true;
	}
}
