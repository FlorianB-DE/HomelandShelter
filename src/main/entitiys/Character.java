package main.entitiys;


import java.awt.Graphics2D;
import java.awt.Point;

import main.tiles.Floor;
import textures.Textures;

public class Character extends Entity implements Movement {

	public Character(Point p, int size) {
		super(p, size);
	}

	public Character(int x, int y, int size) {
		super(x, y, size);
	}

	@Override
	public void show(Graphics2D g) {
		g.drawImage(Textures.CHAR.loadImage().getImage(), getLocatedAt().x, getLocatedAt().y, getLocatedAt().width, getLocatedAt().height,
				null);
	}

	@Override
	public void move(Floor destination) {
		this.x = destination.x;
		this.y = destination.y;
	}
}
