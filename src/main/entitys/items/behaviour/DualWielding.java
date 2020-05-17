package main.entitys.items.behaviour;

import main.Constants;
import main.entitys.Player;
import main.entitys.items.Item;

public final class DualWielding extends Wielding {

	public DualWielding(Item owner) {
		super(owner);
	}

	private static final int[] affectedEquipmentSlots = { 1, 2 };

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
