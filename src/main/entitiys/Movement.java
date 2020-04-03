package main.entitiys;

import main.tiles.Floor;

public interface Movement {
	abstract void move(Floor destination);
}
