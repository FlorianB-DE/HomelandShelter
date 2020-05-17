package main.entitys.items;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.entitys.Entity;
import main.entitys.items.behaviour.Behaviour;
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

	private Behaviour usingBehaviour;

	public Item(Tile locatedAt, List<Attributes<?>> attributes) {
		super(locatedAt, priority, TextureReader.getTextureByString(
				(String) attributes.get(attributes.indexOf(new Attributes<>("texture", null))).getValue()));
		this.attributes = attributes;
		usingBehaviour = readBehavior();
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

	public Behaviour getBehaviour() {
		return usingBehaviour;
	}

	public Map<String, String> getStats() {
		final Map<String, String> stats = new HashMap<>(attributes.size() - 1);
		for (Attributes<?> att : attributes) {
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
			return "name: " + (String) getAttributeByString("name") + " ID: " + getID();
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
			final String className = "main.entitys.items.behaviour."
					+ getAttributeByString(this, "behaviour", String.class);
			newBehaviour = (Behaviour) Class.forName(className).getConstructor(Item.class).newInstance(this);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return newBehaviour;
	}
}
