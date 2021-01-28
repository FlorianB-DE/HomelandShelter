package main.ui;

import main.Constants;
import main.entities.Player;
import main.entities.items.Item;
import main.ui.elements.InventoryElement;
import textures.Texture;
import textures.TextureReader;
import utils.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO
 * 
 * @author Florian M. Becker
 */
public final class Inventory extends JPanel implements ActionListener {

	@SuppressWarnings("SpellCheckingInspection")
	private static final Texture[] equipmentSlotsTextures = {
			TextureReader.getTextureByString("INVENTORY_TILE_NEW_ARMORTEST"),
			TextureReader.getTextureByString("INVENTORY_TILE_NEW_WEAPON"),
			TextureReader.getTextureByString("INVENTORY_TILE_NEW_SHIELDTEST1") };

	private static final int refreshTime = 50;
	private final int tileSize;
	private final WindowUtils bounds;
	private final InventoryElement[] equipmentSlots;
	private final InventoryElement[] tiles = new InventoryElement[Constants.PLAYER_INVENTORY_SIZE];

	// timer
	private final Timer updateTimer;

	// constructor
	public Inventory() {
		// instantiate timer
		updateTimer = new Timer(refreshTime, this);

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

		final int tiles_per_column = (int) Math.ceil(tiles.length / (double) tiles_per_row);
		tileSize = Math.min(bounds.getWidth() / (tiles_per_row + 1),
				bounds.getHeight() / (tiles_per_column + 1));

		// index translates to the index of the tiles array
		int index = 0;

		// iterate columns
		for (int i = 0; i < tiles_per_column; i++) {

			// iterate rows
			for (int j = 0; j < tiles_per_row; j++) {

				// breaking condition
				if (index < tiles.length) {
					// creates new InventoryTile
					tiles[index] = new InventoryElement( //
							getElementX(j, tileSize, tiles_per_row), // x position
							getElementY(i, tileSize, tiles_per_column), // y position
							tileSize); // size

					// increment index
					index++;
				} else
					// when end of the array is reached
					break;
			}
		}

		equipmentSlots = new InventoryElement[3];

		for (int i = 0; i < equipmentSlots.length; i++)
			equipmentSlots[i] = new InventoryElement(bounds.getX() - (int) Math.round(tileSize * 1.1),
					getElementY(i, tileSize, tiles_per_column), tileSize, equipmentSlotsTextures[i]);

		Item.setUISize(tileSize);
		Constants.GAME_FRAME.setGlassPane(this);
		Constants.GAME_FRAME.revalidate();
		Constants.GAME_FRAME.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * checks ever iteration weather the mouse hovers over a particular item slot
		 * and if so displays it
		 */
		final Point mousePos = getMousePosition();
		if (mousePos != null) // mouse is inside inventory bounds
			for (InventoryElement inventoryTile : getAllElements()) // iterate every tile
				if (inventoryTile.contains(mousePos)) // if mouse position is inside inventory tile bounds
					inventoryTile.displayContentName(mousePos); // display name tag
				else
					inventoryTile.removeNameDisplay();

		// repaint screen to update graphics
		repaint();
	}

	/**
	 * @param e the corresponding mouse event
	 * @return true if visible
	 */
	public boolean mouseClicked(MouseEvent e) {
		if (isVisible()) {
			receiveMouseClick(e);
			return true;
		} else
			return false;
	}

	@Override
	public void paintComponent(Graphics g) {
		final Graphics2D g2d = (Graphics2D) g;

		final Texture texture = TextureReader.getTextureByString("INVENTORY_BACKGROUND");
		// draw background
		g2d.drawImage(texture.getContent().getImage(), // get image
				bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), null); // bounds and image observer
		//draw equipment slots background
		g2d.drawImage(texture.getContent().getImage(), bounds.getX() - (int)(tileSize * 1.2), bounds.getY(), (int)(tileSize * 1.25), (int)(tileSize * 3.65), null);
		
		final Player p = GameBoard.getCurrentInstance().getPlayer();

		// iterate item Tiles
		final List<InventoryElement> elements = getAllElements();
		for (int i = 0; i < elements.size(); i++) {
			// if slot is occupied by an Item it gets set as content
			if (i < p.getInventoryContents().size()) // tiles
				tiles[i].setContent(GameBoard.getCurrentInstance().getPlayer().getInventoryContents().get(i));
			else if (i >= tiles.length) { // equipment slot
				final int at = i - tiles.length;
				final Item equipment = p.getEquipment()[at];
				final InventoryElement element = elements.get(i);
				if (equipment != null)
					element.setContent(equipment);
				else
					element.removeContent();
			} else // tiles without content
				tiles[i].removeContent();
			// show tile
			elements.get(i).paint(g2d);
		}
		// paint name display
		for (InventoryElement ie : elements)
			ie.paintNameDisplay(g2d);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible)
			updateTimer.start();
		else
			updateTimer.stop();
	}

	private List<InventoryElement> getAllElements() {
		List<InventoryElement> elements = new ArrayList<>(Arrays.asList(tiles));
		elements.addAll(Arrays.asList(equipmentSlots));
		return elements;
	}

	private int getElementX(int j, int tileSize, int tiles_per_row) {
		return // x position
		bounds.getX() // initial position
				+ tileSize / (tiles_per_row * 2) // initial gap
				+ j * (tileSize // one tile distance
						+ (tileSize / tiles_per_row)); // spacing between tiles
	}

	private int getElementY(int i, int tileSize, int tiles_per_column) {
		return // y position
		bounds.getY() // initial position
				+ tileSize / (tiles_per_column * 2) // initial gap
				+ i * (tileSize // one tile distance
						+ (tileSize / tiles_per_column)); // spacing between tiles
	}

	private void receiveMouseClick(MouseEvent e) {
		if (e.getPoint() == null)
			return;
		for (InventoryElement element : getAllElements()) {
			if (element.getContent() != null && element.contains(e.getPoint())) {
				switch (e.getButton()) {
				case MouseEvent.BUTTON1:
					// left click (use Item)
					element.removeContent().use();
					break;

				case MouseEvent.BUTTON2:
					// middle mouse (not defined yet=
					break;

				case MouseEvent.BUTTON3:
					// right click (drop Item)
					GameBoard.getCurrentInstance().getPlayer() // get player
							.dropItem(element.removeContent()); // drop Item
					break;

				default:
					return;
				}
				Constants.GAME_FRAME.repaint();
				return;
			}
		}

	}
}
