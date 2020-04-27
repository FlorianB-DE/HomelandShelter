package main.entitiys;

import main.tiles.Tile;
import textures.Textures;

import java.awt.Point;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public class Character extends Entity implements Movement {

	public static final int priority = 0;
	private static final Textures texture = Textures.CHAR;

	public Character(Tile locatedAt, Point pos) {
		super(locatedAt, pos, priority, texture);
	}

	public Character(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y, priority, texture);
	}

	@Override
	public void move(Tile destination) {
		getLocatedAt().removeContent(this);
		destination.addContent(this);
		for (Entity e : destination.getContents()) {
			if (e instanceof StairDown) {
				System.out.println("Bravo Six going down");
			}
		}
	}
}
