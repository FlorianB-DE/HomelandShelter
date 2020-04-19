package main.UI;

import java.awt.Graphics;

import main.Main;
import main.tiles.Tile;
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
		g.drawImage(Textures.START_BUTTON.loadImage().getImage(), 0, 0, getWidth(), getHeight(), null);
	}

	private class InventoryTile extends Tile {

		public InventoryTile(int x, int y) {
			super(x, y, Textures.INVENTORY_TILE);
		}

	}

}
