package main.entitiys;

import main.UI.Inventory;
import main.core.DungeonGenerator;
import main.entitiys.items.Item;
import main.tiles.Tile;
import textures.Textures;
import utils.exceptions.InventoryFullException;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import javax.swing.JPanel;

/**
 * TODO
 * 
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public class Character extends Entity implements Movement {

	public static final int inventorySize = 20;
	private List<Item> inventory = new ArrayList<Item>();
	private Inventory inventoryGUI = new Inventory();

	private Queue<Point> path;

	private static final Textures texture = Textures.CHAR;

	public static final int priority = 0;

	public Character(Tile locatedAt, Point pos) {
		super(locatedAt, pos, priority, texture);
	}

	public Character(Tile locatedAt, int x, int y) {
		super(locatedAt, x, y, priority, texture);
	}

	@Override
	public void move(Tile destination) {
		getLocatedAt().removeContent(this);
		destination.addContent(this);
	}

	public void addItem(Item i) throws InventoryFullException {
		if (inventory.size() < inventorySize) {
			i.pickup();
			inventory.add(i);
		} else
			throw new InventoryFullException();
	}

	/**
	 * @return an array with size "inventorySize" with the contents of the Inventory
	 *         List. Not occupied spaces return null.
	 */
	public Item[] getInventoryContents() {
		Iterator<Item> it = inventory.listIterator();
		Item[] contents = new Item[inventorySize];
		for (int i = 0; i < contents.length; i++) {
			try {
				contents[i] = it.next();
			} catch (Exception e) {
				contents[i] = null;
			}
		}
		return contents;
	}

	public boolean moveStep() {
		try {
			Point p = path.poll();
			move(DungeonGenerator.getTileAt(p.x, p.y));
			if (path.isEmpty()) {
				detection(DungeonGenerator.getTileAt(p.x, p.y));
			}
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}

	public void addPath(Queue<Point> path) {
		this.path = path;
	}
	
	/**
	 * add here Entitys the player can interact with
	 * @param at
	 */
	private void detection(Tile at) {
		for (Entity e : at.getContents()) {
			if (e instanceof Item)
				addItem((Item) e);
			if (e instanceof StairDown)
				System.out.println("Bravo Six going down");
		}
	}
	
	public void setInventoryVisibility(boolean state) {
		inventoryGUI.setVisible(state);
	}
	
	public boolean getInventoryVisibility() {
		return inventoryGUI.isVisible();
	}
	
	public void addInventoryGUI(JPanel p) {
		p.add(inventoryGUI);
	}
}
