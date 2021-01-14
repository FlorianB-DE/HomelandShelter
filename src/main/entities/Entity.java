package main.entities;

import main.tiles.Tile;
import textures.Texture;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public abstract class Entity extends Point implements Comparable<Entity> {
	private static int IDCounter = 0;
	private final float ID;
	private Tile locatedAt;
	private Texture texture;

	//constructor
	public Entity(Tile locatedAt, int priority, Texture texture) {
		super(locatedAt.x, locatedAt.y);
		this.locatedAt = locatedAt;
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
	 * @return the current texture
	 */
	public Texture getTexture() {
		return texture;
	}

	/**
	 * @param tiles the locateAt to set
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
	 * @param g the Graphics2D component
	 * @param x location
	 * @param y location
	 */
	public void show(Graphics2D g, int x, int y) {
		Composite prev = changeOpacity(g);
		g.drawImage(texture.getContent().getImage(), x, y, getLocatedAt().width, getLocatedAt().height, null);
		g.setComposite(prev);
	}
	
	@Override
	public String toString() {
		return "Entity-ID: " + java.lang.Float.toString(ID);
	}

	/**
	 * this method changes the opacity of the entity corresponding to the alpha value of its' current Tile. </br>
	 * 
	 * @param g the Graphics2D component
	 * @return the old composite
	 */
	private Composite changeOpacity(Graphics2D g) {
		Composite prev = g.getComposite();
		float alpha = 1 - locatedAt.getAlpha();
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g.setComposite(composite);
		return prev;
	}
}
