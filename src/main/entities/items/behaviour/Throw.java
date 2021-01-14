package main.entities.items.behaviour;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import main.Constants;
import main.UI.Gameboard;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.entities.items.Item;
import main.tiles.Tile;
import utils.exceptions.NoSuchAttributeException;
import utils.exceptions.PathNotFoundException;

public final class Throw extends Behaviour {

	public Throw(Item owner) {
		super(owner);
	}

	@Override
	public void use() {
		MouseListener[] mouseListeners = Constants.GAME_FRAME.getMouseListeners();
		Gameboard.getCurrentInstance().getPlayer().getInventoryGUI().setVisible(false);
		for (MouseListener mouseListener : mouseListeners) {
			Constants.GAME_FRAME.removeMouseListener(mouseListener);
		}
		Constants.GAME_FRAME.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final Gameboard board = Gameboard.getCurrentInstance();
				try {
					final Tile t = board.getFromDisplayLocation(e.getPoint());
					final PathFinderConfig pfc = new PathFinderConfig();
					board.getPlayer().removeItem(getOwner());
					final List<Point> paths = new LinkedList<>();
					paths.addAll(
							new PathFinder(board.getTilegrid(), pfc).findPath(board.getPlayer().getLocatedAt(), t));
					for (int i = 0; i < paths.size(); i++) {
						Point current = paths.get(i);
						if (!board.getTileAt(current.x, current.y).isWalkable()) {
							current = paths.get(i - 1);
							final Tile newTile = board.getTileAt(current.x, current.y);
							close(mouseListeners, this, newTile);
							return;
						}
					}
					close(mouseListeners, this, t);
				} catch (PathNotFoundException exception) {
				}
				board.getPlayer().getInventoryGUI().setVisible(true);
			}
		});
	}

	private void close(MouseListener[] listeners, MouseListener listener, Tile t) {
		t.addContent(getOwner());
		try {
			t.hit(Item.getAttributeByString(getOwner(), "damage", float.class));
		} catch (NoSuchAttributeException e) {
			// do nothing
		}
		Constants.GAME_FRAME.removeMouseListener(listener);
		for (MouseListener mouseListener : listeners) {
			Constants.GAME_FRAME.addMouseListener(mouseListener);
		}
		Constants.GAME_FRAME.revalidate();
		Constants.GAME_FRAME.repaint();
		Gameboard.getCurrentInstance().getPlayer().getInventoryGUI().setVisible(true);
	}
}
