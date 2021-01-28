package main.entities.items;

import main.tiles.Tile;
import utils.exceptions.ItemCreationFailedException;

import java.util.ArrayList;
import java.util.List;

public final class ItemBlueprint {

	public static final List<ItemBlueprint> items = new ArrayList<>();

	private final List<Attribute<?>> attributes;
	private final String name;

	public ItemBlueprint(List<Attribute<?>> attributes) {
		this.attributes = attributes;
		final int index = attributes.indexOf(new Attribute<>("name"));
		name = index < 0 ? null : (String) attributes.get(index).getValue();
		items.add(this);
	}

	public ItemBlueprint(String name){
		this.name = name;
		attributes = null;
	}

	@Override
	public boolean equals(Object obj){
		if (obj instanceof ItemBlueprint)
			return name.equals(((ItemBlueprint)obj).name);
		return false;
	}

	public Item instantiate() throws ItemCreationFailedException {
		try {
			return new Item(attributes);
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
