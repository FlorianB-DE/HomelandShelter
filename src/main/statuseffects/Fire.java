package main.statuseffects;

import main.entitys.Fightable;
import utils.exceptions.StatusEffectExpiredException;

public class Fire extends StatusEffect {

	private static final float damage_per_tick = 1;

	public Fire(Fightable destination, int duration) {
		super(destination, duration);
	}

	@Override
	public void tick() throws StatusEffectExpiredException {
		if (getDuration() > 0) {
			getDestination().hit(damage_per_tick);
			decrementDuration();
		}
		else throw new StatusEffectExpiredException();
	}

}
