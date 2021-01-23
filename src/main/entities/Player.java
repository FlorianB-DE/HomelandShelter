package main.entities;

import main.Constants;
import main.ui.GameBoard;
import main.ui.Inventory;
import main.entities.items.Item;
import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.CanNotMoveException;
import utils.exceptions.NoSuchAttributeException;
import utils.math.MathUtils;

import java.awt.Point;
import java.util.Arrays;
import java.util.Queue;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 0.9 05.04.2020
 */
public final class Player extends Creature implements Movable, Fightable {

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
		level = 0;
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
	 * <b>ATTENTION</b> overrides old path
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
		for (Item hand : hands) {
			try {
				additives += Item.getAttributeByString(hand, "damage", float.class);
			} catch (ClassCastException cce) {
				// damage is not correctly defined
				throw new NoSuchAttributeException();
			} catch (NoSuchAttributeException ignore) {
				// item has nothing for combat => nothing happens
			} catch (NullPointerException ignore) {
				// there is nothing in the hand => nothing happens
			}
		}
		return level + additives;
	}

	/**
	 * add Entities the player can interact with here
	 *
	 * @param at the tile to check
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
	 * @return the current Item in the main hand. null if empty
	 */
	public Item getMainHand() {
		return equipment[1];
	}

	@Override
	public double getMaxHealth() {
		return Constants.MAX_PLAYER_HEALTH + Constants.PLAYER_HEALTH_GROWTH * level;
	}

	/**
	 * @return the current Item in the off hand. null if empty
	 */
	public Item getOffHand() {
		return equipment[2];
	}

	private float getProtection() {
		float protection = 1;
		for (Item item : equipment) {
			if (item != getMainHand()) {
				try {
					protection *= Item.getAttributeByString(item, "protection", float.class);
				} catch (NoSuchAttributeException e) {
					// no corresponding attribute found
				}
			}
		}
		return protection;
	}

	/**
	 * implements Hittable interface
	 */
	@Override
	public void hit(float damage) {
		final double newHealth = getHealth() - MathUtils.convertToDouble(damage * getProtection());
		if (newHealth <= 0)
			die();
		else
			setHealth(newHealth);
	}

	/**
	 * implements Movable interface
	 */
	@Override
	public void move(Tile destination) {
		if (!destination.hasMovableContent()) {
			getLocatedAt().removeContent(this);
			destination.addContent(this);
		}
	}

	/**
	 * Calls detection() method when reached the last item in the queue.</br>
	 * Stops moving when encountering content of type Hittable.
	 * 
	 * @return : <br>
	 * 
	 * <b>true</b> when a step is made and there is something left in the queue,
	 * <b>false</b> when it reaches the last point in the queue or if the next Tile has a
	 *        content of type Hittable or the queue is empty.
	 */
	public boolean moveStep() {
		try {
			doEffectTicks();

			if (path == null || path.isEmpty())
				return false;

			// retrieve next destination
			final Point nextPoint = path.poll();
			final Tile next = GameBoard.getCurrentInstance().getTileAt(nextPoint.x, nextPoint.y);

			// stops if next Tile has a content of type Hittable
			if (next.hasHittableContent(this)) {
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
		} catch (CanNotMoveException e) {
			return true;
		}
	}

	public boolean removeFromInventory(Item i) {
		return getInventory().remove(i);
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
	 * @param mainHand set new mainHand
	 */
	public void setMainHand(Item mainHand) {
		equipment[1] = mainHand;
	}

	/**
	 * @param offHand set new offHand
	 */
	public void setOffHand(Item offHand) {
		equipment[2] = offHand;
	}

	@Override
	public void trueHit(float damage) {
		setHealth(getHealth() - MathUtils.convertToDouble(damage));
		if (getHealth() <= 0)
			die();
	}
}