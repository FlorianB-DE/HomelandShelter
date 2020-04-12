package main.entitiys;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;

import main.tiles.Tile;
import textures.Textures;

/**
 * TODO
 * 
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public class StairDown extends Entity {

	public static final int priority = 2;

	public StairDown(Tile locatedAt, Point pos) {
		super(locatedAt, pos, priority);
	}

	public StairDown(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y, priority);
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		Composite prev = changeOpacity(g);
		g.drawImage(Textures.DOWNSTAIR_LEFT.loadImage().getImage(), x, y, getLocatedAt().width, getLocatedAt().height,
				null);
		g.setComposite(prev);
	}
}
