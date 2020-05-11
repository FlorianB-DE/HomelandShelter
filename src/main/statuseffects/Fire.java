package main.statuseffects;

import main.UI.Gameboard;
import main.entitys.Creature;
import utils.exceptions.StatusEffectExpiredException;

public class Fire extends StatusEffect {

	private static final float damage_per_tick = 1;
	private int duration;

	public Fire(Creature destination, int duration) {
		super(destination);
		this.duration = duration;
	}

	@Override
	public void tick() throws StatusEffectExpiredException {
		if (duration > 0)
			Gameboard.getCurrentInstance().getPlayer().hit(damage_per_tick);
		else throw new StatusEffectExpiredException();
	}

}
