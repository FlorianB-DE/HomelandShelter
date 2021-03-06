package main.core;

import main.Constants;
import main.UI.Gameboard;
import main.tiles.Tile;

/**
 * @author Tim Bauer
 * @version 0.1.0 2020-04-08
 */
public abstract class NeighbourFinder {

	/**
	 * NOT FOR USE INSIDE DUNGEON GENERATOR
	 */
	public static Tile[] findNeighbors(int x, int y) {
		return findNeighborsOnTilegrid(x, y, Gameboard.getCurrentInstance().getTilegrid());
	}

	public static Tile[] findNeighborsOnTilegrid(int x, int y, Tile[][] tilegrid) {
		Tile[] n = null;
		n = new Tile[4];

		if (y - 1 >= 0) {
			n[0] = tilegrid[x][y - 1];
		}
		if (x + 1 < Constants.DUNGEON_SIZE) {
			n[1] = tilegrid[x + 1][y];
		}
		if (y + 1 < Constants.DUNGEON_SIZE) {
			n[2] = tilegrid[x][y + 1];
		}
		if (x - 1 >= 0) {
			n[3] = tilegrid[x - 1][y];
		}

		return n;
	}

	public static boolean isNeighbor(int x1, int y1, int x2, int y2) {
		Tile[] n = findNeighbors(x1, y1);
		for (Tile t : n) {
			try {
				if (t.x == x2 && t.y == y2) {
					return true;
				}
			} catch (Exception e) {
			}
		}
		return false;
	}

	/**
	 * NOT FOR USE INSIDE DUNGEON GENERATOR
	 */
	public static int pathableNeighbors(int x, int y) {
		int count = 0;
		for (Tile tile : findNeighbors(x, y)) {
			if (tile != null) {
				if (tile.isWalkable()) {
					count++;
				}
			}
		}

		return count;
	}

	public static int pathableNeighborsOnTilegrid(int x, int y, Tile[][] tilegrid) {
		int count = 0;
		for (Tile tile : findNeighborsOnTilegrid(x, y, tilegrid)) {
			if (tile != null) {
				if (tile.isWalkable()) {
					count++;
				}
			}
		}

		return count;
	}
}
