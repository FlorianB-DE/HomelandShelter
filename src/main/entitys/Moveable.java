package main.entitys;

import main.tiles.Tile;

/**
 * TODO
 *
 * @author Florian M. Becker
 * @version 0.1 04.04.2020
 */
public interface Moveable {

	abstract void move(Tile destination);
}
