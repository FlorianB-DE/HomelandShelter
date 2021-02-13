package main.entities;

import main.statuseffects.StatusEffect;
import main.tiles.Tile;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.StatusEffectExpiredException;
import utils.math.MathUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author Floian Mirko Becker
 * @version 2021
 */
public abstract class Creature extends Entity {

    private final List<StatusEffect> effects;
    private double health;
    private boolean facingLeft;
    private int walkingFor;

    // constructor
    public Creature(Tile locatedAt, int priority, Texture texture) {
        super(locatedAt, priority, texture);
        effects = new ArrayList<>();
        facingLeft = false;
        walkingFor = 0;
    }

    /**
     * displays the texture in the original direction
     */
    protected void lookLeft() {
        facingLeft = true;
    }

    /**
     * mirrors the texture vertically
     */
    protected void lookRight() {
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

    @Override
    public Texture getTexture() {
        if (walkingFor == 0)
            return super.getTexture();
        walkingFor--;
        return TextureReader.getTextureByString(super.getTexture().getName() + "_WALKING");
    }

    /**
     * @return players health
     */
    public double getHealth() {
        return health;
    }

    /**
     * @param health the new health
     */
    protected void setHealth(double health) {
        this.health = health;
    }

    @Override
    public void show(Graphics2D g, int x, int y) {
        if (getLocatedAt() == null) return;
        Composite prev = changeOpacity(g);
        int width = getLocatedAt().width;
        if (facingLeft) {
            x += width;
            width *= -1;
        }
        g.drawImage(getTexture().getContent().getImage(), x, y, width, getLocatedAt().height, null);
        g.setComposite(prev);
    }

    public abstract double getMaxHealth();

    /**
     * displays walking animation for 10 frames
     */
    public void startWalking(){
        walkingFor = 10;
    }

    /**
     * adds amount to health
     * @param amount to heal
     */
    public void heal(float amount) {
        if (health + amount >= getMaxHealth())
            health = getMaxHealth();
        else
            health += MathUtils.convertToDouble(amount);
    }

}
