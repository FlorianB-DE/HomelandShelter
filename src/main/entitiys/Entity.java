package main.entitiys;

import main.tiles.Tile;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * TODO
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public abstract class Entity extends Point{

	private Tile locatedAt;
	/**
	 * @return the locateAt
	 */
	
	public Entity(Tile locatedAt, Point pos) {
		super(pos);
		this.locatedAt = locatedAt;
	}
	
	public Entity(Tile locatedAt, int x, int y) {
		super(x, y);
		this.locatedAt = locatedAt;
	}
	
	public Tile getLocatedAt() {
		return locatedAt;
	}

	/**
	 * @param tiles the locateAt to set
	 */
	public void setLocatedAt(Tile tiles) {
		this.locatedAt = tiles;
		this.x = locatedAt.x;
		this.y = locatedAt.y;
	}
	
	public abstract void show(Graphics2D g, int x, int y);
	
	public Composite changeOpacity(Graphics2D g) {
		Composite prev = g.getComposite();
		float alpha = 1 - locatedAt.getAlpha();
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g.setComposite(composite);
		return prev;
	}

	public abstract int compareTo(Entity v);
}
