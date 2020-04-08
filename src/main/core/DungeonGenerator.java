package main.core;

import main.entitiys.Character;
import main.entitiys.StairDown;
import main.tiles.Door;
import main.tiles.Floor;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import main.tiles.Wall;
import utils.MathUtils;
import utils.RoomGenerationObstructedException;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * DungeonGenerator Provides the infrastructure needed to generate an array on a
 * random basis.
 * 
 * @author Florian M. Becker
 * @version 1.0 05.04.2020
 */
public abstract class DungeonGenerator {

	public static final int SIZE = 100;

	private static Character mainChar;
	private static Tile[][] tiles;
	private static float values[][];
	private static Room[] rooms;

	/** Max number of Rooms in a level without deviation */
	private static final int roomCount = 20;

	/**
	 * multiplies each calculated by that value to increase over all Room size. Is
	 * also the smallest possible room size
	 */
	private static final int tileSize_per_Room_entry = 4;

	/** Room numbers vary from minus half deviation to plus half deviation */
	private static final int deviation = 4;

	/**
	 * threshold the perlin noise has to reach for the room to get larger than
	 * tileSize_per_Room_entry
	 */
	private static final float generationThreshold = 0.65F;

	private static BlockingQueue<float[][]> queue = new LinkedBlockingDeque<float[][]>(1);

	/**
	 * @return an two dimensional array if SIZE in both dimensions filled with
	 *         RoomFloors, Floors and Doors for performance reasons it doensn't fill
	 *         Wall object in and leaves the spaces empty
	 */
	public static Tile[][] generateDungeon() {
		tiles = new Tile[SIZE][SIZE];
		rooms = new Room[roomCount + (int) Math.round(Math.random() * deviation - deviation / 2)];
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
		} while (queue.isEmpty());

