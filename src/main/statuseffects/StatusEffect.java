package main.statuseffects;

import main.entities.Entity;
import main.entities.Fightable;
import main.tiles.Tile;
import textures.Texture;
import utils.exceptions.StatusEffectExpiredException;

import java.awt.*;

public abstract class StatusEffect {
	private final Texture texture;
	private final Fightable destination;
	private int duration;

	public StatusEffect(Fightable destination, int duration, Texture texture) {
		this.destination = destination;
		this.duration = duration;
		this.texture = texture;
	}

	public void show(Graphics2D g) {
		final Tile dest = ((Entity) destination).getLocatedAt();
		g.drawImage(texture.getContent().getImage(), dest.x, dest.y, dest.width, dest.height, null);
	}

	/**
	 * decrements duration by one
	 * @throws StatusEffectExpiredException if effect is expired
	 */
	public void tick() throws StatusEffectExpiredException {
		if (duration <= 0)
			throw new StatusEffectExpiredException();
		else
			duration--;
	}

	/**
	 * @return the Fightable Entity
	 */
	protected Fightable getDestination() {
		return destination;
	}
}
