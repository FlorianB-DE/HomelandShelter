package main.entities.items;

import main.tiles.Tile;
import utils.exceptions.ItemCreationFailedException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public final class LootTable {

    private static final Map<String, LootTable> lootTables = new Hashtable<>();
    private final Map<ItemBlueprint, Float> lootTable;
    private final boolean writeable;

    public LootTable(Map<ItemBlueprint, Float> itemBlueprints, String className) {
        lootTable = new Hashtable<>(itemBlueprints);
        writeable = false;
        lootTables.put(className, this);
    }

    public LootTable(String className) {
        lootTable = new Hashtable<>();
        writeable = true;
        lootTables.put(className, this);
    }

    public static LootTable getLootTable(String className) {
        return lootTables.get(className);
    }

    public Item createRandomItem() throws ItemCreationFailedException {
        return createRandomItem(null);
    }

    public Item createRandomItem(Tile at) throws ItemCreationFailedException {
        final List<ItemBlueprint> itemBlueprints = new ArrayList<>(lootTable.keySet());
        while (itemBlueprints.size() != 0) {
            final int index = (int) Math.round(Math.random() * (itemBlueprints.size() - 1));
            if (feelingLucky(itemBlueprints, index))
                if (at == null)
                    return itemBlueprints.get(index).instantiate();
                else
                    return itemBlueprints.get(index).instantiate(at);
            else
                itemBlueprints.remove(index);
        }
        throw new ItemCreationFailedException("you got unlucky!");
    }

    public void addBlueprint(ItemBlueprint blueprint, float chance) {
        if (writeable)
            lootTable.put(blueprint, chance);
        else
            throw new IllegalStateException("Loot table not editable");
    }

    private boolean feelingLucky(List<ItemBlueprint> blueprints, int index) {
        return lootTable.get(blueprints.get(index)) >= (Math.random() * 100);
    }
}
