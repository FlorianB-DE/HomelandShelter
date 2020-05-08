package main.UI;

import main.Constants;
import main.core.DungeonGenerator;
import main.core.EnemyController;
import main.core.NeighbourFinder;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.entitiys.Player;
import main.entitiys.Enemy;
import main.tiles.Tile;
import main.tiles.Wall;
import utils.exceptions.PathNotFoundException;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Queue;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 1.0 06.04.2020
 */
public final class Gameboard extends JPanel implements KeyListener, ActionListener, MouseListener {
	// static attributes
	private static Gameboard currentInstance;

	// attributes
	private final Timer gameTimer;
	private final DungeonGenerator level;

	private Tile[][] tilegridInFOV;
	private Player c;

	public Gameboard() {
		gameTimer = new Timer(100, this);

		// add listeners
		Constants.GAME_FRAME.addMouseListener(this);
		Constants.GAME_FRAME.addKeyListener(this);

		// remove layout
		setLayout(null);
		// generate level
		level = new DungeonGenerator();

		setUp();
	}

	/**
	 * needs to be called every time a Gameboard is loaded. Not a constructor cause
	 * already existing levels can be revisited.
	 */
	private void setUp() {
		currentInstance = this;
		c = level.getPlayer();
		c.setInventoryVisibility(false);
		Constants.GAME_FRAME.addMouseListener(c.getInventoryListener());
		EnemyController.getInstance().setEnemyCount(10);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		doGameCycle();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		final Tile[] n = NeighbourFinder.findNeighbours(c.x, c.y);
		try {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				if (n[0].isWalkable()) {
					c.move(n[0]);
					doGameCycle();
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (n[1].isWalkable()) {
					c.move(n[1]);
					doGameCycle();
				}
				break;
			case KeyEvent.VK_DOWN:
				if (n[2].isWalkable()) {
					c.move(n[2]);
					doGameCycle();
				}
				break;
			case KeyEvent.VK_LEFT:
				if (n[3].isWalkable()) {
					c.move(n[3]);
					doGameCycle();
				}
				break;
			case KeyEvent.VK_I:
				c.setInventoryVisibility(!c.getInventoryVisibility());
				Constants.GAME_FRAME.repaint();
				break;
			case KeyEvent.VK_SPACE:
				c.detection(c.getLocatedAt());
				Constants.GAME_FRAME.repaint();
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			// do nothing
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// do nothing

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// do nothing

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!c.getInventoryVisibility()) {
			double size = Math.ceil((Math.min(getWidth(), getHeight()) / ((double) Constants.RENDER_DISTANCE)));
			int x, y;
			x = (int) Math.floor(e.getX() / size);
			y = (int) Math.floor(e.getY() / size);

			Tile tile = tilegridInFOV[x][y];
			if (tile.isWalkable()) {

				Enemy en = null;
				if ((en = EnemyController.getInstance().isEnemyAtTile(tile.x, tile.y)) != null
						&& NeighbourFinder.isNeighbour(c.x, c.y, tile.x, tile.y)) {
					// TODO EDIT DAMAGE
					en.hit(100);
					Constants.GAME_FRAME.repaint();
				} else {

					PathFinderConfig pfc = new PathFinderConfig();
					pfc.setDisallowed();
					pfc.addDest(Wall.class);
					try {
						PathFinder pf = new PathFinder(level.getTilegrid(), pfc);
						Queue<Point> p = pf.findPath(c.getLocatedAt(), tile);
						c.addPath(p);
						gameTimer.start();
					} catch (PathNotFoundException pnfe) {
						// Could not move
					}
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// nothing happens
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// nothing happens
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// nothing happens
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// nothing happens
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int size = (int) (Math.ceil((Math.min(getWidth(), getHeight()) / Constants.RENDER_DISTANCE)));

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

		for (Component comp : getComponents()) {
			comp.repaint();
		}
	}

	/**
	 * does everything that needs to be done in a turn (move enemies, repaint, etc)
	 */
	private void doGameCycle() {
		if (!c.moveStep()) {
			gameTimer.stop();
		}
		EnemyController.getInstance().moveEnemies();
		Constants.GAME_FRAME.repaint();
	}

	/**
	 * Method for fetching the needed Tiles from the original Tiles Array with all
	 * available Tiles. Needed because not all the Tiles are showed at the same
	 * Time.
	 */
	private void fetchTiles() {
		for (int i = 0; i < tilegridInFOV.length; i++) {
			for (int j = 0; j < tilegridInFOV[i].length; j++) {
				final int ix = c.x + i - tilegridInFOV.length / 2;
				final int iy = c.y + j - tilegridInFOV[i].length / 2;
				if (ix >= 0 && ix < Constants.DUNGEON_SIZE && iy >= 0 && iy < Constants.DUNGEON_SIZE) {
					tilegridInFOV[i][j] = level.getTileAt(ix, iy);
				}
			}
		}
	}

	/**
	 * Method to allocate player instance.
	 *
	 * @return current player instance of type Player
	 */
	public Player getPlayer() {
		return c;
	}

	public static Gameboard getCurrentInstance() {
		return currentInstance;
	}

	public Tile getTileAt(int x, int y) {
		return level.getTileAt(x, y);
	}

	public Tile[][] getTilegrid() {
		return level.getTilegrid();
	}
}
