package main.core;

import main.tiles.Tile;
import main.tiles.Wall;

/**
 * @author Tim Bauer
 * @version 0.1.0 2020-04-08
 */
public abstract class NeighbourFinder {

	public static Tile[] findNeighbours(int x, int y) {
		Tile[] n = null;
		n = new Tile[4];

		if (y - 1 >= 0) {
			n[0] = DungeonGenerator.getTileAt(x, y - 1);
		}
		if (x + 1 < DungeonGenerator.SIZE) {
			n[1] = DungeonGenerator.getTileAt(x + 1, y);
		}
		if (y + 1 < DungeonGenerator.SIZE) {
			n[2] = DungeonGenerator.getTileAt(x, y + 1);
		}
		if (x - 1 >= 0) {
			n[3] = DungeonGenerator.getTileAt(x - 1, y);
		}

		return n;
	}

	public static int pathableNeighbours(int x, int y) {
		int count = 0;
		for (Tile tile : findNeighbours(x, y))
			if (tile != null)
				if (!(tile instanceof Wall))
					count++;

		return count;

	}
}
