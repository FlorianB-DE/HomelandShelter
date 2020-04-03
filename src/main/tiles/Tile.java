package main.tiles;

import java.awt.*;

public abstract class Tile extends Rectangle {

	public Tile(Point p, int size) {
		super(p, new Dimension(size, size));
		// TODO Auto-generated constructor stub
	}

	public Tile(int x, int y, int size) {
		super(x, y, size, size);
	}

	public abstract void show(Graphics2D g);

	public float getDistance(double x, double y) {
		return (float) new Point((int) Math.round(getCenterX()), (int) Math.round(getCenterY())).distance(x, y);
	}

}
