package main.core.generationessentials;

import main.core.DungeonGenerator;
import main.entitys.Grass;
import main.entitys.Player;
import main.entitys.StairUp;
import main.entitys.items.ItemBlueprint;
import main.tiles.Door;
import utils.math.Direction;
import utils.exceptions.RoomGenerationObstructedException;

/**
 * subclass of Room to add additional functionality in form of the main Player
 * getting added as well as the tileSize_per_Room_entry is not used. Therefore
 * the StartRoom is ALWAYS a size of 3 * 3 Tiles to ensure the Player is created
 * a the center. The StartRoom uses Math.random() * 100 to set its' coordinates
 * randomly between 0, 0 and 99, 99.
 *
 * @author Florian M. Becker
 */
public final class StartRoom extends Room {

	// size = 3, random x and y
	public StartRoom(DungeonGenerator generator) throws RoomGenerationObstructedException {
		super(3, (int) Math.round((Math.random() * 100)), (int) Math.round((Math.random() * 100)), generator);
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

	// override to add main character
	@Override
	protected void generateRoom() throws RoomGenerationObstructedException {
		super.generateRoom();
		Player mainChar = new Player(generator.getTileAt(x, y));
		generator.setPlayer(mainChar);
		generator.getTileAt(x, y).addContent(mainChar);
		generator.getTileAt(x, y).addContent(new StairUp(generator.getTileAt(x, y)));
		generator.getTileAt(x + 1, y).addContent(new Grass(generator.getTileAt(x + 1, y)));
		try {
			ItemBlueprint.items.get(0).instanciate(generator.getTileAt(x - 1, y));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("item creation failed");
		}
	}

}