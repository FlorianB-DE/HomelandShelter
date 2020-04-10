package main.core;

import main.tiles.Tile;

import java.util.ArrayList;

public class PathFinderConfig {

	private ArrayList<Class<? extends Tile>> moveTo;
	private boolean allowed;

	public PathFinderConfig() {
		moveTo = new ArrayList<>();
	}

	public void addDest(Class<? extends Tile> c) {
		moveTo.add(c);
	}

	public void setAllowed() {
		allowed = true;
	}

	public void setDisallowed() {
		allowed = false;
	}

	public boolean allowedMoveTo(Tile t) {
		if (allowed) {
			if (t == null) {
				return true;
			}
			return moveTo.contains(t.getClass());
		}
		if (t == null) {
			return true;
		}
		// else
		return !moveTo.contains(t.getClass());
	}
}
