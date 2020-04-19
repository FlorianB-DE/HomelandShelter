package main.core;

import main.core.generationessentials.EndRoom;
import main.core.generationessentials.Room;
import main.core.generationessentials.StartRoom;
import main.entitiys.Character;
import main.tiles.Door;
import main.tiles.Floor;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import main.tiles.Wall;
import textures.Textures;
import utils.MathUtils;
import utils.exceptions.RoomGenerationObstructedException;

import java.awt.Point;
import java.util.ArrayList;
import java.util.ListIterator;
//import java.util.concurrent.BlockingQueue;

/**
 * DungeonGenerator Provides the infrastructure needed to generate an array on a
 * random basis.
 * 
 * using a blockingqueue to queue up the results in values is deprecated. For
 * now we'll test weather or not a direct access is unproblematic
 * 
 * @author Florian M. Becker
 * @version 1.0.1 2020-04-08
 */
public abstract class DungeonGenerator {

	// constants
	/** Max number of Rooms in a level without deviation */
	private static final int roomCount = 20;

	/**
	 * multiplies each calculated by that value to increase over all Room size. Is
	 * also the smallest possible room size
	 */
	private static final int tileSize_per_Room_entry = 2;

	/** Room numbers vary from minus half deviation to plus half deviation */
	private static final int deviation = 4;

	/**
	 * threshold the perlin noise has to reach for the room to get larger than
	 * tileSize_per_Room_entry
	 */
	private static final float generationThreshold = 0.65F;
	private static final byte maxTries = 3;

	public static final int SIZE = 100;

	private static float values[][];
	private static double perlinSeedZ = Math.random();
	private static byte tries = 0;

	private static Character mainChar;
	private static Tile[][] tiles;
	private static Room[] rooms;

	// private static BlockingQueue<float[][]> queue = new
	// LinkedBlockingDeque<float[][]>(1);

	/**
	 * @return an two dimensional array if SIZE in both dimensions filled with
	 *         RoomFloors, Floors and Doors for performance reasons it doensn't fill
	 *         Wall object in and leaves the spaces empty
	 */
	public static Tile[][] generateDungeon() {
		tiles = new Tile[SIZE][SIZE];
		rooms = new Room[roomCount + (int) Math.round(Math.random() * deviation - deviation / 2)];

		// start perlin generation in a new Thread cause it's very computationally
		// intensive
		Thread t = new PerlinGeneration();
		t.start();
		do {
			try {
				if (rooms[0] == null)
					rooms[0] = generateStartRoom();
				if (rooms[roomCount - 1] == null)
					rooms[roomCount - 1] = generateEndRoom(rooms[0]);
			} catch (Exception e) {
			}
		} while (/* queue.isEmpty() */ t.isAlive());

		// values = queue.remove();

		// iterate over values array
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {

				// if a values is higher than 0.5 a room generation will be initiated
				if (values[i][j] >= 0.5) {

					// standard room size = (1 * tileSize_per_Room_entry)^2
					int room_sizeX = 1;
					int room_sizeY = 1;

					// determines the size of the room by iterating the 'values' array until the
					// encountered value is lower than 'generationThreshold'
					while (i + 1 < values.length && j + 1 < values[i].length) {
						if (values[i + 1][j] > generationThreshold) {
							room_sizeX++;
							i++;
						} else if (values[i][j + 1] > generationThreshold) {
							room_sizeY++;
							j++;
						} else if (values[i + 1][j + 1] > generationThreshold) {
							room_sizeX++;
							room_sizeY++;
							i++;
							j++;
						} else
							break;
					}

					room_sizeX *= tileSize_per_Room_entry;
					room_sizeY *= tileSize_per_Room_entry;

					// iterates the 'rooms' array
					for (int k = 0; k < rooms.length; k++) {

						// if it finds a free spot in the 'rooms' array it will try to generate a room
						if (rooms[k] == null) {
							try {
								// room generation
								rooms[k] = new Room(room_sizeX, room_sizeY, i - room_sizeX / 2, j - room_sizeY / 2);
							} catch (RoomGenerationObstructedException e) {
							}
							break;
						}
					}
				}
			}
		}
		PathFinderConfig pfc = new PathFinderConfig();
		pfc.setDisallowed();
		pfc.addDest(RoomFloor.class);
		PathFinder pf = new PathFinder(tiles, pfc);
		ArrayList<Point> paths = new ArrayList<>();

