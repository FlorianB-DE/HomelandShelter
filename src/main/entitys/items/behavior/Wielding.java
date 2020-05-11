package main.entitys.items.behavior;

import main.UI.Gameboard;
import main.entitys.Player;
import main.entitys.items.Item;

public abstract class Wielding extends Behavior {
	
	public abstract int[] getAffectedEquipmentSlots();
	
	protected boolean isDualWielded() {
		final Item mainHand = getMainChar().getMainHand();
		return mainHand != null ? ((Equip) mainHand.getBehavior()).getWielding() instanceof DualWielding : false;
	}

	protected Player getMainChar() {
		return Gameboard.getCurrentInstance().getPlayer();
	}

	protected Behavior getArmorWielding() {
		final Item armor = getMainChar().getArmor();
		if (armor != null)
			return ((Equip) armor.getBehavior()).getWielding();
		return null;
	}

	protected Behavior getMainWielding() {
		final Item mainHand = getMainChar().getMainHand();
		if (mainHand != null) {
			return ((Equip) mainHand.getBehavior()).getWielding();
		}
		return null;
	}

	protected Behavior getOffWielding() {
		final Item offHand = getMainChar().getOffHand();
		if (offHand != null) {
			return ((Equip) offHand.getBehavior()).getWielding();
		}
		return null;
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
}
