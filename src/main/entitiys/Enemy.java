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
 * @version 0.9.5 2020-04-05
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

		moveEnemy();
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
		int x = getLocatedAt().x;
		int y = getLocatedAt().y;

		Tile[] n = NeighbourFinder.findNeighbours(x, y);
		Tile dest = null;
		for (int i = 0; i < 10; i++) {
			dest = n[(int) Math.round(Math.random() * (n.length - 1))];
			if (dest instanceof RoomFloor) {
				break;
			}
		}
		try {
			move(dest);
		} catch (NullPointerException e) {

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
