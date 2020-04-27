package main.UI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JTextPane;
import javax.swing.Timer;

import main.Main;
import main.core.DungeonGenerator;
import main.entitiys.items.Item;
import textures.Textures;
import utils.WindowUtils;
import utils.exceptions.NoSuchAttributeException;

/**
 * TODO
 * 
 * @author Florian M. Becker
 */
public class Inventory extends Menue implements ActionListener {

	private InventoryTile[] tiles = new InventoryTile[main.Constants.PLAYER_INVENTORY_SIZE];
	private static final int tiles_per_row = 4;
	private static final int refreshtime = 150;

	// timer
	private Timer updateTimer;

	// constructor
	public Inventory() {

		// instantiate timer
		updateTimer = new Timer(refreshtime, this);

		// removes any Layout from the Panel component
		setLayout(null);

		// setting up bounds
		WindowUtils bounds = new WindowUtils(Main.getGameDimension(), 0.4F, 0.85F);
		setSize(bounds.getWindowDimensions());
		setLocation(bounds.getWindowPosition());

		// set visible
		setVisible(true);

		// repaint screen
		repaint();

		// calculating...
		int tiles_per_column = (int) Math.ceil(tiles.length / (double) tiles_per_row);
		int tileSize = Math.min(bounds.getWidth() / (tiles_per_row + 1), bounds.getHeight() / (tiles_per_column + 1));

		// index translates to the index of the tiles array
		int index = 0;

		// iterate column
		for (int i = 0; i < tiles_per_column; i++) {

			// iterate rows
			for (int j = 0; j < tiles_per_row; j++) {

				// breaking condition
				if (index < tiles.length) {
					// creates new InventoryTile
					tiles[index] = new InventoryTile(
							tileSize / (tiles_per_row * 2) + j * (tileSize + (tileSize / tiles_per_row)),
							tileSize / (tiles_per_column * 2) + i * (tileSize + (tileSize / tiles_per_column)),
							tileSize);
					// adds the name panel to the inventory panel
					add(tiles[index].getNamePanel());

					// increment index
					index++;
				} else
					// when end of the array is reached
					break;
			}
		}

		Item.setUISize(tileSize);

	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible)
			updateTimer.start();
		else
			updateTimer.stop();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isVisible() && getMousePosition() != null) {
			for (InventoryTile inventoryTile : tiles) {
				if (inventoryTile.contains(getMousePosition())) {
					// TODO item.use();
					break;
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

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * checks ever iteration weather the mouse hovers over a particular item slot
		 * and if so displays it
		 */
		if (getMousePosition() != null)
			for (InventoryTile inventoryTile : tiles)
				if (inventoryTile.contains(getMousePosition()))
					inventoryTile.displayContentName(getMousePosition());
				else
					inventoryTile.getNamePanel().setVisible(false);

		// repaint screen to update graphics
		repaint();
	}

	//
	private class InventoryTile extends Rectangle {

		private Item content;
		private JTextPane displayName;

		public InventoryTile(int x, int y, int size) {
			super(x, y, size, size);
			displayName = new JTextPane();
			displayName.setAlignmentY(JTextPane.CENTER_ALIGNMENT);
			displayName.setEditable(false);
			displayName.setSize(width, height / 3);
			setVisible(false);
		}

		public void show(Graphics2D g) {
			g.drawImage(Textures.INVENTORY_TILE.loadImage().getImage(), x, y, width, height, null);
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

		public void displayContentName(Point location) {
			if (content != null) {
				displayName.setLocation(location.x + width / 20, location.y);
				displayName.setVisible(true);
			}
		}

		public JTextPane getNamePanel() {
			return displayName;
		}

//		unused
//		public void removeContent() {
//			content = null;
//		}

	}
}
