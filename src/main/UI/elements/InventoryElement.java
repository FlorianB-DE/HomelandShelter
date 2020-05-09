package main.UI.elements;

import java.awt.Graphics2D;
import java.awt.Point;
import main.entitiys.items.Item;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.NoSuchAttributeException;

public final class InventoryElement extends UIElement {

	private Item content;
	private NameDisplay displayName;
	private Texture texture;

	public InventoryElement(int x, int y, int size) {
		this(x, y, size, null);
	}

	public InventoryElement(int x, int y, int size, Texture texture) {
		super(x, y, size, size);
		displayName = new NameDisplay(width, height / 3);
		this.texture = texture;
	}

	@Override
	public void paint(Graphics2D g) {
		if(content == null) {
			if(texture == null)
				g.drawImage(TextureReader.getTextureByString("INVENTORY_TILE_NEW").getContent().getImage(), x, y, width, height, null);
			else
				g.drawImage(texture.getContent().getImage(), x, y, width, height, null);
		}else {
			g.drawImage(TextureReader.getTextureByString("INVENTORY_TILE_NEW").getContent().getImage(), x, y, width, height, null);
			content.show(g, x, y);
		}
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
	
	public void setTexture(Texture texture) {
		this.texture = texture;
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

	public Texture getTexture() {
		return texture;
	}
}