package main.entitiys.items;

import java.awt.Graphics2D;
import java.awt.Point;

import main.entitiys.Entity;
import main.tiles.Tile;
import textures.Textures;

public abstract class Item extends Entity {
	
	private int uiSize;

	public Item(Tile locatedAt, int x, int y, Textures texture) {
		super(locatedAt, x, y, 6, texture);
	}

	public Item(Tile locatedAt, Point pos, Textures texture) {
		super(locatedAt, pos, 6, texture);
	}
	
	@Override
	public void show(Graphics2D g, int x, int y) {
		if(getLocatedAt() != null)
			super.show(g, x, y);
		else {
			g.drawImage(getTexture().loadImage().getImage(), x, y, uiSize, uiSize, null);
		}
	}
	
	public void setUISize(int size) {
		uiSize = size;
	}

	public void pickup() {
		getLocatedAt().removeContent(this);
		setLocatedAt(null);
		
	}
}
