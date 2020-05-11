package main.entitys.items.behavior;

import main.entitys.Player;
import main.entitys.items.Item;

public abstract class Wielding extends Behavior {

	public Wielding(Item owner) {
		super(owner);
	}

	public abstract int[] getAffectedEquipmentSlots();

	protected Behavior getArmorWielding() {
		final Item armor = getMainChar().getArmor();
		if (armor != null)
			return armor.getBehavior();
		return null;
	}

	protected Behavior getMainWielding() {
		final Item mainHand = getMainChar().getMainHand();
		if (mainHand != null) {
			return mainHand.getBehavior();
		}
		return null;
	}

	protected Behavior getOffWielding() {
		final Item offHand = getMainChar().getOffHand();
		if (offHand != null) {
			return offHand.getBehavior();
		}
		return null;
	}

	protected boolean isDualWielded() {
		final Item mainHand = getMainChar().getMainHand();
		return mainHand != null ? mainHand.getBehavior() instanceof DualWielding : false;
	}

	protected boolean removeDualWield() {
		final Player mainChar = getMainChar();
		if (mainChar.addItem(mainChar.getMainHand())) {
			mainChar.setMainHand(null);
			mainChar.setOffHand(null);
			return true;
		} else
			System.err.println("inventory full!");
		return false;
	}
	
	protected void removeOwner() {
		getMainChar().removeFromInventory(getOwner());
	}
}
