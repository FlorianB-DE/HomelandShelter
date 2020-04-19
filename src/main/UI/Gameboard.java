package main.UI;

import main.Main;
import main.core.DungeonGenerator;
import main.core.EnemyController;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.entitiys.Character;
import main.tiles.Door;
import main.tiles.Tile;
import main.tiles.Wall;
import utils.exceptions.PathNotFoundException;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Queue;

import javax.swing.Timer;

/**
 * TODO
 * 
 * @author Florian M. Becker and Tim Bauer
 * @version 1.0 06.04.2020
 */
public class Gameboard extends Menue implements KeyListener, ActionListener {
	private Tile[][] tilegrid;
	private Tile[][] tilegridInFOV;
	private final double MIN_VISIBLE_TILES = 10.0;

	private Character c;
	private ActionListener actionListener;
	private static Timer gameTimer;

	public Gameboard() {
		addMouseListener(this);
		addKeyListener(this);
		tilegrid = DungeonGenerator.generateDungeon();
		c = Main.getPlayer();
		EnemyController.getInstance().setEnemyCount(10);
		gameTimer = new Timer(100, this);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int size = (int) (Math.ceil((Math.min(getWidth(), getHeight()) / MIN_VISIBLE_TILES)));

		tilegridInFOV = new Tile[(int) Math.ceil(getWidth() / (double) size)][(int) Math
				.ceil(getHeight() / (double) size)];
		fetchTiles();
		for (int i = 0; i < tilegridInFOV.length; i++) {
			for (int j = 0; j < tilegridInFOV[i].length; j++) {
				// fills with a Wall if screen exceeds tilegrid
				if (tilegridInFOV[i][j] == null) {
					tilegridInFOV[i][j] = new Wall(0, 0, size, 0);
					tilegridInFOV[i][j].show(g2d, size * i, size * j);
				} else {
					tilegridInFOV[i][j].setSize(size, size);
					tilegridInFOV[i][j].show(g2d, size * i, size * j);
				}
			}
		}
	}

	/**
	 * Method for fetching the needed Tiles from the original Tiles Array with all
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
				if (ix >= 0 && ix < tilegrid.length && iy >= 0 && iy < tilegrid[i].length) {
					tilegridInFOV[i][j] = tilegrid[ix][iy];
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		double size = (int) (Math.ceil((Math.min(getWidth(), getHeight()) / MIN_VISIBLE_TILES)));
		int x, y;
		x = (int) Math.floor(e.getX() / size);
		y = (int) Math.floor(e.getY() / size);

		Tile tile = tilegridInFOV[x][y];

		// special case Door may be closed
		if (tile instanceof Door) {

			// if the door is closed do nothing
			if (((Door) tile).isClosed())
				return;

		}

		PathFinderConfig pfc = new PathFinderConfig();
		pfc.setDisallowed();
		pfc.addDest(Wall.class);
		try {
			PathFinder pf = new PathFinder(tilegrid, pfc);
			Queue<Point> p = pf.findPath(c.getLocatedAt(), tile);
			c.addPath(p);
			gameTimer.start();
		} catch (PathNotFoundException pnfe) {
			// Could not move
		}

	}

	/**
	 * does everything that needs to be done in a turn (move enemies, repaint, etc)
	 */
	private void doGameCycle() {
		if (!c.moveStep())
			gameTimer.stop();
		EnemyController.getInstance().moveEnemies();
		actionListener.actionPerformed(new ActionEvent(this, Integer.MAX_VALUE, "repaint")); // repaints
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

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO not use currently broken
//		doGameCycle();
//		Tile[] n = NeighbourFinder.findNeighbours(c.x, c.y);
//		try {
//			switch (e.getKeyCode()) {
//			case KeyEvent.VK_UP:
//				c.move(n[0]);
//				doGameCycle();
//				break;
//			case KeyEvent.VK_RIGHT:
//				c.move(n[1]);
//				doGameCycle();
//				break;
//			case KeyEvent.VK_DOWN:
//				c.move(n[2]);
//				doGameCycle();
//				break;
//			case KeyEvent.VK_LEFT:
//				c.move(n[3]);
//				doGameCycle();
//				break;
//			}
//		} catch (PathNotFoundException pnfe) {
//			// hit Wall or closed Door
//		}
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
	public void actionPerformed(ActionEvent e) {
		doGameCycle();
	}
}
