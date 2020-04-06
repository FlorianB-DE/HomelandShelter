package main.entitiys;

import java.awt.Graphics2D;
import java.awt.Point;

import main.tiles.Tile;

/**
 * TODO
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public class StairDown extends Entity {

	public StairDown(Tile locatedAt, Point pos) {
		super(locatedAt, pos);
	}

	public StairDown(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y);
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(Entity v) {
		if (v instanceof StairDown) {
			if (v.getX() == this.getX() && v.getY() == this.getY()) {
				return 0;
			}
		}
		return -1;
	}
}
