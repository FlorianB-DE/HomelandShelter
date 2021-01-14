package main.entities.items;

import java.util.ArrayList;
import java.util.List;

import main.tiles.Tile;
import utils.exceptions.ItemCreationFailedException;

public final class ItemBlueprint {

	public static final List<ItemBlueprint> items = new ArrayList<>();

	private final List<Attributes<?>> attributes;

	public ItemBlueprint(List<Attributes<?>> attributes) {
		this.attributes = attributes;
		items.add(this);
	}

	public Item instanciate() throws ItemCreationFailedException {
		try {
			return new Item(null, attributes);
		} catch (Exception e) {
			throw new ItemCreationFailedException();
		}
	}

	public Item instanciate(Tile tile) throws ItemCreationFailedException {
		try {
			final Item i = new Item(tile, attributes);
			tile.addContent(i);
			return i;
		} catch (Exception e) {
			throw new ItemCreationFailedException();
		}
	}
}
