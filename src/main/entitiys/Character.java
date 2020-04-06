package main.entitiys;

import java.awt.Graphics2D;
import java.awt.Point;

import main.tiles.Tile;
import textures.Textures;

/**
 * TODO
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public class Character extends Entity implements Movement {

	public Character(Tile locatedAt, Point pos) {
		super(locatedAt, pos);
	}
	
	public Character(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y);
	}

	@Override
	public void show(Graphics2D g) {
		g.drawImage(Textures.CHAR.loadImage().getImage(), getLocatedAt().x, getLocatedAt().y, getLocatedAt().width,
				getLocatedAt().height, null);
	}

	@Override
	public void move(Tile destination) {
		getLocatedAt().removeContent(this);
		destination.addContent(this);
		for (Entity e : destination.getContents()) {
			if(e instanceof StairDown)
				System.out.println("Bravo Six going down");
		}
	}

	@Override
	public int compareTo(Entity v) {
		if (v instanceof Character) {
			return 0;
		} else {
			return 1;
		}
	}
}
