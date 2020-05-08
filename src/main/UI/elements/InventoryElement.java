package main.UI.elements;

import java.awt.Graphics2D;
import java.awt.Point;
import main.entitiys.items.Item;
import textures.TextureReader;
import utils.exceptions.NoSuchAttributeException;

public final class InventoryElement extends UIElement {

	private Item content;
	private NameDisplay displayName;

	public InventoryElement(int x, int y, int size) {
		super(x, y, size, size);
		displayName = new NameDisplay(width, height / 3);
	}

	public void paint(Graphics2D g) {
		g.drawImage(TextureReader.getTextureByString("INVENTORY_TILE").getContent().getImage(), x, y, width, height,
				null);
		if (content != null)
			content.show(g, x, y);
	}

	public void removeNameDisplay() {
		displayName.setVisible(false);
	}

	public void paintNameDisplay(Graphics2D g) {
		displayName.paint(g);
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
	}
}