package main.entitiys.items;

import java.awt.Point;

import main.entitiys.Entity;
import main.tiles.Tile;
import textures.Textures;

public abstract class Item extends Entity {

	public Item(Tile locatedAt, int x, int y, Textures texture) {
		super(locatedAt, x, y, 6, texture);
	}

	public Item(Tile locatedAt, Point pos, Textures texture) {
		super(locatedAt, pos, 6, texture);
	}

	public void pickup() {
		getLocatedAt().removeContent(this);
		setLocatedAt(null);
	}
}
