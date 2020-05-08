package main.UI.elements;

import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JTextPane;

import main.entitiys.items.Item;
import textures.TextureReader;
import utils.exceptions.NoSuchAttributeException;

public final class InventoryTile extends UIElement {

	private Item content;
	private JTextPane displayName;

	public InventoryTile(int x, int y, int size) {
		super(x, y, size, size);
		displayName = new JTextPane();
		displayName.setVisible(false);
		displayName.setAlignmentY(JTextPane.CENTER_ALIGNMENT);
		displayName.setEditable(false);
		displayName.setSize(width, height / 3);
	}

	public void paint(Graphics2D g) {
		g.drawImage(TextureReader.getTextureByString("INVENTORY_TILE").getContent().getImage(), x, y, width, height,
				null);
		if (content != null)
			content.show(g, x, y);
	}

	public void setContent(Item content) {
		this.content = content;
		if (content != null)
			try {
				displayName.setText("    " + (String) content.getAttributeByString("name"));
			} catch (NoSuchAttributeException e) {
				displayName.setText("ERROR");
			}
	}

	public Item getContent() {
		return content;
	}

	public void displayContentName(Point location) {
		if (content != null) {
			displayName.setLocation(location.x + width / 20, location.y);
			displayName.setVisible(true);
		}
	}

	public JTextPane getNamePanel() {
		return displayName;
	}

	public void removeContent() {
		content = null;
	}

}