package main.entitiys;

import java.awt.*;

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
		Rectangle locatedAt = getLocatedAt();
		g.drawImage(Textures.CHAR.loadImage().getImage(), locatedAt.x, locatedAt.y, locatedAt.width, locatedAt.height,
				null);
	}

	@Override
	public void move(Floor destination) {
		this.x = destination.x;
		this.y = destination.y;
	}
}
