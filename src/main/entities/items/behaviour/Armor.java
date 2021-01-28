package main.entities.items.behaviour;

import main.entities.Player;
import main.entities.items.Item;

public class Armor extends Wielding {

    private static final int[] affectedEquipmentSlots = {0};

    public Armor(Item owner) {
        super(owner);
    }

    @Override
    public int[] getAffectedEquipmentSlots() {
        return affectedEquipmentSlots;
    }

    @Override
    public void use() {
        removeOwner();
        final Player mainChar = getMainChar();
        if (getArmorWielding() == this) {
            if (mainChar.addItem(mainChar.getArmor()))
                mainChar.setArmor(null);
        } else if (mainChar.addItem(mainChar.getArmor()))
            mainChar.setArmor(getOwner());
    }
}