		values = queue.remove();

		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				if (values[i][j] >= 0.5) {
					int room_sizeX = 1;
					int room_sizeY = 1;
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
					for (int k = 0; k < rooms.length; k++) {
						if (rooms[k] == null) {
							do {
								try {
									rooms[k] = new Room(room_sizeX * tileSize_per_Room_entry,
											room_sizeY * tileSize_per_Room_entry, i - room_sizeX / 2,
											j - room_sizeY / 2);
								} catch (RoomGenerationObstructedException e) {
								}
								break;
							} while (rooms[k] == null);
						}
					}
				}
			}
		}
		PathFinder pf = new PathFinder(tiles);
		Queue<Point> paths = new LinkedList<>();
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
			return generateDungeon();
		}
		while (!paths.isEmpty()) {
			Point p = paths.remove();
			if (tiles[p.x][p.y] == null) {
				tiles[p.x][p.y] = new Floor(p);
			}

		}
		for (int i = 0; i < tiles.length; i++) {
			for (int k = 0; k < tiles[i].length; k++) {
				if (tiles[i][k] == null)
					tiles[i][k] = new Wall(i, k);
			}
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
	private static Tile getTileAt(int x, int y) {
		return tiles[x][y];
	}

	/**
	 * @param x     coordinate
	 * @param y     coordinate
	 * @param toSet Tile the 'tiles[][]' at x, y is set to
	 */
	private static void setTileAt(int x, int y, Tile toSet) {
		tiles[x][y] = toSet;
	}

	public static Character getPlayer() {
		return mainChar;
	}

	/**
	 * class for handling the generation of a Room inside 'tiles' array provided by
	 * DungeonGenerator every Room has a rectangular shape and at least one Door
	 * (two at max) creating a new instance of this class @throws a
	 * RoomGenerationObstructedException and reverts its' changes in the 'tiles'
	 * array
	 * 
	 * @author Florian M. Becker
	 */
	private static class Room extends Point {
		int sizeX, sizeY;
		private List<Door> doors;

		public Room(int size, int x, int y) throws RoomGenerationObstructedException {
			this(size, size, x, y);
		}

		public Room(int sizeX, int sizeY, int x, int y) throws RoomGenerationObstructedException {
			super(x, y);
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			doors = new ArrayList<Door>();
			generateRoom();
		}

		protected void generateRoom() throws RoomGenerationObstructedException {
			for (int i = -sizeX / 2; i <= sizeX / 2; i++)
				for (int j = -sizeY / 2; j <= sizeY / 2; j++)
					if (x + i < SIZE && y + j < SIZE && x + i >= 0 && y + j >= 0)
						if (getTileAt(x + i, y + j) == null) {
							setTileAt(x + i, y + j,
									new RoomFloor(x + i, y + j));
							if (i == -sizeX / 2 && !(j == -sizeY / 2) && !(j
									== sizeY / 2)) {
								try {
									attemptDoorCreation(i + x - 1, j + y);
								} catch (ArrayIndexOutOfBoundsException e) {
									//could not create Door
								}
							else if (i == sizeX / 2 && !(j == -sizeY / 2) && !(j == sizeY / 2))
								try {
									attemptDoorCreation(i + x + 1, j + y);
								} catch (ArrayIndexOutOfBoundsException e) {
									//could not create Door
								}
							else if (j == -sizeY / 2 && !(i == -sizeX / 2) && !(i == sizeX / 2))
								try {
									attemptDoorCreation(i + x, j + y - 1);
								} catch (ArrayIndexOutOfBoundsException e) {
									//could not create Door
								}
							else if (j == sizeY / 2 && !(i == -sizeX / 2) && !(i == sizeX / 2))
								try {
									attemptDoorCreation(i + x, j + y + 1);
								} catch (ArrayIndexOutOfBoundsException e) {
									//could not create Door
								}
							else if (i == sizeX / 2 && j == sizeY / 2 && doors.size() == 0)
								try {
									addDoor(new Door(x + i - 1, y + y + 1, 0));
								} catch (ArrayIndexOutOfBoundsException e) {
									decompose(i, j);
									throw new RoomGenerationObstructedException("ArrayIndexOutOfBounds");
								}
						} else {
							decompose(i, j);
							throw new RoomGenerationObstructedException();
						}
					else
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
		protected void addDoor(Door door) {
			if (doors.size() < 2) {
				doors.add(door);
				setTileAt(door.x, door.y, door);
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
		 * @return the last instance of Door in 'doors' List. If there is none: @return
		 *         the first instance exit is used by the PathFinder to path FROM
		 * @ATTENTION does NOT remove the Door
		 */
		public Door getExit() {
			if (!doors.isEmpty())
				if (doors.size() > 1)
					return doors.get(1);
				else
					return doors.get(0);
			throw new NullPointerException("No Doors avaiable");
		}

		/**
		 * removes ALL Doors from 'doors' List
		 */
		private void removeDoors() {
			for (Door door : doors) {
				try {
					tiles[door.x][door.y] = null;
				} catch (ArrayIndexOutOfBoundsException e) {
					//nothing happens
				}
			}
		}

		private void attemptDoorCreation(int x, int y) {
			if (Math.random() < 0.05)
				addDoor(new Door(x, y));
		}
		
		private void decompose(int i, int j) {
			for (int k = i; k != -sizeX / 2; k--)
				for (int h = j; h != -sizeY / 2; h--)
					try {
						setTileAt(x + k, x + h, null);
					} catch (NullPointerException e) {
						//do nothing
					}
			removeDoors();
		}
	}

	/**
	 * subclass of Room to add additional functionality in form of the main
	 * Character getting added as well as the tileSize_per_Room_entry is not used.
	 * Therefore the StartRoom is ALWAYS a size of 3 * 3 Tiles to ensure the
	 * Character is created a the center. The StartRoom uses Math.random() * 100 to
	 * set its' coordinates randomly between 0, 0 and 99, 99.
	 * 
	 * @author Florian M. Becker
	 */
	private static class StartRoom extends Room {

		public StartRoom() throws RoomGenerationObstructedException {
			super(3, (int) (int) Math.round((Math.random() * 100)), (int) Math.round((Math.random() * 100)));
		}

		@Override
		protected void generateRoom() throws RoomGenerationObstructedException {
			super.generateRoom();
			mainChar = new Character(tiles[x][y], getLocation());
			tiles[x][y].addContent(mainChar);
			addDoor(new Door(x + 2, y + 1));
		}

		@Override
		protected void addDoor(Door door) {
			if (getDoors().size() < 1) {
				getDoors().add(door);
				setTileAt(door.x, door.y, door);
			}
		}
	}

	/**
	 * subclass of Room to add additional functionality in form of the Exit of the
	 * level getting added as well as the tileSize_per_Room_entry is not used.
	 * Therefore the EndRoom is ALWAYS a size of 3 * 3 Tiles to ensure the StairDown
	 * is created a the center. The EndRoom uses Math.random() * 100 to set its'
	 * coordinates randomly between 0, 0 and 99, 99.
	 * 
	 * @author Florian M. Becker
	 */
	private static class EndRoom extends Room {

		public EndRoom() throws RoomGenerationObstructedException {
			super(3, (int) (Math.random() * 100), (int) (Math.random() * 100));
		}

		@Override
		protected void generateRoom() throws RoomGenerationObstructedException {
			super.generateRoom();
			tiles[x][y].addContent(new StairDown(tiles[x][y], getLocation()));
			addDoor(new Door(x + 2, y + 1));
		}
		
		@Override
		protected void addDoor(Door door) {
			if (getDoors().size() < 1) {
				getDoors().add(door);
				setTileAt(door.x, door.y, door);
			}
		}
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
		private float[][] values = new float[SIZE][SIZE];

		@Override
		public void run() {
			for (int i = 0; i < SIZE; i++) {
				for (int j = 0; j < SIZE; j++) {
					values[i][j] = (float) ((MathUtils.perlinNoise(i * 0.075, j * 0.075, 0.45678) + 1) * 0.5);
				}
			}
			queue.add(values);
		}
	}
}
