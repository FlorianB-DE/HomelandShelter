package main.entitiys.items;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import main.core.DungeonGenerator;
import main.entitiys.Entity;
import main.tiles.Tile;
import textures.Textures;
import utils.exceptions.NoSuchAttributeException;

@SuppressWarnings("rawtypes")
public class Item extends Entity {

	private static int uiSize;
	private final List<Attributes> attributes;

	public Item(Tile locatedAt, List<Attributes> attributes) {
		super(locatedAt, 6, Textures
				.valueOf((String) attributes.get(attributes.indexOf(new Attributes<>("texture", null))).getValue()));
		this.attributes = attributes;
	}

	public void use() {
		DungeonGenerator.getPlayer().recieveItemCommand(this);
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		if (getLocatedAt() == null)
			g.drawImage(getTexture().loadImage().getImage(), x, y, uiSize, uiSize, null);
		else
			super.show(g, x, y);
	}

	public static void setUISize(int size) {
		uiSize = size;
	}

	public void pickup() {
		getLocatedAt().removeContent(this);
		setLocatedAt(null);
	}

	public Object getAttributeByString(String s) throws NoSuchAttributeException {
		try {
			return attributes.get(attributes.indexOf(new Attributes<>(s, null))).getValue();
		} catch (Exception e) {
			throw new NoSuchAttributeException();
		}
	}

	public List<Attributes> getAttributes() {
		return new ArrayList<>(attributes);
	}
}