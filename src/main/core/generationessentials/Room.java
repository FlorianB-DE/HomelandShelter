package main.core.generationessentials;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.core.DungeonGenerator;
import main.tiles.Door;
import main.tiles.RoomFloor;
import utils.Direction;
import utils.exceptions.RoomGenerationObstructedException;

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
	int sizeX, sizeY;
	private List<Door> doors;

	public Room(int size, int x, int y) throws RoomGenerationObstructedException {
		this(size, size, x, y);
	}

	public Room(int sizeX, int sizeY, int x, int y) throws RoomGenerationObstructedException {
		super(x, y);
		this.sizeX = sizeX / 2;
		this.sizeY = sizeY / 2;
		doors = new ArrayList<Door>();
		generateRoom();
	}

	protected void generateRoom() throws RoomGenerationObstructedException {
		// looping from negative half size to positive half size ensures that the x and
		// y coordinates are in the center
		for (int i = -sizeX; i <= sizeX; i++)
			for (int j = -sizeY; j <= sizeY; j++)

				// checks weather the coordinates are inside the 'tiles' array
				if (x + i < DungeonGenerator.SIZE && y + j < DungeonGenerator.SIZE && x + i >= 0 && y + j >= 0) {

					// only generates a room if the array is empty at the specified coordinates
					if (DungeonGenerator.getTileAt(x + i, y + j) == null) {

						// occupies a tile
						DungeonGenerator.setTileAt(x + i, y + j, new RoomFloor(x + i, y + j));

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
	 * @param door Door object to add to 'doors' List adds the parameter to the
	 *             'doors' List if there are no more than one door already in the
	 *             List. this prevents the Room to have to many Doors, therefore
	 *             avoiding problems with PathFinder
	 */
	protected void addDoor(int x, int y, Direction dir) {
		if (doors.size() < 2) {
			Door door = new Door(x, y, dir);
			doors.add(door);
			DungeonGenerator.setTileAt(door.x, door.y, door);
		}
	}

	/**
	 * @return the first instance of Door in 'doors' List used by the PathFinder to
	 *         path TO
	 * @ATTENTION does NOT remove the Door
	 */
	public Door getEntrance() {
		if (!doors.isEmpty())
			return doors.get(0);
		throw new NullPointerException("No Doors avaiable");
	}

	/**
	 * @return the last instance of Door in 'doors' List. If there is none:
	 * @return the first instance of Door in 'doors' List. Exit is used by the
	 *         PathFinder to path FROM
	 * @ATTENTION does NOT remove the Door
	 */
	public Door getExit() {
		if (!doors.isEmpty()) {
			if (doors.size() > 1)
				return doors.get(1);

			// else
			return doors.get(0);
		}
		// else
		throw new NullPointerException("No Doors avaiable");
	}

	/**
	 * removes ALL Doors from 'doors' List
	 */
	private void removeDoors() {
		for (Door door : doors) {
			try {
				// removing door
				DungeonGenerator.setTileAt(door.x, door.y, null);
			} catch (ArrayIndexOutOfBoundsException e) {
				// nothing happens
			}
		}
	}

	/**
	 * @param x the x-coordinate the door shall be created
	 * @param y the y-coordinate the door shall be created with a chance of 5%:
	 *          creates a door at x, y
	 */
	private void attemptDoorCreation(int x, int y, Direction dir) {
		if (Math.random() < 0.05)
			addDoor(x, y, dir);
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
		for (int k = i; k >= -sizeX; k--)
			for (int h = j; h >= -sizeY; h--)
				try {
					// deleting tile
					DungeonGenerator.setTileAt(x + k, x + h, null);
				} catch (ArrayIndexOutOfBoundsException e) {
					// do nothing
				}
		// removing doors
		removeDoors();
	}
	
	public void overhaulDoorPositions() {
		for (int i = -sizeX; i <= sizeX; i++) {
			
		}
	}
}