		try {
			for (int i = 0; i < rooms.length - 1; i++) {
				if (rooms[i] != null) {
					Room start = rooms[i];
					while (rooms[i + 1] == null)
						i++;
					paths.addAll(pf.findPath(start.getExit(), rooms[i + 1].getEntrance()));
				}
			}
		} catch (Exception e) {
			// something went wrong during generation
			// restarts the process
			if (tries >= maxTries) {
				perlinSeedZ = Math.random();
				tries = 0;
			} else
				tries++;
			return generateDungeon();
		}

		// fills every path in
		ListIterator<Point> it = paths.listIterator();
		while (it.hasNext()) {
			Point p = it.next();
			if (tiles[p.x][p.y] == null) {
				tiles[p.x][p.y] = new Floor(p);
			}

		}

		// fills unclaimed stuff with Walls
		for (int i = 0; i < tiles.length; i++) {
			for (int k = 0; k < tiles[i].length; k++) {
				if (tiles[i][k] == null)
					tiles[i][k] = new Wall(i, k);
			}
		}
		
		

		for (int i = 0; i < rooms.length - 1; i++) {
			Door d = rooms[i].getExit();
			setTileAt(d.x, d.y, new Floor(d.getLocation()));
			int index = paths.indexOf(d.getLocation());
			paths.remove(index);

			it = paths.listIterator(index);

			while (it.hasNext()) {
				Point current = it.next();
				if (!current.equals(rooms[i + 1].getEntrance().getLocation())) {
					if (it.hasPrevious()) {
						try {
							if (!current.equals(paths.get(it.previousIndex() - 1))
									&& !current.equals(paths.get(it.nextIndex()))
									&& !paths.get(it.nextIndex()).equals(paths.get(it.previousIndex() - 1)))
								if (NeighbourFinder.pathableNeighbours(current.x, current.y) <= 2) {
									if (paths.get(it.previousIndex() - 1).x == paths.get(it.nextIndex()).x) {
										d.setLocation(current);
										d.setTexture(Textures.DOOR);
										break;
									} else if (paths.get(it.previousIndex() - 1).y == paths.get(it.nextIndex()).y) {
										d.setLocation(current);
										d.setTexture(Textures.LEFT_DOOR);
										break;
									}
								}
						} catch (IndexOutOfBoundsException e) {
							// do nothing
						}
					}
				} else {
					setTileAt(d.x, d.y, new Floor(d.getLocation()));
					break;
				}
			}
			if (d != null)
				setTileAt(d.x, d.y, d);
		}

		return tiles;

	}

	/**
	 * @return a StartRoom with a new Character instance as it's middle Tile content
	 */
	private static Room generateStartRoom() {
		StartRoom s;
		for (s = null; s == null;) {
			try {
				s = new StartRoom();
			} catch (RoomGenerationObstructedException e) {
			}
		}
		return s;
	}

	/**
	 * @param startroom
	 * @return an EndRoom with a new StairDown instance as it's middle Tile content
	 */
	private static Room generateEndRoom(Room startroom) {
		EndRoom er;
		for (er = null; er == null;) {
			try {
				er = new EndRoom();
			} catch (RoomGenerationObstructedException e) {
			}
			if (er != null) {
				if (er.distance(startroom.x, startroom.y) < 10) {
					er = null;
				}
			}
		}
		return er;
	}

	/**
	 * @param x coordinate
	 * @param y coordinate
	 * @return the Tile located in the 'tiles[][]' at x, y and null if there is none
	 */
	public static Tile getTileAt(int x, int y) {
		return tiles[x][y];
	}

	/**
	 * @param x     coordinate
	 * @param y     coordinate
	 * @param toSet Tile the 'tiles[][]' at x, y is set to
	 */
	public static void setTileAt(int x, int y, Tile toSet) {
		tiles[x][y] = toSet;
	}

	public static Character getPlayer() {
		return mainChar;
	}
	
	public static void setPlayer(Character c) {
		mainChar = c;
	}

	/**
	 * uses 'extends Thread' to out source calculations for the perlin niose to
	 * another thread to allow the program to do certain things in the meantime.
	 * Adds a two dimensional array of floats each at length SIZE to the
	 * BlockedQueue provided by DungeonGenerator to be processed in the main Thread
	 * 
	 * @author Florian M. Becker
	 */
	private static class PerlinGeneration extends Thread {

		@Override
		public void run() {
			values = new float[SIZE][SIZE];
			for (int i = 0; i < values.length; i++)
				for (int j = 0; j < values[i].length; j++)
					values[i][j] = (float) ((MathUtils.perlinNoise(i * 0.075, j * 0.075, perlinSeedZ) + 1) * 0.5);

			// queue.add(values);
		}
	}
}