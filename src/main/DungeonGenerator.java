package main;

import main.entitiys.Character;
import main.entitiys.StairDown;
import main.tiles.Door;
import main.tiles.Floor;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import utils.MathUtils;
import utils.RoomGenerationObstructedException;

import java.awt.Point;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class DungeonGenerator {
	private static Tile[][] tiles;
	private static float values[][];
	private static Room[] rooms;
	private static final int size = 100;
	private static final int roomCount = 15;
	private static final int tileSize_per_Room_entry = 4;
	private static final int deviation = 4;
	private static final float generationThreshold = 0.6F;
	private static BlockingQueue<float[][]> queue = new LinkedBlockingDeque<float[][]>(1);

	public static Tile[][] generateDungeon() {
		tiles = new Tile[size][size];
		rooms = new Room[roomCount + (int) Math.round(Math.random() * deviation - deviation / 2)];
		Thread t = new PerlinGeneration();
		t.start();
		do {
			try {
				if (rooms[0] == null)
					rooms[0] = generateStartRoom();
				if (rooms[1] == null)
					rooms[1] = generateEndRoom(rooms[0]);
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
		BlockingQueue<Point> paths = pf.findPath(rooms[0].getDoor(), rooms[1].getDoor());
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

	protected static Tile getTileAt(int x, int y) {
		return tiles[x][y];
	}

	protected static void setTileAt(int x, int y, Tile toSet) {
		tiles[x][y] = toSet;
	}

	private static class Room extends Point {
		int sizeX, sizeY;
		private Door door;

		public Room(int size, int x, int y) throws RoomGenerationObstructedException {
			super(x, y);
			sizeX = size;
			sizeY = size;
			generateRoom();
		}

		public Room(int sizeX, int sizeY, int x, int y) throws RoomGenerationObstructedException {
			super(x, y);
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			generateRoom();
		}

		protected void generateRoom() throws RoomGenerationObstructedException {
			for (int i = -sizeX / 2; i <= sizeX / 2; i++)
				for (int j = -(sizeY / 2); j <= sizeY / 2; j++)
					if (x + i < size && y + j < size && x + i >= 0 && y + j >= 0)
						if (getTileAt(x + i, y + j) == null) {
							setTileAt(x + i, y + j, new RoomFloor(0, 0, 0));
						} else {
							throw new RoomGenerationObstructedException();
						}

		}

		public Door getDoor() {
			return door;
		}

		protected void setDoor(Door door) {
			this.door = door;
			setTileAt(this.door.x, this.door.y, this.door);
		}
	}

	private static class StartRoom extends Room {

		public StartRoom() throws RoomGenerationObstructedException {
			super(3, (int) (int) Math.round((Math.random() * 100)), (int) Math.round((Math.random() * 100)));
		}

		@Override
		protected void generateRoom() throws RoomGenerationObstructedException {
			super.generateRoom();
			Character c = new Character(tiles[x][y], getLocation());
			tiles[x][y].addContent(c);
			setDoor(new Door(x + 2, y + 1, 1));
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
			setDoor(new Door(x + 2, y + 1, 1));
		}
	}

	private static class PerlinGeneration extends Thread {
		private float[][] values = new float[size][size];

		@Override
		public void run() {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					values[i][j] = (float) ((MathUtils.perlinNoise(i * 0.075, j * 0.075, 0.45678) + 1) * 0.5);
				}
			}
			queue.add(values);
		}
	}
}
