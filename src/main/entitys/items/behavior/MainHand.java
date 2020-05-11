package main.entitys.items.behavior;

import main.entitys.Player;
import main.entitys.items.Item;

public final class MainHand extends Wielding {
	
	private static final int[] affectedEquipmentSlots = { 1 };
	
	@Override
	public int[] getAffectedEquipmentSlots() {
		return affectedEquipmentSlots;
	}

	@Override
	public boolean use() {
		final Player mainChar = getMainChar();
		final Item mainHand = mainChar.getMainHand();
		if (getMainWielding() == this) {
			if (mainChar.addItem(mainHand)) {
				mainChar.setMainHand(null);
			}
			return false;
		} else if (isDualWielded()) {
			return removeDualWield();
		} else {
			mainChar.addItem(mainHand);
			return true;
		}
	}
}
