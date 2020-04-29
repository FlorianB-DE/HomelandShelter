package main.UI;

import main.Constants;
import main.core.DungeonGenerator;
import main.core.EnemyController;
import main.core.NeighbourFinder;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.entitiys.Character;
import main.entitiys.Enemy;
import main.tiles.Tile;
import main.tiles.Wall;
import utils.exceptions.PathNotFoundException;

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
import java.util.Queue;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 1.0 06.04.2020
 */
public class Gameboard extends Menue implements KeyListener, ActionListener {
	private static Timer gameTimer;
	private Tile[][] tilegrid;
	private Tile[][] tilegridInFOV;
	private Character c;
	private ActionListener actionListener;

	public Gameboard() {
		addMouseListener(this);
		addKeyListener(this);
		setLayout(null);
		tilegrid = DungeonGenerator.generateDungeon();
		c = DungeonGenerator.getPlayer();
		c.setInventoryVisibility(false);
		c.addInventoryGUI(this);
		addMouseListener(c.getInventoryListener());
		EnemyController.getInstance().setEnemyCount(10);
		gameTimer = new Timer(100, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		doGameCycle();
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
		Tile[] n = NeighbourFinder.findNeighbours(c.x, c.y);
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
					actionListener.actionPerformed(
							new ActionEvent(this, Integer.MAX_VALUE,
											"repaint"));
					break;
				case KeyEvent.VK_SPACE:
					c.detection(c.getLocatedAt());
					actionListener.actionPerformed(
							new ActionEvent(this, Integer.MAX_VALUE,
											"repaint"));
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {
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
			double size = Math.ceil((Math.min(getWidth(), getHeight()) /
									 ((double) Constants.RENDER_DISTANCE)));
			int x, y;
			x = (int) Math.floor(e.getX() / size);
			y = (int) Math.floor(e.getY() / size);

			Tile tile = tilegridInFOV[x][y];
			if (tile.isWalkable()) {

				Enemy en = null;
				if ((en = EnemyController.getInstance()
						.isEnemyAtTile(tile.x, tile.y)) != null) {
					// TODO EDIT DAMAGE
					en.hit(100);
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
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int size = (int) (Math.ceil((Math.min(getWidth(), getHeight()) /
									 Constants.RENDER_DISTANCE)));

		tilegridInFOV =
				new Tile[(int) Math.ceil(getWidth() / (double) size)][(int) Math
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
	 * does everything that needs to be done in a turn (move enemies, repaint,
	 * etc)
	 */
	private void doGameCycle() {
		if (!c.moveStep()) {
			gameTimer.stop();
		}
		EnemyController.getInstance().moveEnemies();
		actionListener.actionPerformed(new ActionEvent(this, Integer.MAX_VALUE,
													   "repaint")); // repaints
	}

	/**
	 * Method for fetching the needed Tiles from the original Tiles Array with
	 * all
	 * available Tiles. Needed because not all the Tiles are showed at the same
	 * Time.
	 */
	private void fetchTiles() {
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

	/**
	 * Method to allocate player instance.
	 *
	 * @return current player instance of type Character
	 */
	public Character getPlayer() {
		return c;
	}
}
