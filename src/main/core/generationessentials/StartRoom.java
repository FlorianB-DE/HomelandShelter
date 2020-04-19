package main.core.generationessentials;

import main.core.DungeonGenerator;
import main.entitiys.Character;
import main.entitiys.StairUp;
import main.entitiys.items.Sword;
import main.tiles.Door;
import utils.Direction;
import utils.exceptions.RoomGenerationObstructedException;

/**
 * subclass of Room to add additional functionality in form of the main
 * Character getting added as well as the tileSize_per_Room_entry is not used.
 * Therefore the StartRoom is ALWAYS a size of 3 * 3 Tiles to ensure the
 * Character is created a the center. The StartRoom uses Math.random() * 100 to
 * set its' coordinates randomly between 0, 0 and 99, 99.
 * 
 * @author Florian M. Becker
 */
public class StartRoom extends Room {

	// size = 3, random x and y
	public StartRoom() throws RoomGenerationObstructedException {
		super(3, (int) Math.round((Math.random() * 100)), (int) Math.round((Math.random() * 100)));
	}

	// override to add main character
	@Override
	protected void generateRoom() throws RoomGenerationObstructedException {
		super.generateRoom();
		Character mainChar = new Character(DungeonGenerator.getTileAt(x, y), getLocation());
		DungeonGenerator.setPlayer(mainChar);
		DungeonGenerator.getTileAt(x, y).addContent(mainChar);
		DungeonGenerator.getTileAt(x, y).addContent(new StairUp(DungeonGenerator.getTileAt(x, y), getLocation()));
		
		//TODO REMOVE
		DungeonGenerator.getTileAt(x - 1, y).addContent(new Sword(DungeonGenerator.getTileAt(x - 1, y), x - 1, y));
	}

	// override to allow only a single Door
	@Override
	protected void addDoor(int x, int y, Direction dir) {
		if (getDoors().size() < 1) {
			Door door = new Door(x, y, dir);
			getDoors().add(door);
			DungeonGenerator.setTileAt(door.x, door.y, door);
		}
	}
}