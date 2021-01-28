package main.core;

import main.Constants;
import main.tiles.Tile;
import main.ui.GameBoard;

/**
 * @author Tim Bauer
 * @version 0.1.0 2020-04-08
 */
public abstract class NeighbourFinder {

    /**
     * NOT FOR USE INSIDE DUNGEON GENERATOR
     */
    public static Tile[] findNeighbors(int x, int y) {
        return findNeighborsOnTileGrid(x, y, GameBoard.getCurrentInstance().getTileGrid());
    }

    public static Tile[] findNeighborsOnTileGrid(int x, int y, Tile[][] tileGrid) {
        Tile[] n = new Tile[4];

        if (y - 1 >= 0) {
            n[0] = tileGrid[x][y - 1];
        }
        if (x + 1 < Constants.DUNGEON_SIZE) {
            n[1] = tileGrid[x + 1][y];
        }
        if (y + 1 < Constants.DUNGEON_SIZE) {
            n[2] = tileGrid[x][y + 1];
        }
        if (x - 1 >= 0) {
            n[3] = tileGrid[x - 1][y];
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
            } catch (Exception ignored) {
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

    public static int pathableNeighborsOnTileGrid(int x, int y, Tile[][] tileGrid) {
        int count = 0;
        for (Tile tile : findNeighborsOnTileGrid(x, y, tileGrid)) {
            if (tile != null) {
                if (tile.isWalkable()) {
                    count++;
                }
            }
        }

        return count;
    }
}
