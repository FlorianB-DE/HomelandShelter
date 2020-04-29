package main.core;

import main.Constants;
import main.entitiys.Enemy;
import main.tiles.RoomFloor;
import main.tiles.Tile;

import java.util.ArrayList;

public class EnemyController {

	private static EnemyController instance;
	private int enemyCount;
	private ArrayList<Enemy> eList;

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

	public Enemy isEnemyAtTile(int x, int y) {
		for (Enemy e : eList) {
			if (e.getLocatedAt().getX() == x && e.getLocatedAt().getY() == y) {
				return e;
			}
		}
		return null;
	}

	public void moveEnemies() {
		for (Enemy e : eList) {
			e.moveEnemy();
		}
	}

	public void removeEnemy(Enemy e) {
		this.eList.remove(e);
	}

	public void setEnemyCount(int i) {
		enemyCount = i;
		generateEnemies();
	}

	private void generateEnemies() {
		while (eList.size() < enemyCount) {
			ArrayList<Tile> temp = new ArrayList<>();
			for (int i = 0; i < Constants.DUNGEON_SIZE; i++) {
				for (int j = 0; j < Constants.DUNGEON_SIZE; j++) {
					Tile t = DungeonGenerator.getTileAt(i, j);
					if (t instanceof RoomFloor) {
						temp.add(t);
					}
				}
			}
			Tile randomTile =
					temp.get((int) (Math.random() * (temp.size() - 1)));
			Enemy en = new Enemy(this, randomTile);
			randomTile.addContent(en);
			eList.add(en);
		}
	}
}
