package main.UI.elements;

import java.awt.Graphics2D;
import java.awt.Point;

import main.entitys.items.Item;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.NoSuchAttributeException;

public final class InventoryElement extends UIElement {

	private Item content;
	private NameDisplay displayName;
	private static final Texture defaultTexture = TextureReader.getTextureByString("INVENTORY_TILE_NEW");

	public InventoryElement(int x, int y, int size) {
		this(x, y, size, defaultTexture);
	}

	public InventoryElement(int x, int y, int size, Texture texture) {
		super(x, y, size, size);
		displayName = new NameDisplay(width, height / 3);
		setTexture(texture);
	}

	@Override
	public void paint(Graphics2D g) {
		if (content != null) {
			final Texture prev = getTexture();
			setTexture(defaultTexture);
			super.paint(g);
			setTexture(prev);
			content.show(g, x, y);
		} else
			super.paint(g);
	}

	public void paintNameDisplay(Graphics2D g) {
		if (displayName.isVisible())
			displayName.paint(g);
	}

	/**
	 * same as "displayName.setVisible(false)"
	 */
	public void removeNameDisplay() {
		displayName.setVisible(false);
	}

	public void setContent(Item content) {
		this.content = content;
		if (content != null)
			try {
				displayName.setDisplayText((String) content.getAttributeByString("name"));
			} catch (NoSuchAttributeException e) {
				displayName.setDisplayText("ERROR");
			}
	}

	public Item getContent() {
		return content;
	}

	public void displayContentName(Point location) {
		if (content != null) {
			displayName.setLocation(location.x + width / 10, location.y);
			displayName.setVisible(true);
		}
	}

	public void removeContent() {
		content = null;
		removeNameDisplay();
	}
}