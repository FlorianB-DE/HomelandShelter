package main.entities.items;

import main.tiles.Tile;
import utils.exceptions.ItemCreationFailedException;

import java.util.ArrayList;
import java.util.List;

public final class LootTable {

	private final List<ItemBlueprint> lootTable;

	public LootTable() {
		lootTable = new ArrayList<>();
		lootTable.add(ItemBlueprint.items.get(4));
	}

	public Item createRandomItem() throws ItemCreationFailedException {
		try {
			return lootTable.get((int) Math.round(Math.random() * (lootTable.size() - 1))).instantiate();
		} catch (IndexOutOfBoundsException e) {
			throw new ItemCreationFailedException();
		}
	}

	public Item createRandomItem(Tile at) throws ItemCreationFailedException {
		try {
			return lootTable.get((int) Math.round(Math.random() * (lootTable.size() - 1))).instantiate(at);
		} catch (IndexOutOfBoundsException e) {
			throw new ItemCreationFailedException();
		}
	}
}
