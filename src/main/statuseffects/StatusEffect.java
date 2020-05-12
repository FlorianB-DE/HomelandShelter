package main.statuseffects;

import main.entitys.Fightable;
import utils.exceptions.StatusEffectExpiredException;

public abstract class StatusEffect {
	private final Fightable destination;
	private int duration;

	public StatusEffect(Fightable destination, int duration) {
		this.destination = destination;
		this.duration = duration;
	}

	public void tick() throws StatusEffectExpiredException {
		if (duration <= 0)
			throw new StatusEffectExpiredException();
		else
			duration--;
	}
	
	protected Fightable getDestination() {
		return destination;
	}
}
