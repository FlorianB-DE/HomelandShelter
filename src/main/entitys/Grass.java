package main.entitys;

import main.entitys.items.Loottable;
import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.ItemCreationFailedException;

public class Grass extends Entity {

	// less priority than Item
	public static final int priority = 7;

	private static final Texture[] textures = { // class Textures
			TextureReader.getTextureByString("GRASS"), // normal Texture
			TextureReader.getTextureByString("GRASS_DESTROYED") }; // destroyed Texture

	private static final Loottable loottable = new Loottable();

	// constructor
	public Grass(Tile locatedAt, boolean destroyed) {
		super(locatedAt, priority, destroyed ? textures[1] : textures[0]);
	}

	// constructor
	public Grass(Tile locatedAt) {
		super(locatedAt, priority, textures[0]);
	}

	/**
	 * destroys this Entity and replaces it with a destroyed Texture
	 */
	public void destroy() {
		if (getTexture() == textures[0]) {
			getLocatedAt().addContent(new Grass(getLocatedAt(), true));
			try {
				loottable.createRandomItem(getLocatedAt());
			} catch (ItemCreationFailedException e) {
				System.err.println(e.getMessage());
				// nothing happens
			}
			getLocatedAt().removeContent(this);
		}
	}

}
