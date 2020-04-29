package main.entitiys;

import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;

public class StairUp extends Entity {

	public static final int priority = 2;
	private static final Texture[] texture = { TextureReader.getImageByString("UPSTAIR_LEFT"),
			TextureReader.getImageByString("UPSTAIR_RIGHT") };

	public StairUp(Tile locatedAt) {
		super(locatedAt, priority, texture[(int) Math.round(Math.random() * texture.length - 1)]);
	}
}
