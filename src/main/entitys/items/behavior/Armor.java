package main.entitys.items.behavior;

import main.entitys.Player;

public class Armor extends Wielding {

	private static final int[] affectedEquipmentSlots = { 0 };

	@Override
	public int[] getAffectedEquipmentSlots() {
		return affectedEquipmentSlots;
	}

	@Override
	public boolean use() {
		final Player mainChar = getMainChar();
		if (getArmorWielding() == this) {
			if (mainChar.addItem(mainChar.getArmor()))
				mainChar.setArmor(null);
			return false;
		} else if (mainChar.addItem(mainChar.getArmor()))
			return true;
		return false;
	}
}
