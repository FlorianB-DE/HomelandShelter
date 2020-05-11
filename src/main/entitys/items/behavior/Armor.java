package main.entitys.items.behavior;

import main.entitys.Player;
import main.entitys.items.Item;

public class Armor extends Wielding {

	public Armor(Item owner) {
		super(owner);
	}

	private static final int[] affectedEquipmentSlots = { 0 };

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
