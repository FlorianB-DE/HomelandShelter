package main.tiles;

import java.awt.Point;

import textures.Textures;

/**
 * TODO
 * 
 * @author Florian M. Becker
 * @version 0.9 05.04.2020
 */
public class Wall extends Tile {
	
	private static float chance = 0.025F;

	public static final Textures[] texture = { Textures.WALL, Textures.WALL2 };

	public Wall(Point p, int size) {
		super(p, size, texture[Math.random() > chance ? 0 : 1]);
	}

	public Wall(int x, int y, int size) {
		super(x, y, size, texture[(int) Math.floor(Math.random() > chance ? 0 : 1)]);
	}

	public Wall(int x, int y) {
		super(x, y, texture[(int) Math.floor(Math.random() > chance ? 0 : 1)]);
	}
}
