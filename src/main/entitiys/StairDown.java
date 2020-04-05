package main.entitiys;

import java.awt.Graphics2D;
import java.awt.Point;

import main.tiles.Tile;

public class StairDown extends Entity {

	public StairDown(Tile locatedAt, Point pos) {
		super(locatedAt, pos);
	}

	public StairDown(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y);
	}

	@Override
	public void show(Graphics2D g) {
		// TODO Auto-generated method stub

	}

}
