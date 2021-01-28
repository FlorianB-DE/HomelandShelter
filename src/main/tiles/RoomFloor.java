package main.tiles;

import textures.Texture;
import textures.TextureReader;

import java.awt.*;

/**
 * @author Tim Bauer and Florian M. Becker
 * @version 1.0 2020-04-03
 */
public class RoomFloor extends Tile {

    public static final Texture texture = TextureReader.getTextureByString("PATH");

    public RoomFloor(int x, int y) {
        super(x, y, texture);
    }

    public RoomFloor(int x, int y, int size) {
        // this(new Point(x, y), size);
        super(x, y, size, texture);
    }

    public RoomFloor(Point p, int size) {
        super(p, size, texture);
    }

    @Override
    public boolean isWalkable() {
        return true;
    }
}
