package main.core;

import main.entitiys.Enemy;
import main.tiles.RoomFloor;
import main.tiles.Tile;

import java.util.ArrayList;

public class EnemyController {

	private int enemyCount;
	private ArrayList<Enemy> eList;
	private static EnemyController instance;

	public EnemyController() {
		enemyCount = 0;
		eList = new ArrayList<>();
		instance = this;
	}

	public static EnemyController getInstance() {
		if (instance == null) {
			new EnemyController();
		}
		return instance;
	}

	public void setEnemyCount(int i) {
		enemyCount = i;
		generateEnemies();
	}

	public void moveEnemies() {
		for (Enemy e : eList) {
			e.moveEnemy();
		}
	}

	private void generateEnemies() {
		while (eList.size() < enemyCount) {
			ArrayList<Tile> temp = new ArrayList<>();
			for (int i = 0; i < DungeonGenerator.SIZE; i++) {
				for (int j = 0; j < DungeonGenerator.SIZE; j++) {
					Tile t = DungeonGenerator.getTileAt(i, j);
					if (t instanceof RoomFloor) {
						temp.add(t);
					}
				}
			}
			Tile randomTile =
					temp.get((int) (Math.random() * (temp.size() - 1)));
			Enemy en = new Enemy(randomTile);
			randomTile.addContent(en);
			eList.add(en);
		}
	}
}
