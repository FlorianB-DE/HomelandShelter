package main.UI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import main.Constants;
import main.entitiys.Player;
import main.entitiys.items.Item;
import main.UI.elements.InventoryElement;
import textures.TextureReader;
import utils.WindowUtils;

/**
 * TODO
 * 
 * @author Florian M. Becker
 */
public final class Inventory extends JPanel implements ActionListener, MouseListener {

	private static final int refreshtime = 50;

	private final InventoryElement[] tiles = new InventoryElement[Constants.PLAYER_INVENTORY_SIZE];
	private final InventoryElement armor, mainHand, offHand;
	private final WindowUtils bounds;

	// timer
	private final Timer updateTimer;

	// constructor
	public Inventory() {
		// instantiate timer
		updateTimer = new Timer(refreshtime, this);

		// removes any Layout from the Panel component
		setLayout(null);

		// setting up bounds
		bounds = new WindowUtils(Constants.GAME_FRAME.getSize(), 0.4F, 0.85F);
		setSize(bounds.getOriginalBounds());
		setLocation(0, 0);

		// set visible
		setVisible(true);

		// repaint screen
		repaint();

		// calculating...
		final int tiles_per_row = Constants.PLAYER_INVENTORY_TILES_PER_ROW;

		int tiles_per_column = (int) Math.ceil(tiles.length / (double) tiles_per_row);
		int tileSize = Math.min(bounds.getWidth() / (tiles_per_row + 1), bounds.getHeight() / (tiles_per_column + 1));

		// index translates to the index of the tiles array
		int index = 0;

		// iterate columns
		for (int i = 0; i < tiles_per_column; i++) {

			// iterate rows
			for (int j = 0; j < tiles_per_row; j++) {

				// breaking condition
				if (index < tiles.length) {
					// creates new InventoryTile
					tiles[index] = new InventoryElement(
							// x position
							bounds.getX() // initial position
									+ tileSize / (tiles_per_row * 2) // initial gap
									+ j * (tileSize // one tile distance
											+ (tileSize / tiles_per_row)), // spacing between tiles
							// y position
							bounds.getY() // initial position
									+ tileSize / (tiles_per_column * 2) // initial gap
									+ i * (tileSize // one tile distance
											+ (tileSize / tiles_per_column)), // spacing between tiles
							tileSize); // size

					// increment index
					index++;
				} else
					// when end of the array is reached
					break;
			}
		}

		this.armor = new InventoryElement(bounds.getX() - (int) Math.round(tileSize * 1.1), bounds.getY(), tileSize);
		this.mainHand = new InventoryElement(bounds.getX() - (int) Math.round(tileSize * 1.1),
				bounds.getY() + (int) Math.round(tileSize * 1.1), tileSize);
		this.offHand = new InventoryElement(bounds.getX() - (int) Math.round(tileSize * 1.1),
				bounds.getY() + ((int) Math.round(tileSize * 1.1) * 2), tileSize);

		Item.setUISize(tileSize);
		Constants.GAME_FRAME.setGlassPane(this);
		Constants.GAME_FRAME.revalidate();
		Constants.GAME_FRAME.repaint();
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
			for (int i = 0; i < tiles.length; i++) { // loop all inventory spaces
				if (tiles[i].contains(getMousePosition())) { // found the right tile
					switch (e.getButton()) {
					case MouseEvent.BUTTON1: // left click
						// TODO item.use();
						return;

					case MouseEvent.BUTTON2: // middle mouse button
						return;

					case MouseEvent.BUTTON3: // left click
						final Item item = tiles[i].getContent();
						final Player c = Gameboard.getCurrentInstance().getPlayer();
						// remove from player inventory
						tiles[i].removeContent();
						c.removeItem(item);

						// place at players location
						c.getLocatedAt().addContent(item);
						repaint();
					default:
						return;
					}
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// nothing happens
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// nothing happens
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// nothing happens
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// nothing happens
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// draw background
		g2d.drawImage(TextureReader.getTextureByString("INVENTORY_BACKGROUND").getContent().getImage(), // get image
				bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), null); // bounds and image observer

		final Player p = Gameboard.getCurrentInstance().getPlayer();

		// iterate item Tiles
		for (int i = 0; i < tiles.length; i++) {
			// if slot is occupied by an Item it gets set as content
			if (i < p.getInventoryContents().size())
				tiles[i].setContent(Gameboard.getCurrentInstance().getPlayer().getInventoryContents().get(i));
			else
				tiles[i].removeContent();
			// show tile
			tiles[i].paint(g2d);
		}

		// paint name displays
		for (InventoryElement ie : tiles)
			ie.paintNameDisplay(g2d);

		// draw equipment slots
		final InventoryElement[] equipmentSlots = { armor, mainHand, offHand };
		final Item[] equipment = p.getEquipment();
		for (int i = 0; i < equipmentSlots.length; i++) {
			equipmentSlots[i].setContent(equipment[i]);
			equipmentSlots[i].paint(g2d);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * checks ever iteration weather the mouse hovers over a particular item slot
		 * and if so displays it
		 */
		if (getMousePosition() != null) // mouse is inside inventory bounds
			for (InventoryElement inventoryTile : tiles) // iterate every tile
				if (inventoryTile.contains(getMousePosition())) // if mouse position is inside inventory tile bounds
					inventoryTile.displayContentName(getMousePosition()); // display name tag
				else
					inventoryTile.removeNameDisplay();

		// repaint screen to update graphics
		repaint();
	}
}
