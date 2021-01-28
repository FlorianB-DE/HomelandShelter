package main.entities;

import main.statuseffects.StatusEffect;
import main.tiles.Tile;
import textures.Texture;
import utils.exceptions.StatusEffectExpiredException;
import utils.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Creature extends Entity {

	private final List<StatusEffect> effects;
	private double health;

	// constructor
	public Creature(Tile locatedAt, int priority, Texture texture) {
		super(locatedAt, priority, texture);
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
	 * @return players health
	 */
	public double getHealth() {
		return health;
	}

	public abstract double getMaxHealth();

	/**
	 */
	public void heal(float amount) {
		if (health + amount >= getMaxHealth())
			health = getMaxHealth();
		else
			health += MathUtils.convertToDouble(amount);
	}

	protected void setHealth(double health) {
		this.health = health;
	}

}
