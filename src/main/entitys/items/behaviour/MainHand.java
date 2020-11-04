package main.entitys.items.behaviour;

import main.entitys.Player;
import main.entitys.items.Item;

public final class MainHand extends Wielding {

	public MainHand(Item owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	private static final int[] affectedEquipmentSlots = { 1 };

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
