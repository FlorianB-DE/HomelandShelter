package main.core;

import main.UI.Gameboard;
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
			for (Tile[] ts : Gameboard.getCurrentInstance().getTilegrid()) {
				for (Tile t : ts) {
					if (t instanceof RoomFloor) {
						temp.add(t);
					}
				}
			}
			Tile randomTile =
					temp.get((int) (Math.random() * (temp.size() - 1)));
			Enemy en = new Enemy(this, randomTile, randomTile.x, randomTile.y);
			randomTile.addContent(en);
			eList.add(en);
		}
	}
}
