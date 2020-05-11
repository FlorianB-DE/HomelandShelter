package main.entitys;

import main.Constants;
import main.UI.Gameboard;
import main.UI.Inventory;
import main.entitys.items.Item;
import main.entitys.items.behavior.Equip;
import main.entitys.items.behavior.Wielding;
import main.statuseffects.StatusEffect;
import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.CommandNotFoundException;
import utils.exceptions.NoSuchAttributeException;
import utils.exceptions.StatusEffectExpiredException;

import java.awt.Point;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public final class Player extends Entity implements Movement, Fightable {

	// entity related
	public static final int priority = 0;

	// texture
	private static final Texture texture = TextureReader.getTextureByString("CHAR");

	// inventory
	private final List<Item> inventory;
	private final Inventory inventoryGUI;

	private final List<StatusEffect> effects;

	// future visiting locations
	private Queue<Point> path;

	// equipment slots
	private final Item[] equipment;

	// stats
	private double health;
	private int level;

	public Player(Tile locatedAt) {
		super(locatedAt, priority, texture);
		inventory = new ArrayList<>();
		inventoryGUI = new Inventory();
		effects = new ArrayList<>();
		equipment = new Item[3];

		health = Constants.MAX_PLAYER_HEALTH;
		level = 1;
	}

	public void addEffect(StatusEffect e) {
		effects.add(e);
	}

	/**
	 * @param i Item to add to the inventory.
	 * @return true if Item was successfully added or i == null. False when full
	 */
	public boolean addItem(Item i) {
		if (i == null)
			return true;
		if (inventory.size() < Constants.PLAYER_INVENTORY_SIZE) {
			if (i.getLocatedAt() != null)
				i.pickup();
			inventory.add(i);
			return true;
		} else
			return false;
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

	@Override
	public void die() {
		// TODO
		System.exit(0);
	}

	public void doEffectTicks() {
		for (int i = 0; i < effects.size(); i++) {
			try {
				effects.get(i).tick();
			} catch (StatusEffectExpiredException e) {
				effects.remove(i);
			}
		}
	}

	public Inventory getInventoryGUI() {
		return inventoryGUI;
	}

	public Item getMainHand() {
		return equipment[1];
	}

	public Item getOffHand() {
		return equipment[2];
	}

	public Item getArmor() {
		return equipment[0];
	}

	public double getHealth() {
		return health;
	}

	public Item[] getEquipment() {
		return Arrays.copyOf(equipment, equipment.length);
	}

	public void heal(float ammount) {
		if (health + ammount >= Constants.MAX_PLAYER_HEALTH)
			health = Constants.MAX_PLAYER_HEALTH;
		else
			health += java.lang.Double.parseDouble(java.lang.Float.toString(ammount)); // a little complicated but there
																						// are more complex ways to
																						// covert a float to a double
																						// without loosing precision
	}

	@Override
	public void hit(float damage) {
		try {
			health -= damage * (float) getArmor().getAttributeByString("protection");
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
		} catch (CommandNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void equipItem(Item i) throws CommandNotFoundException {
		inventory.remove(i);
		if (i.getBehavior().use()) {
			for (int j : ((Wielding) ((Equip) i.getBehavior()).getWielding()).getAffectedEquipmentSlots()) {
				equipment[j] = i;
			}
		}
	}

	public void removeItem(Item i) {
		if (!inventory.remove(i)) {
			for (int it = 0; it < equipment.length; it++) {
				if (i.equals(equipment[it]))
					equipment[it] = null;
			}
		}
	}

	public void setMainHand(Item mainHand) {
		equipment[1] = mainHand;
	}

	public void setOffHand(Item offHand) {
		equipment[2] = offHand;
	}

	public void setArmor(Item armor) {
		equipment[0] = armor;
	}

	private void useItem(Item i) throws CommandNotFoundException {

	}

	private void throwItem(Item i) throws CommandNotFoundException {

	}

}
