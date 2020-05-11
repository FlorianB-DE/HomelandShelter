package main.entitys;

import java.util.ArrayList;
import java.util.List;

import main.entitys.items.Item;
import main.statuseffects.StatusEffect;
import main.tiles.Tile;
import textures.Texture;
import utils.exceptions.StatusEffectExpiredException;

public abstract class Creature extends Entity {

	private double health;
	private final List<Item> inventory;
	private final List<StatusEffect> effects;

	public Creature(Tile locatedAt, int priority, Texture texture) {
		super(locatedAt, priority, texture);
		inventory = new ArrayList<>();
		effects = new ArrayList<>();
	}

	protected abstract double getMaxHealth();

	/**
	 * @param e adds "e" to effects List
	 */
	public void addEffect(StatusEffect e) {
		effects.add(e);
	}

	/**
	 * triggers all effects' tick methods inside effects List
	 */
	public void doEffectTicks() {
		for (int i = 0; i < effects.size(); i++) {
			try {
				effects.get(i).tick();
			} catch (StatusEffectExpiredException e) {
				effects.remove(i);
			}
		}
	}

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
	
	/**
	 * @return players health
	 */
	public double getHealth() {
		return health;
	}

	protected void setHealth(double health) {
		this.health = health;
	}

	protected List<Item> getInventory() {
		return inventory;
	}
	
	/**
	 * @return a COPY of the inventory List
	 */
	public List<Item> getInventoryContents() {
		return new ArrayList<Item>(getInventory());
	}

}
