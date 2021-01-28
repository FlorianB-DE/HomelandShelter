package main.entities;

import main.core.EnemyController;
import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;

/**
 * TODO
 *
 * @author Tim Bauer
 * @version 0.10.0 2020-04-08
 */
public class Enemy extends NonPlayerCharacter implements Fightable, ILootable {
    // public constants
    public static final int priority = 1;

    // private constants
    private static final Texture[] texture = {TextureReader.getTextureByString("ENEMY")};
    private final EnemyController con;
    // attributes
    private final float armor;

    // constructor
    public Enemy(EnemyController con, Tile locatedAt) {
        super(locatedAt, priority, texture[0]);
        this.con = con;
        setHealth(2.0);
        armor = 0.99F;
    }

    @Override
    public float attack() {
        // TODO
        return 1;
    }

    @Override
    public void die() {
        getLocatedAt().removeContent(this);
        con.removeEnemy(this);
        try {
            dropItem(createFromLootTable());
        } catch (IllegalAccessException ignore) {
        }
    }

    @Override
    public void hit(float damage) {
        setHealth(getHealth() - damage * armor);
        if (getHealth() <= 0) {
            die();
        }
    }

    public void moveEnemy() {
        followPlayer();
    }

    @Override
    public void trueHit(float damage) {
        setHealth(getHealth() - damage);
        if (getHealth() <= 0) {
            die();
        }
    }

    @Override
    public double getMaxHealth() {
        return 2.0;
    }
}
