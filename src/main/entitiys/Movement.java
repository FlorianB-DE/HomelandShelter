package main.entitiys;

import main.tiles.Tile;

public interface Movement {
	abstract void move(Tile destination);
}
