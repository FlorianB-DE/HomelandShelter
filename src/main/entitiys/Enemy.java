package main.entitiys;

import main.core.NeighbourFinder;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import textures.Textures;

import java.awt.Point;

/**
 * TODO
 *
 * @author Tim Bauer
 * @version 0.10.0 2020-04-08
 */
public class Enemy extends Entity implements Movement {

	public static final int priority = 1;
	private static final Textures texture = Textures.ENEMY;

	public Enemy(Tile locatedAt, Point pos) {
		super(locatedAt, pos, priority, texture);
	}

	public Enemy(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y, priority, texture);
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
