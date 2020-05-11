package main.entitys.items.behavior;

import main.entitys.Player;
import main.entitys.items.Item;

public class OffHand extends Wielding {

	private static final int[] affectedEquipmentSlots = { 2 };

	@Override
	public boolean use() {
		final Player mainChar = getMainChar();
		final Item offHand = mainChar.getOffHand();
		if (getOffWielding() == this) {
			if (mainChar.addItem(offHand)) {
				mainChar.setOffHand(null);
			}
			return false;
		} else if (isDualWielded()) {
			return removeDualWield();
		} else {
			mainChar.addItem(offHand);
			return true;
		}
	}

	@Override
	public int[] getAffectedEquipmentSlots() {
		return affectedEquipmentSlots;
	}

}
