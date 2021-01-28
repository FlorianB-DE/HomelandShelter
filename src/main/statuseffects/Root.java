package main.statuseffects;

import main.entities.Fightable;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.CanNotMoveException;
import utils.exceptions.StatusEffectExpiredException;

public class Root extends StatusEffect {

    private static final Texture texture = TextureReader.getTextureByString("FIRE");

    public Root(Fightable destination, int duration) {
        super(destination, duration, texture);
    }

    @Override
    public void tick() throws StatusEffectExpiredException, CanNotMoveException {
        super.tick();
        throw new CanNotMoveException();
    }

}
