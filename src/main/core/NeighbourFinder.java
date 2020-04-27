package main.core;

import main.UI.Gameboard;
import main.tiles.Tile;
import main.tiles.Wall;

/**
 * @author Tim Bauer
 * @version 0.1.0 2020-04-08
 */
public abstract class NeighbourFinder {

	public static Tile[] findNeighbours(int x, int y) {
		return findNeighbours(x, y,
							  Gameboard.getCurrentInstance().getTilegrid());
	}

	public static Tile[] findNeighbours(int x, int y, Tile[][] tilegrid) {
		Tile[] n = null;
		n = new Tile[4];

		if (y - 1 >= 0) {
			n[0] = tilegrid[x][y - 1];
		}
		if (x + 1 < tilegrid.length) {
			n[1] = tilegrid[x + 1][y];
		}
		if (y + 1 < tilegrid[x].length) {
			n[2] = tilegrid[x][y + 1];
		}
		if (x - 1 >= 0) {
			n[3] = tilegrid[x - 1][y];
		}

		return n;
	}

	public static int pathableNeighbours(int x, int y) {
		int count = 0;
		for (Tile tile : findNeighbours(x, y)) {
			if (tile != null) {
				if (!(tile instanceof Wall)) {
					count++;
				}
			}
		}

		return count;
	}

	public static int pathableNeighbours(int x, int y, Tile[][] tilegrid) {
		int count = 0;
		for (Tile tile : findNeighbours(x, y, tilegrid)) {
			if (tile != null) {
				if (!(tile instanceof Wall)) {
					count++;
				}
			}
		}

		return count;
	}
}
