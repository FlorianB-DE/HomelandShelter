package main.entities;

import main.tiles.Tile;
import textures.Texture;

import java.awt.*;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public abstract class Entity extends Point implements Comparable<Entity> {
    private static int IDCounter = 0;
    private final float ID;
    private final Texture texture;
    private Tile locatedAt;

    //constructor
    public Entity(Tile locatedAt, int priority, Texture texture) {
        super(locatedAt.x, locatedAt.y);
        this.locatedAt = locatedAt;
        this.texture = texture;
        ID = ++IDCounter * Math.round(Math.pow(100, priority));
    }

    public Entity(int priority, Texture texture) {
        locatedAt = null;
        this.texture = texture;
        ID = ++IDCounter * Math.round(Math.pow(100, priority));
    }

    /**
     * compares IDs
     */
    @Override
    public int compareTo(Entity v) {
        return java.lang.Float.compare(v.getID(), ID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity)
            try {
                return ((Entity) obj).compareTo(this) == 0;
            } catch (NullPointerException e) {
                return false;
            }
        return false;
    }

    /**
     * @return the unique ID of this Entity
     */
    public float getID() {
        return ID;
    }

    /**
     * @return the locateAt
     */
    public Tile getLocatedAt() {
        return locatedAt;
    }

    /**
     * @param tile the locateAt to set
     */
    public void setLocatedAt(Tile tile) {
        this.locatedAt = tile;
        if (tile == null) {
            this.x = Integer.MIN_VALUE;
            this.y = Integer.MIN_VALUE;
        } else {
            this.x = locatedAt.x;
            this.y = locatedAt.y;
        }
    }

    /**
     * @return the current texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * @param g the Graphics2D component
     * @param x location
     * @param y location
     */
    public void show(Graphics2D g, int x, int y) {
        if (locatedAt == null) return;
        Composite prev = changeOpacity(g);
        g.drawImage(texture.getContent().getImage(), x, y, getLocatedAt().width, getLocatedAt().height, null);
        g.setComposite(prev);
    }

    @Override
    public String toString() {
        return "Entity-ID: " + ID;
    }

    /**
     * this method changes the opacity of the entity corresponding to the alpha value of its' current Tile. </br>
     *
     * @param g the Graphics2D component
     * @return the old composite
     */
    protected Composite changeOpacity(Graphics2D g) {
        final Composite prev = g.getComposite();
        float alpha = 1 - locatedAt.getAlpha();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        return prev;
    }
}
