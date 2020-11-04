package main.entitys.items;

import java.util.ArrayList;
import java.util.List;

import main.tiles.Tile;
import utils.exceptions.ItemCreationFailedException;

public final class Loottable {

	private final List<ItemBlueprint> loottable;

	public Loottable() {
		loottable = new ArrayList<>();
		loottable.add(ItemBlueprint.items.get(4));
	}

	public Item createRandomItem() throws ItemCreationFailedException {
		try {
			return loottable.get((int) Math.round(Math.random() * (loottable.size() - 1))).instanciate();
		} catch (IndexOutOfBoundsException e) {
			throw new ItemCreationFailedException();
		}
	}

	public Item createRandomItem(Tile at) throws ItemCreationFailedException {
		try {
			return loottable.get((int) Math.round(Math.random() * (loottable.size() - 1))).instanciate(at);
		} catch (IndexOutOfBoundsException e) {
			throw new ItemCreationFailedException();
		}
	}
}
