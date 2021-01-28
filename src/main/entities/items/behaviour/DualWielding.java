package main.entities.items.behaviour;

import main.Constants;
import main.entities.Player;
import main.entities.items.Item;

public final class DualWielding extends Wielding {

    private static final int[] affectedEquipmentSlots = {1, 2};

    public DualWielding(Item owner) {
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

        if (isDualWielded()) {
            if (getMainWielding() == this) {
                removeDualWield();
            } else if (mainChar.addItem(mainChar.getMainHand())) {
                mainChar.setMainHand(getOwner());
                mainChar.setOffHand(getOwner());
            }

        } else if (mainChar.getMainHand() != null && mainChar.getOffHand() != null) {
            if (mainChar.getInventoryContents().size() < Constants.PLAYER_INVENTORY_SIZE - 1) {
                mainChar.addItem(mainChar.getMainHand());
                mainChar.addItem(mainChar.getOffHand());
            }
        } else {
            mainChar.addItem(mainChar.getMainHand());
            mainChar.addItem(mainChar.getOffHand());
            mainChar.setMainHand(getOwner());
            mainChar.setOffHand(getOwner());
        }
    }
}
