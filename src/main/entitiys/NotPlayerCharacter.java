package main.entitiys;

import main.Constants;
import main.UI.Gameboard;
import main.core.DungeonGenerator;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.tiles.Tile;
import main.tiles.Wall;
import textures.Texture;
import utils.exceptions.PathNotFoundException;

import java.awt.Point;

public abstract class NotPlayerCharacter extends Entity implements Movement {

	public NotPlayerCharacter(Tile locatedAt, int priority, Texture texture) {
		super(locatedAt, priority, texture);
	}

	public void followPlayer() {
		// if character is inside enemys field of view
		if (DungeonGenerator.getPlayer().distance(x, y) <=
			Constants.RENDER_DISTANCE / 2) {

			// pathfind towards player
			PathFinderConfig pfc = new PathFinderConfig();
			pfc.setDisallowed();
			pfc.addDest(Wall.class);
			try {
				// find path
				Point path = new PathFinder(
						Gameboard.getCurrentInstance().getTilegrid(),
						pfc) // new Pathfinder
						.findPath(getLocatedAt(), // starting point
								  Gameboard.getCurrentInstance().getPlayer()
										  .getLocatedAt()) // destination
						.getFirst(); // retrieve point
				move(DungeonGenerator.getTileAt(path.x, path.y));
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
