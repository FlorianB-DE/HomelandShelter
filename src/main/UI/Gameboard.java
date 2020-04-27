package main.UI;

import main.Main;
import main.core.DungeonGenerator;
import main.core.EnemyController;
import main.core.NeighbourFinder;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.entitiys.Character;
import main.tiles.Door;
import main.tiles.Floor;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import main.tiles.Wall;
import utils.PathNotFoundException;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.concurrent.BlockingQueue;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 1.0 06.04.2020
 */
public class Gameboard extends Menue implements KeyListener {
	private static Gameboard currentInstance;
	private final double MIN_VISIBLE_TILES = 10;
	private Tile[][] tilegrid;
	private Tile[][] tilegridInFOV;
	private Character c;
	private ActionListener actionListener;

	public Gameboard() {
		currentInstance = this;
		addMouseListener(this);
		addKeyListener(this);
		tilegrid = DungeonGenerator.generateDungeon();
		c = Main.getPlayer();
		EnemyController.getInstance().setEnemyCount(10);
	}

	public static Gameboard getCurrentInstance() {
		return currentInstance;
	}

	/**
	 * Needed to tell the JFrame to repaint without directly referring to the
	 * JFrame.
	 *
	 * @param actionListener a reference to an actiobPerformed method
	 */
	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		doGameCycle();
		Tile[] n = NeighbourFinder
				.findNeighbours((int) Math.round(c.x), (int) Math.round(c.y));
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				moveCharacter(n[0]);
				break;
			case KeyEvent.VK_RIGHT:
				moveCharacter(n[1]);
				break;
			case KeyEvent.VK_DOWN:
				moveCharacter(n[2]);
				break;
			case KeyEvent.VK_LEFT:
				moveCharacter(n[3]);
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		double size = (int) (Math
				.ceil((Math.min(getWidth(), getHeight()) / MIN_VISIBLE_TILES)));
		int x, y;
		x = (int) Math.floor(e.getX() / size);
		y = (int) Math.floor(e.getY() / size);

		Tile tile = tilegridInFOV[x][y];


		PathFinderConfig pfc = new PathFinderConfig();
		pfc.setDisallowed();
		pfc.addDest(Wall.class);
		try {
			PathFinder pf = new PathFinder(tilegrid, pfc);
			BlockingQueue<Point> p = pf.findPath(c.getLocatedAt(), tile);
			for (int i = 0; i < p.size() - 1; i++) {
				doGameCycle();
			}
			moveCharacter(tile);
		} catch (PathNotFoundException pnfe) {
			//Could not move

		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int size = (int) (Math
				.ceil((Math.min(getWidth(), getHeight()) / MIN_VISIBLE_TILES)));

		tilegridInFOV =
				new Tile[(int) Math.ceil(getWidth() / (double) size)][(int) Math
						.ceil(getHeight() / (double) size)];
		fetchTiles();
		for (int i = 0; i < tilegridInFOV.length; i++) {
			for (int j = 0; j < tilegridInFOV[i].length; j++) {
				if (tilegridInFOV[i][j] == null) {
					tilegridInFOV[i][j] = new Wall(0, 0, size);
					tilegridInFOV[i][j].show(g2d, size * i, size * j);
				} else {
					tilegridInFOV[i][j].setSize(size, size);
					tilegridInFOV[i][j].show(g2d, size * i, size * j);
				}
			}
		}
	}

	/**
	 * does everything that needs to be done in a turn (move enemies, repaint,
	 * etc)
	 */
	private void doGameCycle() {
		EnemyController.getInstance().moveEnemies();
		actionListener.actionPerformed(null); //repaints
	}

	/**
	 * Method for fetching the needed Tiles from the original Tiles Array with
	 * all
	 * available Tiles. Needed because not all the Tiles are showed at the same
	 * Time.
	 */
	private void fetchTiles() {
		for (int i = 0; i < tilegrid.length; i++) {
			for (int j = 0; j < tilegrid[i].length; j++) {
				try {
					tilegrid[i][j].getPlayer().setLocation(i, j);
				} catch (Exception e) {
					// Nothing
				}
			}
		}
		for (int i = 0; i < tilegridInFOV.length; i++) {
			for (int j = 0; j < tilegridInFOV[i].length; j++) {
				int ix = c.x + i - tilegridInFOV.length / 2;
				int iy = c.y + j - tilegridInFOV[i].length / 2;
				if (ix >= 0 && ix < tilegrid.length && iy >= 0 &&
					iy < tilegrid[i].length) {
					tilegridInFOV[i][j] = tilegrid[ix][iy];
				}
			}
		}
	}

	private void moveCharacter(Tile tile) {
		if ((tile instanceof RoomFloor || tile instanceof Door ||
			 tile instanceof Floor)) {

			// special case Door may be closed
			if (tile instanceof Door) {

				// if the door is closed do nothing
				if (((Door) tile).isClosed()) {
					return;
				}
			}
			c.move(tile);
		}
	}

	/**
	 * Method to allocate player instance.
	 *
	 * @return current player instance of type Character
	 */
	public Character getPlayer() {
		return c;
	}

	public Tile[][] getTilegrid() {
		return tilegrid;
	}
}
