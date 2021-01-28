package main.entities;

import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;

public class Grass extends Entity implements ILootable{

	// less priority than Item
	public static final int priority = 7;

	private static final Texture[] textures = { // class Textures
			TextureReader.getTextureByString("GRASS"), // normal Texture
			TextureReader.getTextureByString("GRASS_DESTROYED") }; // destroyed Texture

	// constructor
	public Grass(Tile locatedAt) {
		super(locatedAt, priority, textures[0]);
	}

	// constructor
	public Grass(Tile locatedAt, boolean destroyed) {
		super(locatedAt, priority, destroyed ? textures[1] : textures[0]);
	}

	/**
	 * destroys this Entity and replaces it with a destroyed Texture
	 */
	public void destroy() {
		if (getTexture() == textures[0]) {
			getLocatedAt().addContent(new Grass(getLocatedAt(), true));
			getLocatedAt().removeContent(this);
			getLocatedAt().addContent(createFromLootTable());
		}
	}
}
