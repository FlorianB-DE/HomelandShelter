package main.core.generationessentials;

import main.core.DungeonGenerator;
import main.entities.StairDown;
import main.tiles.Door;
import utils.exceptions.RoomGenerationObstructedException;
import utils.math.Direction;

/**
 * subclass of Room to add additional functionality in form of the Exit of the
 * level getting added as well as the tileSize_per_Room_entry is not used.
 * Therefore the EndRoom is ALWAYS a size of 3 * 3 Tiles to ensure the StairDown
 * is created a the center. The EndRoom uses Math.random() * 100 to set its'
 * coordinates randomly between 0, 0 and 99, 99.
 *
 * @author Florian M. Becker
 */
public final class EndRoom extends Room {

    // size = 3, random x and y
    public EndRoom(DungeonGenerator generator) throws RoomGenerationObstructedException {
        super(3, (int) (Math.random() * 100), (int) (Math.random() * 100), generator);
    }

    // override to allow only a single Door
    @Override
    protected void addDoor(int x, int y, Direction dir) {
        if (getDoors().size() < 1) {
            Door door = new Door(x, y, dir);
            getDoors().add(door);
            generator.setTileAt(door.x, door.y, door);
        }
    }

    // override to add exit stair
    @Override
    protected void generateRoom() throws RoomGenerationObstructedException {
        super.generateRoom();
        generator.getTileAt(x, y).addContent(new StairDown(generator.getTileAt(x, y)));
    }
}
