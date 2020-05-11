package main.statuseffects;

import main.entitys.Creature;
import utils.exceptions.StatusEffectExpiredException;

public abstract class StatusEffect {
	private final Creature destination;
	
	public StatusEffect(Creature destination) {
		this.destination = destination;
	}
	
	public abstract void tick() throws StatusEffectExpiredException;

	public Creature getDestination() {
		return destination;
	}
}
