package test;

import main.DungeonGenerator;
import main.core.PathFinder;
import main.tiles.Door;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import org.junit.jupiter.api.Test;
import utils.PathNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DungeonGeneratorTest {

	@Test
	public void generate() {
		DungeonGenerator.generateDungeon();
		DungeonGenerator.generateDungeon();
		DungeonGenerator.generateDungeon();
		DungeonGenerator.generateDungeon();
		DungeonGenerator.generateDungeon();
		DungeonGenerator.generateDungeon();
		DungeonGenerator.generateDungeon();
		DungeonGenerator.generateDungeon();
		DungeonGenerator.generateDungeon();
		DungeonGenerator.generateDungeon();
		DungeonGenerator.generateDungeon();
	}

	@Test
	public void generatePath() {
		Tile[][] t = new Tile[1][3];
		new PathFinder(t).findPath(new Door(0, 0, 1), new Door(0, 2, 1));
	}

	@Test
	public void throwPathNotFoundException() {
		Tile[][] t = new Tile[1][3];
		t[0][1] = new RoomFloor(0, 1, 0);
		assertThrows(PathNotFoundException.class, () -> new PathFinder(t)
				.findPath(new Door(0, 0, 1), new Door(0, 2, 1)));
	}

	@Test
	public void samePositionDoorsTest() {
		Tile[][] t = new Tile[1][1];
		new PathFinder(t).findPath(new Door(0, 0, 1), new Door(0, 0, 1));
	}
}
