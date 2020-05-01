package main.entitiys;

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

	public static final int priority = 1;
	private static final Texture texture =
			TextureReader.getTextureByString("ENEMY");
	private double health = 1;
	private EnemyController con;

	public Enemy(EnemyController con, Tile locatedAt) {
		super(locatedAt, priority, texture);
		this.con = con;
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
}
