package main.entitiys;

import java.awt.Point;
import main.Constants;
import main.UI.Gameboard;
import main.core.DungeonGenerator;
import main.core.EnemyController;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.tiles.Tile;
import main.tiles.Wall;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.PathNotFoundException;

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

	@Override
	public void move(Tile destination) {
		Character c = Gameboard.getCurrentInstance().getPlayer();
		if (c.getLocatedAt().getLocation().equals(destination.getLocation()))
			c.hit(attack());
		else {
			getLocatedAt().removeContent(this);
			destination.addContent(this);
		}
	}

	public void moveEnemy() {

		// Tims' code
//		Tile[] n = NeighbourFinder.findNeighbours(x, y);
//		for (int i = 0; i < 10; i++) {
//			Tile tile = n[(int) ((Math.random() * 100) % 4)];
//			if (tile instanceof RoomFloor) {
//				move(tile);
//				return;
//			}
//		}

		// final code???

		// if character is inside enemys field of view
		if (DungeonGenerator.getPlayer().distance(x, y) <= Constants.RENDER_DISTANCE / 2) {

			// pathfind towards player
			PathFinderConfig pfc = new PathFinderConfig();
			pfc.setDisallowed();
			pfc.addDest(Wall.class);
			try {
				// find path
				Point path = new PathFinder(Gameboard.getCurrentInstance().getTilegrid(), pfc) // new Pathfinder
						.findPath(getLocatedAt(), // starting point
								Gameboard.getCurrentInstance().getPlayer().getLocatedAt()) // destination
						.getFirst(); // retrieve point
				move(DungeonGenerator.getTileAt(path.x, path.y));
			} catch (PathNotFoundException e) {
				// do nothing
			}

		}
	}

	@Override
	public void die() {
		getLocatedAt().removeContent(this);
		con.removeEnemy(this);
	}
}
