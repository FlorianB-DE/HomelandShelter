package main.tiles;

import java.awt.Graphics2D;
import java.awt.Point;

import main.entitiys.Entity;
import textures.Textures;

/**
 * @author Tim Bauer and Florian M. Becker
 * @version 1.0 2020-04-03
 */
public class RoomFloor extends Tile {

	public RoomFloor(Point p, int size) {
		super(p, size);
	}

	public RoomFloor(int x, int y, int size) {
		// this(new Point(x, y), size);
		super(x, y, size);
	}

	public RoomFloor(int x, int y) {
		super(x, y);
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		g.drawImage(Textures.PATH.loadImage().getImage(), x, y, width, height, null);
		super.show(g, x, y);
		showContent(g, x, y);
	}
}
