package main.entitiys;

import main.UI.Gameboard;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import textures.Textures;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Arrays;

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

		g.drawImage(Textures.ENEMY.loadImage().getImage(), x, y, getLocatedAt().width, getLocatedAt().height, null);
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
		Tile[] n = new Tile[5];
		Tile[][] grid = Gameboard.getTilegrid();

		int x = getLocatedAt().x;
		int y = getLocatedAt().y;

		Arrays.fill(n, grid[x][y]);

		if (x + 1 < grid.length) {
			n[0] = grid[x + 1][y];
		}
		if (y + 1 < grid[x].length) {
			n[1] = grid[x][y + 1];
		}
		if (x - 1 >= 0) {
			n[2] = grid[x - 1][y];
		}
		if (y - 1 >= 0) {
			n[3] = grid[x][y - 1];
		}
		Tile dest = null;
		while (!(dest instanceof RoomFloor)) {
			dest = n[(int) Math.round(Math.random() * 4)];
		}
		move(dest);
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
