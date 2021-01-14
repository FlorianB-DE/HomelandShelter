package main.entities.items;

import main.tiles.Tile;
import utils.exceptions.ItemCreationFailedException;

import java.util.ArrayList;
import java.util.List;

public final class LootTable {

	private final List<ItemBlueprint> loottable;

	public LootTable() {
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
