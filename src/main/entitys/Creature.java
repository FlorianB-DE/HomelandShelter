package main.entitys;

import main.entitys.items.Item;
import main.statuseffects.StatusEffect;
import main.tiles.Tile;
import textures.Texture;
import utils.exceptions.StatusEffectExpiredException;

import java.util.ArrayList;
import java.util.List;

public abstract class Creature extends Entity {

	private final List<StatusEffect> effects;
	private double health;
	private final List<Item> inventory;

	// constructor
	public Creature(Tile locatedAt, int priority, Texture texture) {
		super(locatedAt, priority, texture);
		inventory = new ArrayList<>();
		effects = new ArrayList<>();
	}

	/**
	 * @param e adds "e" to effects List
	 */
	public void addEffect(StatusEffect e) {
		effects.add(e);
	}

	/**
	 * triggers all effects' tick methods inside effects List
	 */
	protected void doEffectTicks() {
		for (int i = 0; i < effects.size(); i++) {
			try {
				effects.get(i).tick();
			} catch (StatusEffectExpiredException e) {
				effects.remove(i);
			}
		}
	}

	/**
	 * @param i the Item to drop if it is inside the inventory
	 */
	public void dropItem(Item i) {
		if (inventory.remove(i))
			getLocatedAt().addContent(i);
	}

	/**
	 * @return players health
	 */
	public double getHealth() {
		return health;
	}

	/**
	 * @return the inventory as a List of Items
	 */
	protected List<Item> getInventory() {
		return inventory;
	}

	/**
	 * @return a COPY of the inventory List
	 */
	public List<Item> getInventoryContents() {
		return new ArrayList<Item>(getInventory());
	}

	public abstract double getMaxHealth();

	/**
	 * @param ammount. Adds the given amount to the players health. If it would
	 *                 exceed the players MAX_HEALTH as defined in main.Constants
	 *                 the health becomes said constant value
	 */
	public void heal(float amount) {
		if (health + amount >= getMaxHealth())
			health = getMaxHealth();
		else
			health += java.lang.Double.parseDouble(java.lang.Float.toString(amount)); // a little complicated but there
																						// are more complex ways to
																						// covert a float to a double
																						// without loosing precision
	}

	protected void setHealth(double health) {
		this.health = health;
	}

}
