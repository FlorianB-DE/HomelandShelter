package main.entities;

import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public class StairDown extends Entity {

    public static final int priority = 2;
    private static final Texture[] texture = {TextureReader.getTextureByString("DOWNSTAIR_LEFT"), TextureReader.getTextureByString("DOWNSTAIR_RIGHT")};

    public StairDown(Tile locatedAt) {
        super(locatedAt, priority, texture[(int) Math.round(Math.random() * (texture.length - 1))]);
    }

}
