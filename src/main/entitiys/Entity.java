package main.entitiys;


import java.awt.Graphics2D;
import java.awt.Point;

import main.tiles.Tile;

public abstract class Entity extends Point{

	private Tile locatedAt;
	/**
	 * @return the locateAt
	 */
	
	public Entity(Tile locatedAt, Point pos) {
		super(pos);
		this.locatedAt = locatedAt;
	}
	
	public Entity(Tile locatedAt, int x, int y) {
		super(x, y);
		this.locatedAt = locatedAt;
	}
	
	public Tile getLocatedAt() {
		return locatedAt;
	}

	/**
	 * @param tiles the locateAt to set
	 */
	public void setLocatedAt(Tile tiles) {
		this.locatedAt = tiles;
	}
	
	public abstract void show(Graphics2D g);

	public abstract int compareTo(Entity v);
}
