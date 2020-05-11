package main.entitys;

import main.Constants;
import main.UI.Gameboard;
import main.UI.Inventory;
import main.entitys.items.Item;
import main.entitys.items.behavior.Equip;
import main.entitys.items.behavior.Wielding;
import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.CommandNotFoundException;
import utils.exceptions.NoSuchAttributeException;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Queue;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
/**
 * @author Florian Becker
 *
 */
public final class Player extends Creature implements Moveable, Fightable {

	// entity related
	public static final int priority = 0;

	// texture
	private static final Texture texture = TextureReader.getTextureByString("CHAR");

	// equipment slots
	private final Item[] equipment;

	// inventory
	private final Inventory inventoryGUI;

	// stats
	private int level;

	// future visiting locations
	private Queue<Point> path;

	public Player(Tile locatedAt) {
		super(locatedAt, priority, texture);
		inventoryGUI = new Inventory();
		equipment = new Item[3];

		setHealth(Constants.MAX_PLAYER_HEALTH);
		level = 1;
	}

	/**
	 * @param i Item to add to the inventory.
	 * @return true if Item was successfully added or i == null. False when full
	 */
	public boolean addItem(Item i) {
		if (i == null)
			return true;
		if (getInventory().size() < Constants.PLAYER_INVENTORY_SIZE) {
			if (i.getLocatedAt() != null)
				i.pickup();
			getInventory().add(i);
			return true;
		} else
			return false;
	}

	/**
	 * @param path the player has to go to reach destination. </br>
	 * @ATTENTION overrides old path
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
		Item[] hands = { equipment[1], equipment[2] };
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
			} else if (e instanceof StairDown) {
				System.out.println("Bravo Six going down"); // go to next level
			} else if (e instanceof Grass)
				((Grass) e).destroy();
		}
	}

	/**
	 * implements Fightable interface
	 */
	@Override
	public void die() {
		// TODO
		System.exit(0);
	}

	@Override
	public void dropItem(Item i) {
		removeItem(i);
		super.dropItem(i);
	}

	/**
	 * @return the armor Item. null if empty
	 */
	public Item getArmor() {
		return equipment[0];
	}

	/**
	 * @return all equipment slots as array. </br>
	 *         NOTE that this is a copy and NOT the original
	 */
	public Item[] getEquipment() {
		return Arrays.copyOf(equipment, equipment.length);
	}

	/**
	 * @return the players Inventory GUI
	 */
	public Inventory getInventoryGUI() {
		return inventoryGUI;
	}

	/**
	 * @return the MouseListener component from players Inventory instance
	 */
	public MouseListener getInventoryListener() {
		return inventoryGUI;
	}

	/**
	 * @return the Inventory GUI visibility
	 */
	public boolean getInventoryVisibility() {
		return inventoryGUI.isVisible();
	}

	/**
	 * @return the current Item in the main hand. null if empty
	 */
	public Item getMainHand() {
		return equipment[1];
	}

	/**
	 * @return the current Item in the off hand. null if empty
	 */
	public Item getOffHand() {
		return equipment[2];
	}

	/**
	 * implements Hitable interface
	 */
	@Override
	public void hit(float damage) {
		try {
			setHealth(
					getHealth() - damage * Item.getAttributeByString(getArmor(), "protection", java.lang.Float.class));
		} catch (Exception e) {
			setHealth(getHealth() - damage);
		}
		if (getHealth() <= 0) {
			die();
		}
	}

	/**
	 * implements Moveable interface
	 */
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
			return false;
		} else {
			move(next);
			if (path.isEmpty()) {
				detection(next);
				path = null;
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * is called from Item.use() and looks for an Attribute of Name "command" of
	 * type String
	 * 
	 * @param source
	 */
	public void recieveItemCommand(Item source) {
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
	}

	/**
	 * @param i removes i from inventory List AND equipment slots
	 */
	public void removeItem(Item i) {
		if (!getInventory().remove(i)) {
			for (int it = 0; it < equipment.length; it++) {
				if (i.equals(equipment[it]))
					equipment[it] = null;
			}
		}
	}

	/**
	 * @param armor set new armor
	 */
	public void setArmor(Item armor) {
		equipment[0] = armor;
	}

	/**
	 * @param state sets the Inventory GUI visibility to "state"
	 */
	public void setInventoryVisibility(boolean state) {
		inventoryGUI.setVisible(state);
	}

	/**
	 * @param mainHand set new mainHand
	 */
	public void setMainHand(Item mainHand) {
		equipment[1] = mainHand;
	}

	/**
	 * @param mainHand set new offHand
	 */
	public void setOffHand(Item offHand) {
		equipment[2] = offHand;
	}

	@Override
	public void trueHit(float damage) {
		setHealth(getHealth() - damage);
		if(getHealth() <= 0)
			die();
	}

	@Override
	protected double getMaxHealth() {
		return Constants.MAX_PLAYER_HEALTH + Constants.PLAYER_HEALTH_GROTH * level;
	}

	/**
	 * @param i the Item that shall be equipped
	 */
	private void equipItem(Item i) {
		getInventory().remove(i);
		if (i.getBehavior().use()) {
			for (int j : ((Wielding) ((Equip) i.getBehavior()).getWielding()).getAffectedEquipmentSlots()) {
				equipment[j] = i;
			}
		}
	}

	private void throwItem(Item i) {

	}

	private void useItem(Item i) {

	}

}
