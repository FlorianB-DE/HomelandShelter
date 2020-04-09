/*
 * Door.java 1.0 03 Apr 2020
 * Author: Tim Bauer
 */
package main.tiles;

import java.awt.Graphics2D;
import java.awt.Point;

import textures.Textures;

/**
 * @author Tim Bauer
 * @version 1.0 2020-04-03
 */
public class Door extends Floor {

	private boolean isOpen = true;

	public Door(Point p, int size) {
		super(p, size);
	}

	public Door(int x, int y, int size) {
		super(x, y, size);
	}

	public Door(int x, int y) {
		super(x, y);
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

	@Override
	public void show(Graphics2D g, int x, int y) {
		g.drawImage(Textures.DOOR.loadImage().getImage(), x, y, width, height, null);
		super.show(g, x, y);
		showContent(g, x, y);
	}
}
