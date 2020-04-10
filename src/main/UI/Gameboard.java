package main.UI;

import main.Main;
import main.core.DungeonGenerator;
import main.core.EnemyController;
import main.core.NeighbourFinder;
import main.entitiys.Character;
import main.tiles.Door;
import main.tiles.Floor;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import main.tiles.Wall;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

/**
 * TODO
 * 
 * @author Florian M. Becker and Tim Bauer
 * @version 1.0 06.04.2020
 */
public class Gameboard extends Menue implements KeyListener {
	private static Tile[][] tilegrid;
	private Tile[][] tilegridInFOV;
	private final double MIN_VISIBLE_TILES = 10;

	private Character c;
	private ActionListener actionListener;

	public Gameboard() {
		addMouseListener(this);
		addKeyListener(this);
		tilegrid = DungeonGenerator.generateDungeon();
		c = Main.getPlayer();
		EnemyController.getInstance().setEnemyCount(10);
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
		double x = e.getX();
		double y = e.getY();
		double h = getHeight();
		double w = getWidth();

		double m = (h / 2) / (w / 2);
		boolean l1 = y > (m * x);
		boolean l2 = y > (-m * x + h);
		Tile[] n = NeighbourFinder.findNeighbours((int) Math.round(c.x), (int) Math.round(c.y));
		Tile tile = null;

		if (!l1 && !l2) {
			tile = n[0];
		} else if (!l1 && l2) {
			tile = n[1];
		} else if (l1 && l2) {
			tile = n[2];
		} else if (l1 && !l2) {
			tile = n[3];
		}

		// is the destination a Tile where you can walk on?
		if ((tile instanceof RoomFloor || tile instanceof Door || tile instanceof Floor)) {

			// special case Door may be closed
			if (tile instanceof Door) {

				// if the door is closed do nothing
				if (((Door) tile).isClosed())
					return;
			}
			
			// when no special cases apply does one movement cycle
			c.move(tile);
			doGameCycle();
			return;
		}
	}

	/**
	 * does everything that needs to be done in a turn (move enemies, repaint, etc)
	 */
	private void doGameCycle() {
		EnemyController.getInstance().moveEnemies();
		actionListener.actionPerformed(null); //repaints
	}

	/**
	 * Needed to tell the JFrame to repaint without directly referring to the
	 * JFrame.
	 *
	 * @param e a reference to an actiobPerformed method
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

	public static Tile[][] getTilegrid() {
		return tilegrid;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO if(e.getKeyCode() == KEY.DOWN) go_down(); etc...

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
