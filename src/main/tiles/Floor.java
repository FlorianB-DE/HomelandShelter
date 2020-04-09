package main.tiles;

import java.awt.Graphics2D;
import java.awt.Point;

import textures.Textures;

/**
 * TODO
 * @author Florian M. Becker
 * @version 1.0 04.04.2020
 */
public class Floor extends Tile {

	public Floor(Point p, int size) {
		super(p, size);
	}

	public Floor(int x, int y, int size) {
		// this(new Point(x, y), size);
		super(x, y, size);
	}
	
	public Floor(int x, int y) {
		super(x, y);
	}

	public Floor(Point p) {
		this(p.x, p.y);
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		g.drawImage(Textures.PATH.loadImage().getImage(), x, y, width, height, null);
		super.show(g, x, y);
		showContent(g, x, y);
	}

}
