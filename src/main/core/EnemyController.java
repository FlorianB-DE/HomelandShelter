package main.core;

import main.UI.Gameboard;
import main.entitiys.Enemy;
import main.tiles.RoomFloor;
import main.tiles.Tile;

import java.util.ArrayList;

public class EnemyController extends Thread {

	private int enemyCount;
	private ArrayList<Enemy> eList;
	private static boolean run = true;
	private static EnemyController instance;

	public EnemyController() {
		enemyCount = 0;
		eList = new ArrayList<>();
		instance = this;
		instance.start();
	}

	public static EnemyController getInstance() {
		if (instance == null) {
			new EnemyController();
		}
		return instance;
	}

	public void setEnemyCount(int i) {
		enemyCount = i;
	}

	public void endEnemy() {
		run = false;
	}

	public void run() {
		while (run) {
			generateEnemies();
			//moveEnemies();
			try {
				Thread.sleep(1000);
			} catch (Exception e) {

			}
		}
	}

	private void generateEnemies() {
		while (eList.size() < enemyCount) {
			ArrayList<Tile> temp = new ArrayList<>();
			for (Tile[] ts : Gameboard.getTilegrid()) {
				for (Tile t : ts) {
					if (t instanceof RoomFloor) {
						temp.add(t);
					}
				}
			}
			Tile randomTile = temp.get((int) (Math.random() * temp.size()));
			Enemy en = new Enemy(randomTile, randomTile.x, randomTile.y);
			randomTile.addContent(en);
			eList.add(en);
		}
	}

	private void moveEnemies() {

	}
}
