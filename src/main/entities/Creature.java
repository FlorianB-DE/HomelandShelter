package main.entities;

import main.statuseffects.StatusEffect;
import main.tiles.Tile;
import textures.Texture;
import utils.exceptions.StatusEffectExpiredException;
import utils.math.MathUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Creature extends Entity {

    private final List<StatusEffect> effects;
    private double health;
    private boolean facingLeft;

    // constructor
    public Creature(Tile locatedAt, int priority, Texture texture) {
        super(locatedAt, priority, texture);
        effects = new ArrayList<>();
        facingLeft = false;
    }

    protected void lookLeft(){
        facingLeft = true;
    }

    protected void lookRight(){
        facingLeft = false;
    }

    /**
     * @param e adds "e" to effects List
     */
    public void addEffect(StatusEffect e) {
        effects.add(e);
    }

    /**
     * triggers all effects' tick methods inside effects List
     */
    protected void doEffectTicks() {
        for (int i = 0; i < effects.size(); i++) {
            try {
                effects.get(i).tick();
            } catch (StatusEffectExpiredException e) {
                effects.remove(i);
            }
        }
    }

    /**
     * @return players health
     */
    public double getHealth() {
        return health;
    }

    protected void setHealth(double health) {
        this.health = health;
    }

    @Override
    public void show(Graphics2D g, int x, int y) {
        if (getLocatedAt() == null) return;
        Composite prev = changeOpacity(g);
        int width = getLocatedAt().width;
        if (facingLeft){
            x += width;
            width *= -1;
        }
        g.drawImage(getTexture().getContent().getImage(), x, y, width, getLocatedAt().height, null);
        g.setComposite(prev);
    }

    public abstract double getMaxHealth();

    /**
     *
     */
    public void heal(float amount) {
        if (health + amount >= getMaxHealth())
            health = getMaxHealth();
        else
            health += MathUtils.convertToDouble(amount);
    }

}
