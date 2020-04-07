package main.core;

import main.UI.Gameboard;
import main.tiles.Tile;

public class NeighbourFinder {

	public static Tile[] findNeighbours(int x, int y) {

		Tile[][] grid = Gameboard.getTilegrid();
		Tile[] n = new Tile[4];


		if (y - 1 >= 0) {
			n[0] = grid[x][y - 1];
		}
		if (x + 1 < grid.length) {
			n[1] = grid[x + 1][y];
		}
		if (y + 1 < grid[x].length) {
			n[2] = grid[x][y + 1];
		}
		if (x - 1 >= 0) {
			n[3] = grid[x - 1][y];
		}

		return n;
	}
}
