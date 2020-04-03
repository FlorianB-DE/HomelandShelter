package main.entitiys;

import java.awt.*;

import main.tiles.Floor;
import main.tiles.Tile;

public abstract class Entity extends Tile {

	private Floor locatedAt;

	public Entity(Point p, int size) {
		super(p, size);
	}

	public Entity(int x, int y, int size) {
		super(x, y, size);
	}

	/**
	 * @return the locateAt
	 */
	public Floor getLocatedAt() {
		return locatedAt;
	}

	/**
	 * @param locateAt the locateAt to set
	 */
	public void setLocatedAt(Floor locateAt) {
		this.locatedAt = locateAt;
	}

}
