package main.statuseffects;

import main.entitys.Fightable;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.StatusEffectExpiredException;

public class Fire extends StatusEffect {

	private static final float damage_per_tick = 1;
	private static final Texture texture = TextureReader.getTextureByString("FIRE");

	public Fire(Fightable destination, int duration) {
		super(destination, duration, texture);
	}

	@Override
	public void tick() throws StatusEffectExpiredException {
		super.tick();
		getDestination().hit(damage_per_tick);
	}

}
