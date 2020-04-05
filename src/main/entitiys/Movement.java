package main.entitiys;

import main.tiles.Tile;

public interface Movement {
	abstract void movePlayer(Tile destination);
}
