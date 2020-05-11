package main.entitys.items.behavior;

import main.Constants;
import main.entitys.Player;

public final class DualWielding extends Wielding {

	private static final int[] affectedEquipmentSlots = { 1, 2 };

	@Override
	public int[] getAffectedEquipmentSlots() {
		return affectedEquipmentSlots;
	}

	@Override
	public boolean use() {
		final Player mainChar = getMainChar();

		if (isDualWielded()) {
			if (getMainWielding() == this) {
				removeDualWield();
				return false;
			} else {
				if (mainChar.addItem(mainChar.getMainHand()))
					return true;
				return false;
			}
		} else if (mainChar.getMainHand() != null && mainChar.getOffHand() != null) {
			if (mainChar.getInventoryContents().size() < Constants.PLAYER_INVENTORY_SIZE - 1) {
				mainChar.addItem(mainChar.getMainHand());
				mainChar.addItem(mainChar.getOffHand());
				return true;
			}
			return false;
		} else {
			mainChar.addItem(mainChar.getMainHand());
			mainChar.addItem(mainChar.getOffHand());
			return true;
		}
	}
}
