package main.entitiys;

import main.core.EnemyController;
import main.core.NeighbourFinder;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;

/**
 * TODO
 *
 * @author Tim Bauer
 * @version 0.10.0 2020-04-08
 */
public class Enemy extends Entity implements Movement {

	public static final int priority = 1;
	private static final Texture texture = TextureReader.getTextureByString("ENEMY");
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
	public void hit(float damage) {
		health -= damage;
		if (health <= 0) {
			die();
		}
	}

	@Override
	public void move(Tile destination) {
//		getLocatedAt().removeContent(this);
//		destination.addContent(this);
//		for (Entity e : destination.getContents()) {
//			if (e instanceof Character) {
//				System.out.println("Got ya");
//			}
//		}
	}

	public void moveEnemy() {
		Tile[] n = NeighbourFinder.findNeighbours(x, y);
		for (int i = 0; i < 10; i++) {
			Tile tile = n[(int) ((Math.random() * 100) % 4)];
			if (tile instanceof RoomFloor) {
				move(tile);
				return;
			}
		}
	}

	@Override
	public void die() {
		getLocatedAt().removeContent(this);
		con.removeEnemy(this);
	}
}
