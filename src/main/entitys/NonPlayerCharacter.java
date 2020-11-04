package main.entitys;

import main.Constants;
import main.UI.Gameboard;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.tiles.Tile;
import main.tiles.Wall;
import textures.Texture;
import utils.exceptions.PathNotFoundException;

import java.awt.Point;
import java.util.concurrent.BlockingQueue;

public abstract class NonPlayerCharacter extends Creature implements Moveable {

	public NonPlayerCharacter(Tile locatedAt, int priority, Texture texture) {
		super(locatedAt, priority, texture);
	}

	public void followPlayer() {
		// if character is inside enemys field of view
		if (Gameboard.getCurrentInstance().getPlayer().distance(x, y) <=
			Constants.RENDER_DISTANCE / 2) {

			// pathfind towards player
			PathFinderConfig pfc = new PathFinderConfig();
			pfc.setDisallowed();
			pfc.addDest(Wall.class);
			try {
				// find path
				BlockingQueue<Point> path = new PathFinder(
						Gameboard.getCurrentInstance().getTilegrid(),
						pfc) // new Pathfinder
						.findPath(getLocatedAt(), // starting point
								  Gameboard.getCurrentInstance().getPlayer()
										  .getLocatedAt());
				// First note is current location --> drop first
				path.remove();
				Point next = path.remove();
				move(Gameboard.getCurrentInstance()
							 .getTilegrid()[next.x][next.y]);
			} catch (PathNotFoundException e) {
				// do nothing
			}
		}
	}

	@Override
	public void move(Tile destination) {
		if (this instanceof Fightable && destination.hasHitableContent(this)) {
			destination.hit(((Fightable) this).attack());
		} else {
			getLocatedAt().removeContent(this);
			destination.addContent(this);
		}
	}
}
