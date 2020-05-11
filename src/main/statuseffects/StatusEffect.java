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

	public void decrementDuration() {
		duration--;
	}

	public Fightable getDestination() {
		return destination;
	}

	public int getDuration() {
		return duration;
	}

	public abstract void tick() throws StatusEffectExpiredException;
}
