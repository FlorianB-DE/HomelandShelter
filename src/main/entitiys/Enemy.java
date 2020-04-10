package main.entitiys;

import main.core.NeighbourFinder;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import textures.Textures;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * TODO
 *
 * @author Tim Bauer
 * @version 0.10.0 2020-04-08
 */
public class Enemy extends Entity implements Movement {
	private final int ID = ++counter;
	private static int counter = 0;

	public Enemy(Tile locatedAt, Point pos) {
		super(locatedAt, pos);
	}

	public Enemy(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y);
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		Composite prev = changeOpacity(g);
		g.drawImage(Textures.ENEMY.loadImage().getImage(), x, y, getLocatedAt().width, getLocatedAt().height, null);
		g.setComposite(prev);
	}

	@Override
	public int compareTo(Entity v) {
		if (v instanceof Enemy) {
			if (this.ID == ((Enemy) v).getID()) {
				return 0;
			}
		}
		return -1;
	}

	public int getID() {
		return ID;
	}

	public void moveEnemy() {
		Tile[] n = NeighbourFinder.findNeighbours(x, y);
		for (int i = 0; i < 10; i++) {
			Tile tile = n[(int) ((Math.random() * 100) % 4)];
			if (tile instanceof RoomFloor) {
				move(tile);
				return;
			}
		}
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
