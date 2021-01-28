package main.entities;

import main.Constants;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.tiles.Tile;
import main.tiles.Wall;
import main.ui.GameBoard;
import textures.Texture;
import utils.exceptions.PathNotFoundException;

import java.awt.*;
import java.util.concurrent.BlockingQueue;

public abstract class NonPlayerCharacter extends Creature implements Movable {

	public NonPlayerCharacter(Tile locatedAt, int priority, Texture texture) {
		super(locatedAt, priority, texture);
	}

	public void followPlayer() {
		// if character is inside enemies field of view
		if (GameBoard.getCurrentInstance().getPlayer().distance(x, y) <=
			Constants.RENDER_DISTANCE / 2) {

			// path find towards player
			PathFinderConfig pfc = new PathFinderConfig();
			pfc.setDisallowed();
			pfc.addDest(Wall.class);
			try {
				// find path
				BlockingQueue<Point> path = new PathFinder(
						GameBoard.getCurrentInstance().getTileGrid(),
						pfc) // new Pathfinder
						.findPath(getLocatedAt(), // starting point
								  GameBoard.getCurrentInstance().getPlayer()
										  .getLocatedAt());
				// First note is current location --> drop first
				path.remove();
				Point next = path.remove();
				move(GameBoard.getCurrentInstance()
							 .getTileGrid()[next.x][next.y]);
			} catch (PathNotFoundException e) {
				// do nothing
			}
		}
	}

	@Override
	public void move(Tile destination) {
		if (this instanceof Fightable && destination.hasHittableContent(this)) {
			destination.hit(((Fightable) this).attack());
		} else {
			getLocatedAt().removeContent(this);
			destination.addContent(this);
		}
	}
}
