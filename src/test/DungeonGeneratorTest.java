package test;

import main.core.DungeonGenerator;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.tiles.Door;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import main.tiles.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.PathNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DungeonGeneratorTest {

	private PathFinderConfig pfc;

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

	@BeforeEach
	public void generateConfig() {
		pfc = new PathFinderConfig();
		pfc.setDisallowed();
		pfc.addDest(RoomFloor.class);
	}

	@Test
	public void generatePath() {
		Tile[][] t = new Tile[1][3];
		t[0][1] = new Wall(0, 1, 1);
		new PathFinder(t, pfc).findPath(new Door(0, 0, Direction.vertical),
										new Door(0, 2, Direction.vertical));
	}

	@Test
	public void samePositionDoorsTest() {
		Tile[][] t = new Tile[1][1];
		new PathFinder(t, pfc).findPath(new Door(0, 0, Direction.vertical),
										new Door(0, 0, Direction.vertical));
	}

	@Test
	public void throwPathNotFoundException() {
		Tile[][] t = new Tile[1][3];
		t[0][1] = new RoomFloor(0, 1, 0);
		assertThrows(PathNotFoundException.class, () -> new PathFinder(t, pfc)
				.findPath(new Door(0, 0, Direction.vertical),
						  new Door(0, 2, Direction.vertical)));
	}
}
