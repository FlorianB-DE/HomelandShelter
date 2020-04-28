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

	/**
	 * 
	 * @param <T>
	 * @param i
	 * @param s
	 * @param clazz
	 * @return
	 */
	public static <T> T getAttributeByString(Item i, String s, Class<T> c) throws NoSuchAttributeException {
		try {
			return c.cast(i.getAttributes().get(i.getAttributes().indexOf(new Attributes<>(s, null))).getValue());
		} catch (Exception e) {
			throw new NoSuchAttributeException();
		}
	}

	/**
	 * @param s name if the attribute
	 * @return the value associated with the given String. Null if there is none.
	 */
	public Object getAttributeByString(String s) throws NoSuchAttributeException{
		try {
			return attributes.get(attributes.indexOf(new Attributes<>(s, null))).getValue();
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchAttributeException();
		}
	}

	public List<Attributes> getAttributes() {
		return new ArrayList<>(attributes);
	}

	public static void setUISize(int size) {
		uiSize = size;
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		if (getLocatedAt() == null)
			g.drawImage(getTexture().loadImage().getImage(), x, y, uiSize, uiSize, null);
		else
			super.show(g, x, y);
	}

	public void pickup() {
		getLocatedAt().removeContent(this);
		setLocatedAt(null);
	}

	public void use() {
		DungeonGenerator.getPlayer().recieveItemCommand(this);
	}
}
