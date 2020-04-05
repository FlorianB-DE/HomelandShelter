package main.tiles;

import java.awt.Graphics2D;
import java.awt.Point;

import textures.Textures;

public class Floor extends Tile {

	public Floor(Point p, int size) {
		super(p, size);
	}

	public Floor(int x, int y, int size) {
		// this(new Point(x, y), size);
		super(x, y, size);
	}

	@Override
	public void show(Graphics2D g) {
		g.drawImage(Textures.PATH.loadImage().getImage(), x, y, width, height, null);
		showContent(g);
	}

}
