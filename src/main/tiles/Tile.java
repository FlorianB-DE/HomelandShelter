package main.tiles;

import main.Constants;
import main.entities.Entity;
import main.entities.Fightable;
import main.entities.Movable;
import textures.Texture;
import utils.math.Fractions;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO
 *
 * @author Florian M. Becker
 * @version 1.0 04.04.2020
 */
public abstract class Tile extends Rectangle {

    private float alpha;
    private List<Entity> content;
    private Texture i;

    public Tile(int x, int y, int size, Texture texture) {
        this(new Point(x, y), size, texture);
    }

    public Tile(int x, int y, Texture texture) {
        this(x, y, 0, texture);
    }

    public Tile(Point p, int size, Texture texture) {
        super(p, new Dimension(size, size));
        i = texture;
    }

    /**
     * @param content the content to set
     */
    public void addContent(Entity content) {
        if (content == null)
            return;
        content.setLocatedAt(this);
        if (this.content == null) {
            this.content = new ArrayList<>();
        }
        this.content.add(content);
    }

    /**
     * @return the alpha state of the square which is drawn over a Tile to simulate
     * shadow
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * @return the content
     */
    public Entity getContent(int at) {
        if (content == null) {
            return null;
        }
        return content.get(at);
    }

    /**
     * @return a COPY of the "contents" List<Entity>
     */
    public List<Entity> getContents() {
        if (content == null) {
            return null;
        }
        return new ArrayList<>(content);
    }

    public <T extends Entity> List<T> getContentsOfType(Class<T> type) {
        if (content == null)
            return null;
        List<T> list = new ArrayList<>();
        for (Entity entity : content) {
            if (type.isInstance(entity)) {
                list.add(type.cast(entity));
            }
        }
        return list;
    }

    public List<Entity> getFightable() {
        if (content == null)
            return null;
        List<Entity> list = new ArrayList<>();
        for (Entity entity : content)
            if (entity instanceof Fightable)
                list.add(entity);
        return list;
    }

    public <T extends Entity> T getFirstContentOfType(Class<T> type) {
        if (content == null)
            return null;
        for (Entity entity : content)
            if (type.isInstance(entity))
                return type.cast(entity);
        return null;
    }

    public List<Entity> getMovables() {
        if (content == null)
            return null;
        List<Entity> list = new ArrayList<>();
        for (Entity entity : content)
            if (entity instanceof Movable)
                list.add(entity);
        return list;
    }

    public boolean hasContentOfType(Class<? extends Entity> type) {
        // has no content
        if (content == null)
            return false;

        // iterate content
        for (Entity entity : content)
            if (entity != null)
                if (type.isInstance(entity))
                    return true;

        // nothing found
        return false;
    }

    public boolean hasHittableContent(Entity except) {
        if (content == null)
            return false;
        for (Entity e : content)
            if (e instanceof Fightable && e != except)
                return true;
        return false;
    }

    public boolean hasHittableContent(List<Entity> exceptions) {
        if (content == null)
            return false;
        for (Entity e : content)
            if (e instanceof Fightable && !exceptions.contains(e))
                return true;
        return false;
    }

    public boolean hasMovableContent() {
        if (content == null)
            return false;
        for (Entity e : content)
            if (e instanceof Movable)
                return true;
        return false;
    }

    public void hit(float damage) {
        if (content == null)
            return;
        for (int i = 0; i < content.size(); i++) {
            Entity e = content.get(i);
            if (e instanceof Fightable)
                ((Fightable) e).hit(damage);
            // entities can delete themselves out of the content list if they die therefore the check
            if (content == null)
                break;
        }
    }

    public abstract boolean isWalkable();

    public void removeContent(Entity content) {
        this.content.removeIf(e -> e.compareTo(content) == 0);
        if (this.content.isEmpty())
            this.content = null;
    }

    public void setTexture(Texture texture) {
        i = texture;
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public void show(Graphics2D g, int x, int y) {
        final float divider = 2.4F;
        final int centerX = x + width / 2;
        final int centerY = y + height / 2;
        final int frameWidth = Constants.GAME_FRAME.getWidth();
        final int frameHeight = Constants.GAME_FRAME.getHeight();
        double sqDist = Point.distanceSq(centerX, centerY, frameWidth / 2, frameHeight / 2);
        for (Fractions fraction : Fractions.values()) {
            if (sqDist >= Math.pow((frameWidth / divider), 2) * fraction.val
                    + Math.pow((frameHeight / divider), 2) * fraction.val) {
                alpha = fraction.val;
                break;
            }
        }

        g.drawImage(i.getContent().getImage(), x, y, width, height, null);

        g.setColor(new Color(0F, 0F, 0F, alpha));
        g.fillRect(x, y, width, height);

        if (content != null) {
            Collections.sort(content);
            showContent(g, x, y);
        }
    }

    protected void showContent(Graphics2D g, int x, int y) {
        if (getContents() != null)
            for (Entity entity : getContents())
                entity.show(g, x, y);
    }
}
