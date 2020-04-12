package main.entitiys;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;

import main.tiles.Tile;
import textures.Textures;

public class StairUp extends Entity {
	
	public static final int priority = 2;

	public StairUp(Tile locatedAt, Point pos) {
		super(locatedAt, pos, priority);
	}

	public StairUp(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y, priority);
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		Composite prev = changeOpacity(g);
		g.drawImage(Textures.UPSTAIR_LEFT.loadImage().getImage(), x, y, getLocatedAt().width, getLocatedAt().height,
				null);
		g.setComposite(prev);
	}

}
