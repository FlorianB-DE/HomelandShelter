package main.entities;

import main.tiles.Tile;

/**
 * TODO
 *
 * @author Florian M. Becker
 * @version 0.1 04.04.2020
 */
public interface Movable {
    static void setDirection(Creature creature, Tile destination){
        if (creature.getLocatedAt().x - destination.x < 0)
            creature.lookLeft();
        else if (creature.getLocatedAt().x - destination.x > 0)
            creature.lookRight();
    }
    void move(Tile destination);
}
