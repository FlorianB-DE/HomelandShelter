package main.entities;

import main.entities.items.Item;
import main.entities.items.LootTable;
import utils.exceptions.ItemCreationFailedException;

public interface ILootable {
    default Item createFromLootTable(){
        try {
            return LootTable.getLootTable(getClass().getSimpleName()).createRandomItem();
        }
        catch (ItemCreationFailedException itemCreationFailedException){
            return null;
        }
        catch (NullPointerException nullPointerException){
            return null;
        }
    }

    default void dropItem(Item item) throws IllegalAccessException {
        if (this instanceof Entity)
            ((Entity) this).getLocatedAt().addContent(item);
        else throw new IllegalAccessException();
    }
}
