package main.statuseffects;

import main.entitys.Fightable;
import utils.exceptions.StatusEffectExpiredException;

public class Poison extends StatusEffect {

	private static final float damage_per_tick = 1;

	public Poison(Fightable destination, int duration) {
		super(destination, duration);
	}

	@Override
	public void tick() throws StatusEffectExpiredException {
		if (getDuration() > 0) {
			getDestination().trueHit(damage_per_tick);
			decrementDuration();
		}
		else
			throw new StatusEffectExpiredException();
	}

}
