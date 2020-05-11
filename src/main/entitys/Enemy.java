package main.entitys;

import main.core.EnemyController;
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
	// private /*final*/ Loottable loottable;

	// attributes
	private double health;
	private final EnemyController con;

	// constructor
	public Enemy(EnemyController con, Tile locatedAt) {
		super(locatedAt, priority, texture[0]);
		this.con = con;
		health = 2;
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
		// TODO loot table drop
	}

	@Override
	public void hit(float damage) {
		health -= damage;
		if (health <= 0) {
			die();
		}
	}

	public void moveEnemy() {
		followPlayer();
	}

	@Override
	protected double getMaxHealth() {
		return 2.0;
	}
}
