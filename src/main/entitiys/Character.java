package main.entitiys;

import main.tiles.Tile;
import textures.Textures;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * TODO
 * 
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public class Character extends Entity implements Movement {

	public static final int priority = 0;

	public Character(Tile locatedAt, Point pos) {
		super(locatedAt, pos, priority);
	}

	public Character(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y, priority);
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		Composite prev = changeOpacity(g);
		g.drawImage(Textures.CHAR.loadImage().getImage(), x, y, getLocatedAt().width, getLocatedAt().height, null);
		g.setComposite(prev);
	}

	@Override
	public void move(Tile destination) {
		getLocatedAt().removeContent(this);
		destination.addContent(this);
		for (Entity e : destination.getContents()) {
			if (e instanceof StairDown)
				System.out.println("Bravo Six going down");
		}
	}
}
