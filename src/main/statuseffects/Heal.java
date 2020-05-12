package main.statuseffects;

import main.entitys.Fightable;
import utils.exceptions.StatusEffectExpiredException;

public class Heal extends StatusEffect {
	
	private static final float heal_amount = 10F;

	public Heal(Fightable destination, int duration) {
		super(destination, duration);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick() throws StatusEffectExpiredException {
		super.tick();
		getDestination().heal(heal_amount);
	}

}
