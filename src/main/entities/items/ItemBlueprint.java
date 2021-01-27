package main.entities.items;

import main.tiles.Tile;
import utils.exceptions.ItemCreationFailedException;

import java.util.ArrayList;
import java.util.List;

public final class ItemBlueprint {

	public static final List<ItemBlueprint> items = new ArrayList<>();

	private final List<Attribute<?>> attributes;

	public ItemBlueprint(List<Attribute<?>> attributes) {
		this.attributes = attributes;
		items.add(this);
	}

	public Item instantiate() throws ItemCreationFailedException {
		try {
			return new Item(null, attributes);
		} catch (Exception e) {
			throw new ItemCreationFailedException();
		}
	}

	public Item instantiate(Tile tile) throws ItemCreationFailedException {
		try {
			final Item i = new Item(tile, attributes);
			tile.addContent(i);
			return i;
		} catch (Exception e) {
			throw new ItemCreationFailedException();
		}
	}
}
