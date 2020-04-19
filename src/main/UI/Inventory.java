package main.UI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import main.Main;
import main.entitiys.items.Item;
import textures.Textures;
import utils.WindowUtils;

public class Inventory extends Menue {

	private InventoryTile[] tiles = new InventoryTile[main.entitiys.Character.inventorySize];
	private static final int tiles_per_row = 4;

	public Inventory() {
		WindowUtils bounds = new WindowUtils(Main.getGameDimension(), 0.4F, 0.95F);
		setSize(bounds.getWindowDimensions());
		setLocation(bounds.getWindowPosition());
		setVisible(true);
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(Textures.INVENTORY_BACKGROUND.loadImage().getImage(), 0, 0, getWidth(), getHeight(), null);

	}

	private class InventoryTile extends Rectangle {

		private Item content;

		public InventoryTile(int x, int y, int size) {
			super(x, y, size, size);
		}

		public void show(Graphics2D g) {
			g.drawImage(Textures.INVENTORY_TILE.loadImage().getImage(), x, y, width, height, null);
		}

	}

}
