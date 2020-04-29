package main.entitiys;

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
	private static int counter = 0;
	private final long ID;
	private Tile locatedAt;
	private Texture texture;

	public Entity(Tile locatedAt, int priority, Texture texture) {
		super(locatedAt.x, locatedAt.y);
		this.locatedAt = locatedAt;
		this.texture = texture;
		ID = ++counter * Math.round(Math.pow(100, priority));
	}

	@Override
	public int compareTo(Entity v) {
		if (getID() == v.getID()) {
			return 0;
		} else if (getID() > v.getID()) {
			return -1;
		}
		return 1;
	}

	public void show(Graphics2D g, int x, int y) {
		Composite prev = changeOpacity(g);
		g.drawImage(texture.getContent(), x, y, getLocatedAt().width,
					getLocatedAt().height, null);
		g.setComposite(prev);
	}

	private Composite changeOpacity(Graphics2D g) {
		Composite prev = g.getComposite();
		float alpha = 1 - locatedAt.getAlpha();
		AlphaComposite composite =
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g.setComposite(composite);
		return prev;
	}
	
	public long getID() {
		return ID;
	}

	/**
	 * @return the locateAt
	 */
	public Tile getLocatedAt() {
		return locatedAt;
	}
	
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
}
