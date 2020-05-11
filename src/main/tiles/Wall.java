package main.tiles;

import java.awt.Point;
import java.util.List;

import main.entitys.Entity;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.PathNotFoundException;

/**
 * TODO
 *
 * @author Florian M. Becker
 * @version 0.9 05.04.2020
 */
public class Wall extends Tile {

	public static final Texture[] texture = { TextureReader.getTextureByString("WALL"),
			TextureReader.getTextureByString("WALL2") };
	private static float chance = 0.025F;

	public Wall(Point p, int size) {
		super(p, size, texture[Math.random() > chance ? 0 : 1]);
	}

	public Wall(int x, int y, int size) {
		super(x, y, size, texture[(int) Math.floor(Math.random() > chance ? 0 : 1)]);
	}

	public Wall(int x, int y) {
		super(x, y, texture[(int) Math.floor(Math.random() > chance ? 0 : 1)]);
	}

	public Wall(int x, int y, int size, int textureNumber) {
		super(x, y, size, texture[textureNumber]);
	}

	@Override
	public void addContent(Entity content) {
		throw new PathNotFoundException("Can not move into Wall!");
	}

	@Override
	public Entity getContent(int at) {
		throw new PathNotFoundException("Can not move into Wall!");
	}

	@Override
	public List<Entity> getContents() {
		throw new PathNotFoundException("Can not move into Wall!");
	}

	@Override
	public boolean isWalkable() {
		return false;
	}

	@Override
	public void removeContent(Entity content) {
		throw new PathNotFoundException("Can not move into Wall!");
	}
}
