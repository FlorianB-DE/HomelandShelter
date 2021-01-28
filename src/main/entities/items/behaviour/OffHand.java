package main.entities.items.behaviour;

import main.entities.Player;
import main.entities.items.Item;

public class OffHand extends Wielding {

    private static final int[] affectedEquipmentSlots = {2};

    public OffHand(Item owner) {
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
        final Item offHand = mainChar.getOffHand();
        if (getOffWielding() == this) {
            if (mainChar.addItem(offHand))
                mainChar.setOffHand(null);
        } else if (isDualWielded()) {
            removeDualWield();
        } else {
            mainChar.addItem(offHand);
            mainChar.setOffHand(getOwner());
        }
    }

}
