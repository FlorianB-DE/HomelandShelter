package main.entities.items;

import main.entities.Entity;
import main.entities.items.behaviour.Behaviour;
import main.tiles.Tile;
import textures.TextureReader;
import utils.exceptions.NoSuchAttributeException;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Item extends Entity {

	public static final int priority = 6;

	private static int uiSize;

	/**
	 * 
	 * @param <T> the expected type note: for primitive types use e.g.: Float.class NOT float.class
	 * @param i   the item which may contain the searched attribute
	 * @param s   the name of the attribute
	 * @return the value of given type
	 * @throws NoSuchAttributeException if it's the wrong type or there is no such
	 *                                  attribute
	 */
	public static <T> T getAttributeByString(Item i, String s, Class<T> c) throws NoSuchAttributeException {
		try {
			return c.cast(i.getAttributes().get(i.getAttributes().indexOf(new Attribute<>(s))).getValue());
		} catch (Exception e) {
			throw new NoSuchAttributeException("Attribute " + s + " not found!");
		}
	}

	public static void setUISize(int size) {
		uiSize = size;
	}

	private final List<Attribute<?>> attributes;

	private Behaviour usingBehaviour;

	public Item(Tile locatedAt, List<Attribute<?>> attributes) {
		super(locatedAt, priority, TextureReader.getTextureByString(
				(String) attributes.get(attributes.indexOf(new Attribute<>("texture", null))).getValue()));
		this.attributes = attributes;
		usingBehaviour = readBehavior();
	}

	/**
	 * @deprecated since 05.2020 replaced with static method
	 * @param s name if the attribute
	 * @return the value associated with the given String. Null if there is none.
	 */
	@Deprecated
	public Object getAttributeByString(String s) throws NoSuchAttributeException {
		try {
			return attributes.get(attributes.indexOf(new Attribute<>(s, null))).getValue();
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchAttributeException();
		}
	}

	public List<Attribute<?>> getAttributes() {
		return new ArrayList<>(attributes);
	}

	public Behaviour getBehaviour() {
		return usingBehaviour;
	}

	public Map<String, String> getStats() {
		final Map<String, String> stats = new HashMap<>(attributes.size() - 1);
		for (Attribute<?> att : attributes) {
			if (att.getKeyWord().compareToIgnoreCase("texture") != 0) {
				final String keyWord = att.getKeyWord().substring(0, 1).toUpperCase() + att.getKeyWord().substring(1);
				stats.put(keyWord, att.getValue().toString());

			}
		}
		return stats;
	}

	public void pickup() {
		getLocatedAt().removeContent(this);
		setLocatedAt(null);
	}

	public void setBehaviour(Behaviour behavior) {
		usingBehaviour = behavior;
	}

	@Override
	public void show(Graphics2D g, int x, int y) {
		if (getLocatedAt() == null)
			g.drawImage(getTexture().getContent().getImage(), x, y, uiSize, uiSize, null);
		else
			super.show(g, x, y);
	}

	/**
	 * returns the ID and the Name of this Item
	 */
	@Override
	public String toString() {
		try {
			return "name: " + getAttributeByString(this, "name", String.class) + " ID: " + getID();
		} catch (NoSuchAttributeException e) {
			return "ID: " + java.lang.Float.toString(getID());
		}
	}

	public void use() {
		usingBehaviour.use();
	}

	private Behaviour readBehavior() {
		final Behaviour newBehaviour;
		try {
			final String className = "main.entities.items.behaviour."
					+ getAttributeByString(this, "behaviour", String.class);
			newBehaviour = (Behaviour) Class.forName(className).getConstructor(Item.class).newInstance(this);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return newBehaviour;
	}
}
