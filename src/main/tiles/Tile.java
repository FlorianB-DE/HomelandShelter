package main.tiles;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import main.entitiys.Character;
import main.entitiys.Entity;

/**
 * TODO
 * @author Florian M. Becker
 * @version 1.0 04.04.2020
 */
public abstract class Tile extends Rectangle {
	
	private List<Entity> content;

	public Tile(Point p, int size) {
		super(p, new Dimension(size, size));
	}

	public Tile(int x, int y, int size) {
		super(x, y, size, size);
	}

	public abstract void show(Graphics2D g, int x, int y);

	public float getDistance(double x, double y) {
		return (float) new Point((int) Math.round(getCenterX()), (int) Math.round(getCenterY())).distance(x, y);
	}
	
	public Character getPlayer() {
		if (content == null)
			return null;
		for (Entity entity : content) {
			if (entity instanceof Character)
				return (Character) entity;
		}
		return null;
	}
	
	/**
	 * @return the content
	 */
	public Entity getContent(int at) {
		if (content == null)
			return null;
		return content.get(at);
	}

	public List<Entity> getContents() {
		if (content == null)
			return null;
		return new ArrayList<Entity>(content);
	}

	/**
	 * @param content the content to set
	 */
	public void addContent(Entity content) {
		content.setLocatedAt(this);
		if (this.content == null)
			this.content = new ArrayList<Entity>();
		this.content.add(content);
	}

	public void removeContent(Entity content) {
		this.content.removeIf(e -> e.compareTo(content) == 0);
		if (this.content.isEmpty()) {
			this.content = null;
		}
	}
	
	protected void showContent(Graphics2D g, int x, int y) {
		if (getContents() != null)
			for (Entity entity : getContents())
				entity.show(g, x, y);
	}

}
