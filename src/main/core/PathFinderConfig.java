package main.core;

import main.tiles.Tile;

import java.util.ArrayList;

public class PathFinderConfig {

	private boolean allowed;
	private ArrayList<Class<? extends Tile>> moveTo;

	public PathFinderConfig() {
		moveTo = new ArrayList<>();
	}

	public void addDest(Class<? extends Tile> c) {
		moveTo.add(c);
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

	public void setAllowed() {
		allowed = true;
	}

	public void setDisallowed() {
		allowed = false;
	}
}
