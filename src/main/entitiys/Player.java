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
import java.util.List;
import java.util.Queue;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public final class Player extends Entity implements Movement, Fightable {

	// entity related
	public static final int priority = 0;

	// texture
	private static final Texture texture = TextureReader.getTextureByString("CHAR");

	// inventory
	private final List<Item> inventory;
	private final Inventory inventoryGUI;

	// future visiting locations
	private Queue<Point> path;

	// equipment slots
	private Item mainHand, offHand, armor;

	// stats
	private double health;
	private int level;

	public Player(Tile locatedAt) {
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

	/**
	 * @param path the player has to go to reach destination. @ATTENTION overrides
	 *             old path
	 */
	public void addPath(Queue<Point> path) {
		this.path = path;
	}

	/**
	 * @return current attack value (in best case: level + mainHand + offHand)
	 */
	@Override
	public float attack() {
		float additives = 0;

		// checks for equipment in his hands
		Item[] hands = { mainHand, offHand };
		for (int i = 0; i < hands.length; i++) {
			try {
				additives += (float) hands[i].getAttributeByString("damage");
			} catch (ClassCastException cce) {
				// damage is not correctly defined
				throw new NoSuchAttributeException();
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
			throw new NoSuchAttributeException();
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

	/**
	 * Calls detection() method when reached the last item in the queue.</br>
	 * Stops moving when encountering content of type Hitable.
	 * 
	 * @return
	 * 
	 * @true when a step is made and there is something left in the queue,
	 * @false when it reaches the last point in the queue or if the next Tile has a
	 *        content of type Hitable or the queue is empty.
	 */
	public boolean moveStep() {
		if (path == null)
			return false;

		// retrieve next destination
		final Point nextPoint = path.poll();
		final Tile next = Gameboard.getCurrentInstance().getTileAt(nextPoint.x, nextPoint.y);

		// stops if next Tile has a content of type Hitable
		if (next.hasHitableContent(this)) {
			path = null;
			// attack Hitable target
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
	 * @return a copy of the inventory List
	 */
	public List<Item> getInventoryContents() {
		return new ArrayList<Item>(inventory);
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

	public void removeItem(Item i) {
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