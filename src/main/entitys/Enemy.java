package main.entitys;

import main.core.EnemyController;
import main.entitys.items.Item;
import main.entitys.items.Loottable;
import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;

/**
 * TODO
 *
 * @author Tim Bauer
 * @version 0.10.0 2020-04-08
 */
public class Enemy extends NonPlayerCharacter implements Fightable {
	// public constants
	public static final int priority = 1;

	// private constants
	private static final Texture[] texture = { TextureReader.getTextureByString("ENEMY") };
	private static Loottable loottable;

	// attributes
	private float armor;
	private final EnemyController con;

	// constructor
	public Enemy(EnemyController con, Tile locatedAt) {
		super(locatedAt, priority, texture[0]);
		this.con = con;
		setHealth(2.0);
		armor = 0.99F;
	}

	@Override
	public float attack() {
		// TODO
		return 0;
	}

	@Override
	public void die() {
		getLocatedAt().removeContent(this);
		con.removeEnemy(this);
		for (Item i : getInventory()) {
			dropItem(i);
		}
	}

	@Override
	public void hit(float damage) {
		setHealth(getHealth() - damage * armor);
		if (getHealth() <= 0) {
			die();
		}
	}

	public void moveEnemy() {
		followPlayer();
	}

	@Override
	public void trueHit(float damage) {
		setHealth(getHealth() - damage);
		if (getHealth() <= 0) {
			die();
		}
	}

	@Override
	protected double getMaxHealth() {
		return 2.0;
	}

	/**
	 * @return the loottable
	 */
	public static Loottable getLoottable() {
		return loottable;
	}

	/**
	 * @param loottable the loottable to set
	 */
	public static void setLoottable(Loottable loottable) {
		Enemy.loottable = loottable;
	}
}
