package main.UI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import main.Main;
import main.core.DungeonGenerator;
import main.entitiys.items.Item;
import textures.Textures;
import utils.WindowUtils;

/**
 * TODO
 * @author Florian M. Becker
 */
public class Inventory extends Menue {

	private InventoryTile[] tiles = new InventoryTile[main.entitiys.Character.inventorySize];
	private static final int tiles_per_row = 4;

	public Inventory() {
		WindowUtils bounds = new WindowUtils(Main.getGameDimension(), 0.4F, 0.85F);
		setSize(bounds.getWindowDimensions());
		setLocation(bounds.getWindowPosition());
		setVisible(true);
		repaint();

		int tiles_per_collum = (int) Math.ceil(tiles.length / (double) tiles_per_row);
		int tileSize = Math.min(bounds.getWidth() / (tiles_per_row + 1), bounds.getHeight() / (tiles_per_collum + 1));

		int index = 0;
		for (int i = 0; i < tiles_per_collum; i++) {
			for (int j = 0; j < tiles_per_row; j++) {
				if (index < tiles.length) {
					tiles[index] = new InventoryTile(
							tileSize / (tiles_per_row * 2) + j * (tileSize + (tileSize / tiles_per_row)),
							tileSize / (tiles_per_collum * 2) + i * (tileSize + (tileSize / tiles_per_collum)),
							tileSize);
					index++;
				} else
					break;
			}
		}
		
		Item.setUISize(tileSize);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isVisible()) {
			for (InventoryTile inventoryTile : tiles) {
				if (inventoryTile.contains(getMousePosition())) {
					//TODO item.use();
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(Textures.INVENTORY_BACKGROUND.loadImage().getImage(), 0, 0, getWidth(), getHeight(), null);
		for (int i = 0; i < tiles.length; i++) {
			tiles[i].setContent(DungeonGenerator.getPlayer().getInventoryContents()[i]);
			tiles[i].show(g2d);
		}
	}

	private class InventoryTile extends Rectangle {

		private Item content;

		public InventoryTile(int x, int y, int size) {
			super(x, y, size, size);
		}

		public void show(Graphics2D g) {
			g.drawImage(Textures.INVENTORY_TILE.loadImage().getImage(), x, y, width, height, null);
			if (content != null)
				content.show(g, x, y);
		}

		public void setContent(Item content) {
			this.content = content;
		}

//		unused
//		public void removeContent() {
//			content = null;
//		}

	}

}
