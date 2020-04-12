package main.tiles;

import main.Main;
import main.entitiys.Character;
import main.entitiys.Entity;
import textures.Textures;
import utils.Fractions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO
 * 
 * @author Florian M. Becker
 * @version 1.0 04.04.2020
 */
public abstract class Tile extends Rectangle {

	private float alpha;
	private List<Entity> content;
	private Textures i;

	public Tile(Point p, int size, Textures image) {
		super(p, new Dimension(size, size));
	}

	public Tile(int x, int y, int size, Textures image) {
		this(new Point(x, y), size, image);
	}

	public Tile(int x, int y, Textures image) {
		this(x, y, 0, image);
	}

	public void show(Graphics2D g, int x, int y) {
		float devider = 2.4F;
		int centerX = x + width / 2;
		int centerY = y + height / 2;
		double sqDist = Point.distanceSq(centerX, centerY, Main.getGameDimension().getWidth() / 2,
				Main.getGameDimension().getHeight() / 2);
		for (Fractions fraction : Fractions.values()) {
			if (sqDist >= Math.pow((Main.getGameDimension().getWidth() / devider), 2) * fraction.val
					+ Math.pow((Main.getGameDimension().getHeight() / devider), 2) * fraction.val) {
				alpha = fraction.val;
				break;
			}
		}

		g.setColor(new Color(0F, 0F, 0F, alpha));
		g.fillRect(x, y, width, height);

		g.drawImage(i.loadImage().getImage(), x, y, width, height, null);

		if (content != null) {
			Collections.sort(content);
			showContent(g, x, y);
		}
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

	public float getAlpha() {
		return alpha;
	}

}
