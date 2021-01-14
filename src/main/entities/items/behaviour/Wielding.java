package main.entities.items.behaviour;

import main.entities.Player;
import main.entities.items.Item;

public abstract class Wielding extends Behaviour {

	public Wielding(Item owner) {
		super(owner);
	}

	public abstract int[] getAffectedEquipmentSlots();

	protected Behaviour getArmorWielding() {
		final Item armor = getMainChar().getArmor();
		if (armor != null)
			return armor.getBehaviour();
		return null;
	}

	protected Behaviour getMainWielding() {
		final Item mainHand = getMainChar().getMainHand();
		if (mainHand != null) {
			return mainHand.getBehaviour();
		}
		return null;
	}

	protected Behaviour getOffWielding() {
		final Item offHand = getMainChar().getOffHand();
		if (offHand != null) {
			return offHand.getBehaviour();
		}
		return null;
	}

	protected boolean isDualWielded() {
		final Item mainHand = getMainChar().getMainHand();
		return mainHand != null ? mainHand.getBehaviour() instanceof DualWielding : false;
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
