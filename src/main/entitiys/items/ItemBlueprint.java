package main.entitiys.items;

import java.util.ArrayList;
import java.util.List;

import main.tiles.Tile;

@SuppressWarnings("rawtypes")
public class ItemBlueprint {
	
	public static List<ItemBlueprint> items = new ArrayList<>();
	
	private final List<Attributes> attributes;

	public ItemBlueprint(List<Attributes> attributes) {
		this.attributes = attributes;
		items.add(this);
	}
	
	public Item instanciate(Tile tile) {
		return new Item(tile, attributes);
	}
}
