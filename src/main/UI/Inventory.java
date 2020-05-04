package main.UI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

import main.Main;
import main.entitiys.items.Item;
import main.UI.elements.InventoryTile;
import textures.TextureReader;
import utils.WindowUtils;

/**
 * TODO
 * 
 * @author Florian M. Becker
 */
public final class Inventory extends Menue implements ActionListener {

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
		if (isVisible() && getMousePosition() != null) { // if the click happens on the inventory
			for (InventoryTile inventoryTile : tiles) { // loop all inventory spaces
				if (inventoryTile.contains(getMousePosition())) { // found the right tile
					switch (e.getButton()) {
					case MouseEvent.BUTTON1:
						// TODO item.use();
						return;

					case MouseEvent.BUTTON2:
						return;

					case MouseEvent.BUTTON3:
						Item i = inventoryTile.getContent();
						Gameboard.getCurrentInstance().getPlayer().getLocatedAt().addContent(i);
						inventoryTile.removeContent();
						repaint();
					default:
						return;
					}
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(TextureReader.getTextureByString("INVENTORY_BACKGROUND").getContent().getImage(), 0, 0,
				getWidth(), getHeight(), null);
		for (int i = 0; i < tiles.length; i++) {
			if (i < Gameboard.getCurrentInstance().getPlayer().getInventoryContents().size())
				tiles[i].setContent(Gameboard.getCurrentInstance().getPlayer().getInventoryContents().get(i));
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
}
