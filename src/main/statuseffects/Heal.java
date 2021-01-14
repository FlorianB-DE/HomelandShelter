package main.statuseffects;

import main.entities.Fightable;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.StatusEffectExpiredException;

public class Heal extends StatusEffect {

	private static final float heal_amount = 10F;

	private static final Texture texture = TextureReader.getTextureByString("FIRE");

	public Heal(Fightable destination, int duration) {
		super(destination, duration, texture);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick() throws StatusEffectExpiredException {
		super.tick();
		getDestination().heal(heal_amount);
	}

}
