package main.entitiys;

import main.tiles.Tile;
import textures.Textures;

import java.awt.*;

public class Enemy extends Entity implements Movement {
	public Enemy(Tile locatedAt, Point pos) {
		super(locatedAt, pos);
	}

	public Enemy(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y);
	}

	@Override
	public void show(Graphics2D g) {
		g.drawImage(Textures.ENEMY.loadImage().getImage(), getLocatedAt().x,
					getLocatedAt().y, getLocatedAt().width,
					getLocatedAt().height, null);
	}

	@Override
	public int compareTo(Entity v) {
		if (v instanceof Enemy) {
			if (v.getX() == this.getX() && v.getY() == this.getY()) {
				return 0;
			}
		}
		return -1;
	}

	@Override
	public void move(Tile destination) {
		getLocatedAt().removeContent(this);
		destination.addContent(this);
		for (Entity e : destination.getContents()) {
			if (e instanceof Character) {
				System.out.println("Got ya");
			}
		}
	}
}
