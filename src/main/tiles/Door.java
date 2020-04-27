/*
 * Door.java 1.0 03 Apr 2020
 * Author: Tim Bauer
 */
package main.tiles;

import textures.Textures;
import utils.Direction;

import java.awt.Point;

/**
 * @author Tim Bauer
 * @version 1.0 2020-04-03
 */
public class Door extends Tile {

	public static final Textures texture[] =
			{Textures.DOOR, Textures.LEFT_DOOR, Textures.RIGHT_DOOR};

	private boolean isOpen = true;

	public Door(Point p, int size, Direction direction) {
		super(p, size, direction == Direction.vertical ?
				texture[(int) Math.round(Math.random() * 2 + 1)] : texture[0]);
	}

	public Door(int x, int y, int size, Direction direction) {
		super(x, y, size, direction == Direction.vertical ?
				texture[(int) Math.round(Math.random() * 2 + 1)] : texture[0]);
	}

	public Door(int x, int y, Direction direction) {
		super(x, y, direction == Direction.vertical ?
				texture[(int) Math.round(Math.random() * 2 + 1)] : texture[0]);
	}

	public void closeDoor() {
		isOpen = false;
	}

	public void openDoor() {
		isOpen = true;
	}

	public boolean isClosed() {
		return !isOpen;
	}
}
