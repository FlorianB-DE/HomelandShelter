package main.entitiys;

import java.awt.Color;
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
		Color prev = g.getColor();
		g.setColor(new Color(0F, 0F, 0F, 1 - getLocatedAt().getAlpha()));
		g.fillRect(x + 1, y + 1, getLocatedAt().width - 2, getLocatedAt().height - 2);
		g.setColor(prev);
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
