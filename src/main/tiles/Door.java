/*
 * Door.java 1.0 03 Apr 2020
 * Author: Tim Bauer
 */
package main.tiles;

import java.awt.Point;

import textures.Textures;

/**
 * @author Tim Bauer
 * @version 1.0 2020-04-03
 */
public class Door extends Tile {

	public static final Textures texture = Textures.DOOR;

	private boolean isOpen = true;

	public Door(Point p, int size) {
		super(p, size, texture);
	}

	public Door(int x, int y, int size) {
		super(x, y, size, texture);
	}

	public Door(int x, int y) {
		super(x, y, texture);
	}

	public void openDoor() {
		isOpen = true;
	}

	public void closeDoor() {
		isOpen = false;
	}

	public boolean isClosed() {
		return !isOpen;
	}
}
