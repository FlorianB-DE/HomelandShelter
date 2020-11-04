package main.core.generationessentials;

import main.Constants;
import main.core.DungeonGenerator;
import main.tiles.Door;
import main.tiles.RoomFloor;
import utils.exceptions.RoomGenerationObstructedException;
import utils.math.Direction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * class for handling the generation of a Room inside 'tiles' array provided by
 * DungeonGenerator every Room has a rectangular shape and at least one Door
 * (two at max) creating a new instance of this class @throws a
 * RoomGenerationObstructedException and reverts its' changes in the 'tiles'
 * array
 *
 * @author Florian M. Becker
 */
public class Room extends Point {
	protected final DungeonGenerator generator;
	private final List<Door> doors;
	private final int sizeX, sizeY;

	public Room(int size, int x, int y, DungeonGenerator generator) throws RoomGenerationObstructedException {
		this(size, size, x, y, generator);
	}

	public Room(int sizeX, int sizeY, int x, int y, DungeonGenerator generator) throws RoomGenerationObstructedException {
		super(x, y);
		this.sizeX = sizeX / 2;
		this.sizeY = sizeY / 2;
		this.generator = generator;
		doors = new ArrayList<Door>();
		generateRoom();
	}

	/**
	 * @return the first instance of Door in 'doors' List used by the PathFinder to
	 *         path TO
	 * @ATTENTION does NOT remove the Door
	 */
	public Door getEntrance() {
		if (!doors.isEmpty()) {
			return doors.get(0);
		}
		throw new NullPointerException("No Doors avaiable");
	}

	/**
	 * @return the first instance of Door in 'doors' List. Exit is used by the
	 *         PathFinder to path FROM
	 * @ATTENTION does NOT remove the Door
	 */
	public Door getExit() {
		if (!doors.isEmpty()) {
			if (doors.size() > 1) {
				return doors.get(1);
			}

			// else
			return doors.get(0);
		}
		// else
		throw new NullPointerException("No Doors avaiable");
	}
//	work in progress
//	public void overhaulDoorPositions() {
//		for (int i = -sizeX; i <= sizeX; i++) {
//			
//		}
//	}


	/**
	 * TODO
	 *
	 * @param x
	 * @param y
	 * @param dir
	 */
	protected void addDoor(int x, int y, Direction dir) {
		if (doors.size() < 2) {
			final Door door = new Door(x, y, dir);
			doors.add(door);
			generator.setTileAt(door.x, door.y, door);
		}
	}

	protected void generateRoom() throws RoomGenerationObstructedException {
		// looping from negative half size to positive half size ensures that the x and
		// y coordinates are in the center
		for (int i = -sizeX; i <= sizeX; i++)
			for (int j = -sizeY; j <= sizeY; j++)

				// checks weather the coordinates are inside the 'tiles' array
				if (x + i < Constants.DUNGEON_SIZE && y + j < Constants.DUNGEON_SIZE && x + i >= 0 && y + j >= 0) {

					// only generates a room if the array is empty at the specified coordinates
					if (generator.getTileAt(x + i, y + j) == null) {

						// occupies a tile
						generator.setTileAt(x + i, y + j, new RoomFloor(x + i, y + j));

						// if the current loop is at a border of a room this set of conditions try to
						// generate a door

						// true when iterator is at the most left edge without the most upper/lower case
						if (i == -sizeX && !(j == -sizeY) && !(j == sizeY))
							try {
								attemptDoorCreation(i + x - 1, j + y, Direction.vertical);
							} catch (ArrayIndexOutOfBoundsException e) {
								// could not create Door
							}
						// true when iterator is at the most right edge without the most upper/lower
						// case
						else if (i == sizeX && !(j == -sizeY) && !(j == sizeY))
							try {
								attemptDoorCreation(i + x + 1, j + y, Direction.vertical);
							} catch (ArrayIndexOutOfBoundsException e) {
								// could not create Door
							}
						// true when iterator is at the most upper edge without the most left/right case
						else if (j == -sizeY && !(i == -sizeX) && !(i == sizeX))
							try {
								attemptDoorCreation(i + x, j + y - 1, Direction.horizontal);
							} catch (ArrayIndexOutOfBoundsException e) {
								// could not create Door
							}
						// true when iterator is at the most lower edge without the most left/right case
						else if (j == sizeY && !(i == -sizeX) && !(i == sizeX))
							try {
								attemptDoorCreation(i + x, j + y + 1, Direction.horizontal);
							} catch (ArrayIndexOutOfBoundsException e) {
								// could not create Door
							}
						// true when both iterators are on their peak value. Checks weather the room has
						// a door or not.
						// If it doesn't, a door will be created by force. If it fails, it cancels the
						// generation and reverts all changes to the array
						else if (i == sizeX && j == sizeY && doors.size() == 0)
							try {
								addDoor(x + i - 1, y + j + 1, Direction.horizontal);
							} catch (ArrayIndexOutOfBoundsException e) {
								decompose(i, j);
								throw new RoomGenerationObstructedException("ArrayIndexOutOfBounds");
							}
					} else {
						decompose(i, j);
						throw new RoomGenerationObstructedException();
					}
				} else
					throw new RoomGenerationObstructedException();
	}

	protected List<Door> getDoors() {
		return doors;
	}

	/**
	 * @param x the x-coordinate the door shall be created
	 * @param y the y-coordinate the door shall be created with a chance of 5%:
	 *          creates a door at x, y
	 */
	private void attemptDoorCreation(int x, int y, Direction dir) {
		if (Math.random() < 0.05) {
			addDoor(x, y, dir);
		}
	}

	/**
	 * @param i the current state of the first dimensions iterator from where to
	 *          decompose
	 * @param j the current state of the second dimensions iterator from where to
	 *          decompose sets every tile from i, j to negative half the sizes (both
	 *          values included) to null
	 */
	private void decompose(int i, int j) {
		// iterate every changed position
		for (int k = i; k >= -sizeX; k--) {
			for (int h = j; h >= -sizeY; h--) {
				try {
					// deleting tile
					generator.setTileAt(x + k, x + h, null);
				} catch (ArrayIndexOutOfBoundsException e) {
					// do nothing
				}
			}
		}
		// removing doors
		removeDoors();
	}

	/**
	 * removes ALL Doors from 'doors' List
	 */
	private void removeDoors() {
		for (Door door : doors) {
			try {
				// removing door
				generator.setTileAt(door.x, door.y, null);
			} catch (ArrayIndexOutOfBoundsException e) {
				// nothing happens
			}
		}
	}
}