/*
 * Door.java 1.0 03 Apr 2020
 * Author: Tim Bauer
 */
package main.tiles;

import main.entities.Entity;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.PathNotFoundException;
import utils.math.Direction;

import java.awt.*;

/**
 * @author Tim Bauer
 * @version 1.0 2020-04-03
 */
public class Door extends Tile {

	public static final Texture[] texture = { TextureReader.getTextureByString("DOOR"), TextureReader.getTextureByString("LEFT_DOOR"), TextureReader.getTextureByString("RIGHT_DOOR") };

	private boolean isOpen = true;

	public Door(int x, int y, Direction direction) {
		super(x, y, direction == Direction.vertical ? texture[(int) Math.round(Math.random() * 2 + 1)] : texture[0]);
	}

	public Door(int x, int y, int size, Direction direction) {
		super(x, y, size,
				direction == Direction.vertical ? texture[(int) Math.round(Math.random() * 2 + 1)] : texture[0]);
	}

	public Door(Point p, int size, Direction direction) {
		super(p, size, direction == Direction.vertical ? texture[(int) Math.round(Math.random() * 2 + 1)] : texture[0]);
	}

	@Override
	public void addContent(Entity content) {
		if (isOpen)
			super.addContent(content);
		else
			throw new PathNotFoundException("Door is closed!");
	}

	public void closeDoor() {
		isOpen = false;
	}

	public boolean isClosed() {
		return !isOpen;
	}

	@Override
	public boolean isWalkable() {
		return isOpen;
	}

	public void openDoor() {
		isOpen = true;
	}
}
