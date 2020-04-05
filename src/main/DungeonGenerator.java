package main;

import main.entitiys.Character;
import main.entitiys.StairDown;
import main.tiles.Door;
import main.tiles.Floor;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import utils.MathUtils;
import utils.PathNotFoundException;
import utils.RoomGenerationObstructedException;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * DungeonGenerator
 * Provides the infrastructure needed to generate an array on a random basis.
 * @author Florian M. Becker
 * @version 0.9 05.04.2020
 */
public abstract class DungeonGenerator {

	public static final int SIZE = 100;

	static Character mainChar;

	private static Tile[][] tiles;
	private static float values[][];
	private static Room[] rooms;
	
	//Max nummber of Rooms in a level without deviation
	private static final int roomCount = 20;
	
	//multiplies each calculated by that value to increase over all Room size
	private static final int tileSize_per_Room_entry = 4;
	
	// 
	private static final int deviation = 4;
	private static final float generationThreshold = 0.65F;
	private static BlockingQueue<float[][]> queue = new LinkedBlockingDeque<float[][]>(1);

	/**
	 * @return an two dimensional array if SIZE in both dimensions filled with Roomfloors, Floors and Doors
	 * for performance reasons it doensn't fill Wall object in and leaves the spaces empty
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
			for (int i = 0; i < rooms.length - 1;  i++) {
				if (rooms[i] != null) {
					Room start = rooms[i];
					while (rooms[i + 1] == null)
						i++;
					System.out.println(rooms[i + 1].getEntrance());
					paths.addAll(pf.findPath(start.getExit(), rooms[i + 1].getEntrance(), tiles));
				}
			}
		} catch (PathNotFoundException e) {
			System.out.println("Generate new");
			//return generateDungeon();
		}
		while (!paths.isEmpty()) {
			Point p = paths.remove();
			tiles[p.x][p.y] = new Floor(p, 0);

		}

		return tiles;
	}

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

	private static Tile getTileAt(int x, int y) {
		return tiles[x][y];
	}

	private static void setTileAt(int x, int y, Tile toSet) {
		tiles[x][y] = toSet;
	}

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
							setTileAt(x + i, y + j, new RoomFloor(0, 0, 0));
							if (i == -sizeX / 2 || i == sizeX / 2 || j == -sizeY / 2 || j == sizeY / 2)
								if (Math.random() < 0.05) {
									addDoor(new Door(x + i, y + j, 0));
								}
						} else {
							for (int k = i; k != -sizeX / 2; k--)
								for (int h = j; h != -sizeY / 2; h--)
									setTileAt(x + k, x + h, null);
							removeDoors();
							throw new RoomGenerationObstructedException();
						}
					else
						throw new RoomGenerationObstructedException();
			if (doors.size() == 0)
				doors.add(new Door(x + sizeX / 2, y + sizeY / 2 - 1, 0));
		}

//		public List<Door> getDoors() {
//			return doors;
//		}

		protected void addDoor(Door door) {
			if (doors.size() < 2) {
				doors.add(door);
				setTileAt(door.x, door.y, door);
			}
		}

		public Door getEntrance() {
			if (!doors.isEmpty())
				return doors.get(0);
			throw new NullPointerException("No Doors avaiable");
		}

		public Door getExit() {
			if (!doors.isEmpty())
				if (doors.size() > 1)
					return doors.get(1);
				else
					return doors.get(0);
			throw new NullPointerException("No Doors avaiable");
		}
		
		private void removeDoors() {
			for (Door door : doors) {
				tiles[door.x][door.y] = null;
			}
		}
	}

	private static class StartRoom extends Room {

		public StartRoom() throws RoomGenerationObstructedException {
			super(3, (int) (int) Math.round((Math.random() * 100)), (int) Math.round((Math.random() * 100)));
		}

		@Override
		protected void generateRoom() throws RoomGenerationObstructedException {
			super.generateRoom();
			mainChar = new Character(tiles[x][y], getLocation());
			tiles[x][y].addContent(mainChar);
			addDoor(new Door(x + 2, y + 1, 1));
		}
	}

	private static class EndRoom extends Room {

		public EndRoom() throws RoomGenerationObstructedException {
			super(3, (int) (Math.random() * 100), (int) (Math.random() * 100));
		}

		@Override
		protected void generateRoom() throws RoomGenerationObstructedException {
			super.generateRoom();
			tiles[x][y].addContent(new StairDown(tiles[x][y], getLocation()));
			addDoor(new Door(x + 2, y + 1, 1));
		}
	}

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
