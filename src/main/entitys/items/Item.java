package main.entitys.items;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import main.entitys.Entity;
import main.entitys.items.behavior.Behavior;
import main.tiles.Tile;
import textures.TextureReader;
import utils.exceptions.NoSuchAttributeException;

public final class Item extends Entity {

	public static final int priority = 6;

	private static int uiSize;

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

	public static void setUISize(int size) {
		uiSize = size;
	}

	private final List<Attributes<?>> attributes;

	private Behavior usingBehavior;

	public Item(Tile locatedAt, List<Attributes<?>> attributes) {
		super(locatedAt, priority, TextureReader.getTextureByString(
				(String) attributes.get(attributes.indexOf(new Attributes<>("texture", null))).getValue()));
		this.attributes = attributes;
		usingBehavior = readBehavior();
	}

	/**
	 * @param s name if the attribute
	 * @return the value associated with the given String. Null if there is none.
	 */
	public Object getAttributeByString(String s) throws NoSuchAttributeException {
		try {
			return attributes.get(attributes.indexOf(new Attributes<>(s, null))).getValue();
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchAttributeException();
		}
	}

	public List<Attributes<?>> getAttributes() {
		return new ArrayList<>(attributes);
	}

	public Behavior getBehavior() {
		return usingBehavior;
	}

	public void pickup() {
		getLocatedAt().removeContent(this);
		setLocatedAt(null);
	}

	public void setBehavior(Behavior behavior) {
		usingBehavior = behavior;
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		if (getLocatedAt() == null)
			g.drawImage(getTexture().getContent().getImage(), x, y, uiSize, uiSize, null);
		else
			super.show(g, x, y);
	}

	@Override
	public String toString() {
		try {
			return "name: " + (String) getAttributeByString("name") + " ID: " + getID();
		} catch (NoSuchAttributeException e) {
			return "ID: " + java.lang.Float.toString(getID());
		}
	}

	public void use() {
		usingBehavior.use();
	}

	private Behavior readBehavior() {
		final Behavior newBehavior;
		try {
			final String className = "main.entitys.items.behavior."
					+ getAttributeByString(this, "behavior", String.class);
			newBehavior = (Behavior) Class.forName(className).getConstructor(Item.class).newInstance(this);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return newBehavior;
	}
}
