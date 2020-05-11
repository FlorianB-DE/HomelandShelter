package main.statuseffects;

import utils.exceptions.StatusEffectExpiredException;

public abstract class StatusEffect {
	public abstract void tick() throws StatusEffectExpiredException;
}
