package main;

import main.entitiys.Character;
import main.tiles.Door;
import main.tiles.Floor;
import main.tiles.Tile;

import java.awt.*;

public abstract class DungeonGenerator {
	protected static Tile[][] tiles;

	public static Tile[][] generateDungeon() {
		tiles = new Tile[100][100];
		StartRoom s;
		for (s = null; s == null; ) {
			try {
				s = new StartRoom();
			} catch (Exception e) {
			}
		}
		EndRoom er;
		for (er = null; er == null; ) {
			try {
				er = new EndRoom();
			} catch (Exception e) {
			}
			if (er != null) {
				if (er.distance(s.x, s.y) < 10) {
					er = null;
				}
			}
		}

		PathFinder pf = new PathFinder(tiles);
		pf.findPath(s.getDoor(), er.getDoor());

		return tiles;
	}

	protected static Tile getTileAt(int x, int y) {
		return tiles[x][y];
	}

	protected static void setTileAt(int x, int y, Tile toSet) {
		tiles[x][y] = toSet;
	}

	private static class Room extends Point {
		int size;

		public Room(int size, int x, int y) throws Exception {
			super(x, y);
			this.size = size;
			generateRoom();
		}

		protected void generateRoom() throws Exception {
			for (int i = -size / 2; i <= size / 2; i++) {
				for (int j = -(size / 2); j <= size / 2; j++) {
					if (getTileAt(x + i, y + j) == null) {
						setTileAt(x + i, y + j, new Floor(0, 0, 0));
					} else {
						throw new IllegalAccessException("Tile is not Empty!");
					}
				}
			}
		}
	}

	private static class StartRoom extends Room {

		private Door door;

		public StartRoom() throws Exception {
			super(3, (int) (int) Math.round((Math.random() * 100)),
				  (int) Math.round((Math.random() * 100)));
		}

		@Override
		protected void generateRoom() throws Exception {
			super.generateRoom();
			Character c = new Character(x, y, 0);
			((Floor) tiles[x][y]).addContent(c);
			door = new Door(x + 2, y + 1, 1);
		}

		public Door getDoor() {
			return door;
		}
	}

	private static class EndRoom extends Room {

		private Door door;

		public EndRoom() throws Exception {
			super(3, (int) (Math.random() * 100), (int) (Math.random() * 100));
		}

		@Override
		protected void generateRoom() throws Exception {
			super.generateRoom();
			System.out.println("Exit created at: " + x + " " + y);
			door = new Door(x + 2, y + 1, 1);
		}

		public Door getDoor() {
			return door;
		}
	}
}
