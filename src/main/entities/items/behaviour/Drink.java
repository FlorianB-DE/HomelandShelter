package main.entities.items.behaviour;

import main.entities.Fightable;
import main.entities.items.Item;
import main.statuseffects.StatusEffect;
import utils.exceptions.NoSuchAttributeException;

public class Drink extends Behaviour {

	public Drink(Item owner) {
		super(owner);
	}

	@Override
	public void use() {
		getMainChar().removeItem(getOwner());
		final String effect;
		int duration;
		try {
			effect = Item.getAttributeByString(getOwner(), "effect", String.class);
		} catch (NoSuchAttributeException e) {
			// potion does nothing
			return;
		}
		try {
			duration = Item.getAttributeByString(getOwner(), "duration", Integer.class);
		} catch (NoSuchAttributeException e) {
			// standard value...
			duration = 1;
		}
		try {
			//noinspection SpellCheckingInspection
			getMainChar().addEffect((StatusEffect) Class.forName("main.statuseffects." + effect).getConstructor(Fightable.class, int.class)
					.newInstance(getMainChar(), duration));
		} catch (Exception ignore) {
		}
	}

}
