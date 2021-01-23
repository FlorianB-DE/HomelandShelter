package main.entities.items.behaviour;

import main.Constants;
import main.ui.GameBoard;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.entities.items.Item;
import main.tiles.Tile;
import utils.exceptions.NoSuchAttributeException;
import utils.exceptions.PathNotFoundException;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

public final class Throw extends Behaviour {

	public Throw(Item owner) {
		super(owner);
	}

	@Override
	public void use() {
		MouseListener[] mouseListeners = Constants.GAME_FRAME.getMouseListeners();
		GameBoard.getCurrentInstance().getPlayer().getInventoryGUI().setVisible(false);
		for (MouseListener mouseListener : mouseListeners) {
			Constants.GAME_FRAME.removeMouseListener(mouseListener);
		}
		Constants.GAME_FRAME.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final GameBoard board = GameBoard.getCurrentInstance();
				try {
					final Tile t = board.getFromDisplayLocation(e.getPoint());
					final PathFinderConfig pfc = new PathFinderConfig();
					board.getPlayer().removeItem(getOwner());
					final List<Point> paths = new LinkedList<>(new PathFinder(board.getTileGrid(), pfc).findPath(board.getPlayer().getLocatedAt(), t));
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
				} catch (PathNotFoundException ignored) {
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
		GameBoard.getCurrentInstance().getPlayer().getInventoryGUI().setVisible(true);
	}
}
