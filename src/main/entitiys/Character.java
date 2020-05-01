package main.entitiys;

import main.Constants;
import main.UI.Gameboard;
import main.UI.Inventory;
import main.entitiys.items.Item;
import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.CommandNotFoundException;
import utils.exceptions.InventoryFullException;
import utils.exceptions.NoSuchAttributeException;

import javax.swing.JPanel;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public class Character extends Entity implements Movement, Fightable {

	public static final int priority = 0;

	private static final Texture texture = TextureReader.getTextureByString("CHAR");
	private final List<Item> inventory;
	private Queue<Point> path;
	private Inventory inventoryGUI;

	private Item mainHand, offHand, armor;

	private double health;
	private int level;

	public Character(Tile locatedAt) {
		super(locatedAt, priority, texture);
		mainHand = null;
		offHand = null;
		armor = null;
		inventory = new ArrayList<Item>();
		inventoryGUI = new Inventory();

		health = 100;
		level = 1;
	}

	public void addInventoryGUI(JPanel p) {
		p.add(inventoryGUI);
	}

	/**
	 * @param i Item to add to the inventory.
	 * @throws InventoryFullException if the inventory is full.
	 */
	public void addItem(Item i) throws InventoryFullException {
		if (inventory.size() < Constants.PLAYER_INVENTORY_SIZE) {
			i.pickup();
			inventory.add(i);
		} else {
			throw new InventoryFullException();
		}
	}

	public void addPath(Queue<Point> path) {
		this.path = path;
	}

	/**
	 * @return current attack value (in best case: level + mainHand + offHand)
	 */
	@Override
	public float attack() {
		float additives = 0;
		Item[] hands = { mainHand, offHand };
		for (int i = 0; i < hands.length; i++) {
			try {
				additives += (float) hands[i].getAttributeByString("damage");
			} catch (ClassCastException cce) {
				// damage is not correctly defined
				System.exit(-1);
			} catch (NoSuchAttributeException nsae) {
				// item has nothing for combat => nothing happens
			} catch (NullPointerException npe) {
				// there is nothing in the hand => nothing happens
			}
		}

		return level + additives;
	}

	/**
	 * add Entitys the player can interact with here
	 *
	 * @param at
	 * @return true if the path is blocked
	 */
	public void detection(Tile at) {
		for (Entity e : at.getContents()) {
			if (e instanceof Item) {
				addItem((Item) e);
			}
			if (e instanceof StairDown) {
				System.out.println("Bravo Six going down"); // go to next level
			}
		}
	}

	@Override
	public void die() {
		// TODO
		System.exit(0);
	}

	@Override
	public void hit(float damage) {
		try {
			health -= damage * (float) armor.getAttributeByString("protection");
		} catch (ClassCastException cce) {
			// damage is not correctly defined
			System.exit(-1);
		} catch (NoSuchAttributeException nsae) {
			// item has no protection value => nothing happens
		} catch (NullPointerException npe) {
			// no armor equipped
			health -= damage;
		}
		if (health <= 0) {
			die();
		}
	}

	@Override
	public void move(Tile destination) {
		if (!destination.hasMoveableContent()) {
			getLocatedAt().removeContent(this);
			destination.addContent(this);
		}
	}

	public boolean moveStep() {
		if (path == null) {
			return false;
		}
		Point nextPoint = path.poll();
		Tile next = Gameboard.getCurrentInstance().getTilegrid()[nextPoint.x][nextPoint.y];
		if (next.hasHitableContent(this)) {
			path = null;
			next.hit(attack());
			return false;
		} else {
			move(next);
			if (path.isEmpty()) {
				detection(next);
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * @return an array with size "Constants" with the contents of the Inventory
	 *         List. Not occupied spaces return null.
	 */
	public Item[] getInventoryContents() {
		Iterator<Item> it = inventory.listIterator();
		Item[] contents = new Item[Constants.PLAYER_INVENTORY_SIZE];
		for (int i = 0; i < contents.length; i++) {
			try {
				contents[i] = it.next();
			} catch (Exception e) {
				contents[i] = null;
			}
		}
		return contents;
	}

	public boolean getInventoryVisibility() {
		return inventoryGUI.isVisible();
	}

	public void setInventoryVisibility(boolean state) {
		inventoryGUI.setVisible(state);
	}

	public MouseListener getInventoryListener() {
		return inventoryGUI;
	}

	/**
	 * is called from Item.use() and looks for an Attribute of Name "command" of
	 * type String
	 * 
	 * @param source
	 */
	public void recieveItemCommand(Item source) {
		try {
			switch ((String) source.getAttributeByString("command")) {
			case "equip":
				equipItem(source);
				break;
			case "use":
				useItem(source);
				break;
			case "throw":
				throwItem(source);
				break;
			default:
				throw new CommandNotFoundException();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private void equipItem(Item i) throws CommandNotFoundException {
		// remove item from inventory
		inventory.remove(i);

		switch ((String) i.getAttributeByString("wielding")) {
		case "off_hand":
			inventory.add(offHand);
			offHand = i;
			break;
		case "main_hand":
			inventory.add(mainHand);
			mainHand = i;
			break;
		case "dual":
			if (inventory.size() <= Constants.PLAYER_INVENTORY_SIZE - 1) {
				removeItems(new Item[] { mainHand, offHand });
				mainHand = i;
				offHand = i;
			} else
				inventory.add(i);
			break;
		case "armor":
			inventory.add(armor);
			armor = i;
			break;
		default:
			throw new CommandNotFoundException();
		}
	}

	private void removeItem(Item i) {
		inventory.remove(i);
	}

	private void removeItems(Item[] i) {
		if (i != null)
			for (Item item : i)
				removeItem(item);
	}

	private void useItem(Item i) throws CommandNotFoundException {

	}

	private void throwItem(Item i) throws CommandNotFoundException {

	}

}
