package main.entities.items.behaviour;

import main.entities.Player;
import main.entities.items.Item;

public final class MainHand extends Wielding {

    private static final int[] affectedEquipmentSlots = {1};

    public MainHand(Item owner) {
        super(owner);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int[] getAffectedEquipmentSlots() {
        return affectedEquipmentSlots;
    }

    @Override
    public void use() {
        removeOwner();
        final Player mainChar = getMainChar();
        final Item mainHand = mainChar.getMainHand();
        if (getMainWielding() == this) {
            if (mainChar.addItem(mainHand))
                mainChar.setMainHand(null);
        } else if (isDualWielded()) {
            removeDualWield();
        } else {
            mainChar.addItem(mainHand);
            mainChar.setMainHand(getOwner());
        }
    }
}
