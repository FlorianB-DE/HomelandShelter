package main.entitys.items.behaviour;

import main.entitys.Player;
import main.entitys.items.Item;

public class OffHand extends Wielding {

	public OffHand(Item owner) {
		super(owner);
	}

	private static final int[] affectedEquipmentSlots = { 2 };

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
