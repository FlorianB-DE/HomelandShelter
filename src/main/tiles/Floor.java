package main.tiles;

import java.awt.*;
import java.util.*;

import main.entitiys.Character;
import main.entitiys.Entity;

public class Floor extends Tile {

	protected java.util.List<Entity> content;

	public Floor(Point p, int size) {
		super(p, size);
	}

	public Floor(int x, int y, int size) {
		this(new Point(x, y), size);
	}

	@Override
	public void show(Graphics2D g) {
		g.drawRect(x, y, width, height);
		if (content != null)
			if (!content.isEmpty())
				for (Entity entity : content)
					entity.show(g);
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

	public Entity[] getContents() {
		if (content == null)
			return null;
		return (Entity[]) content.toArray();
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
		this.content.remove(content);
		if (this.content.isEmpty())
			this.content = null;
	}

}
