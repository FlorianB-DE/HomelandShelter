package main.tiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import textures.Textures;

/**
 * TODO
 * @author Florian M. Becker
 * @version 0.9 05.04.2020
 */
public class Wall extends Tile {

	public Wall(Point p, int size) {
		super(p, size);
	}

	public Wall(int x, int y, int size) {
		super(x, y, size);
	}
	
	public Wall(int x, int y) {
		super(x, y);
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		g.drawImage(Textures.WALL.loadImage().getImage(), x, y, width, height, Color.black, null);
		super.show(g, x, y);
	}

}
