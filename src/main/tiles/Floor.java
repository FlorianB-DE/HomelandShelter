package main.tiles;

import textures.Texture;
import textures.TextureReader;

import java.awt.Point;

/**
 * TODO
 *
 * @author Florian M. Becker
 * @version 1.0 04.04.2020
 */
public class Floor extends Tile {

	public static final Texture texture = TextureReader.getTextureByString("PATH");

	public Floor(int x, int y) {
		super(x, y, texture);
	}

	public Floor(int x, int y, int size) {
		super(x, y, size, texture);
	}

	public Floor(Point p) {
		this(p.x, p.y);
	}

	public Floor(Point p, int size) {
		super(p, size, texture);
	}

	@Override
	public boolean isWalkable() {
		return true;
	}
}
