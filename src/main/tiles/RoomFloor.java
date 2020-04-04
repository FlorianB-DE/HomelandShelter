package main.tiles;

import main.entitiys.Character;
import main.entitiys.Entity;

import java.awt.*;

public class RoomFloor extends Tile {

	private java.util.List<Entity> content;

	public RoomFloor(Point p, int size) {
		super(p, size);
	}

	public RoomFloor(int x, int y, int size) {
		//this(new Point(x, y), size);
		super(x, y, size);
	}

	@Override
	public void show(Graphics2D g) {
		g.drawRect(x, y, width, height);
		if (content != null) {
			if (!content.isEmpty()) {
				for (Entity entity : content) {
					entity.show(g);
				}
			}
		}
	}

	public Character getPlayer() {
		if (content == null) {
			return null;
		}
		for (Entity entity : content) {
			if (entity instanceof Character) {
				return (Character) entity;
			}
		}
		return null;
	}

	/**
	 * @return the content
	 */
	public Entity getContent(int at) {
		if (content == null) {
			return null;
		}
		return content.get(at);
	}

	public Entity[] getContents() {
		if (content == null) {
			return null;
		}
		return (Entity[]) content.toArray();
	}
}
