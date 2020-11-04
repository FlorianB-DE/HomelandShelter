package main.UI;

import main.Constants;
import main.UI.elements.IngameButton;
import main.UI.elements.UIElement;
import main.core.DungeonGenerator;
import main.core.EnemyController;
import main.core.NeighbourFinder;
import main.core.PathFinder;
import main.core.PathFinderConfig;
import main.entitys.Player;
import main.tiles.Tile;
import main.tiles.Wall;
import textures.TextureReader;
import utils.WindowUtils;
import utils.exceptions.PathNotFoundException;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 1.0 06.04.2020
 */
public final class Gameboard extends JPanel implements ActionListener {
	private class AttackButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (attackButton.isVisible()) {
				final Tile[] neighbors = NeighbourFinder.findNeighbors(c.getLocatedAt().x, c.getLocatedAt().y);
				for (Tile tile : neighbors) {
					if (tile.hasHitableContent(c)) {
						tile.hit(c.attack());
						doGameCycle();
						return;
					}
				}
			}
		}
	}

	private class GameboardKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			// Neighbor tiles
			final Tile[] n = NeighbourFinder.findNeighbors(c.x, c.y);
			try {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP: // move up
					if (n[0].isWalkable()) {
						c.move(n[0]);
						doGameCycle();
					}
					break;
				case KeyEvent.VK_RIGHT: // move right
					if (n[1].isWalkable()) {
						c.move(n[1]);
						doGameCycle();
					}
					break;
				case KeyEvent.VK_DOWN: // move down
					if (n[2].isWalkable()) {
						c.move(n[2]);
						doGameCycle();
					}
					break;
				case KeyEvent.VK_LEFT: // move left
					if (n[3].isWalkable()) {
						c.move(n[3]);
						doGameCycle();
					}
					break;
				case KeyEvent.VK_I: // open inventory
					c.getInventoryGUI().setVisible((!c.getInventoryGUI().isVisible()));
					Constants.GAME_FRAME.repaint();
					break;
				case KeyEvent.VK_SPACE: // collect items
					c.detection(c.getLocatedAt());
					doGameCycle();
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
	}

	private final class GameboardMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (c.getInventoryGUI().mouseClicked(e)) // checks if the inventory receives this event
				return;
			if (checkComponents(e.getPoint())) // check for active components
				return;

			// when the event doesn't belong to any component the character moves
			// get destination Tile
			final Tile tile = getFromDisplayLocation(e.getPoint());
			if (tile.isWalkable())
				// if there is an enemy(Fightable content) nearby, attack it
				if (isFightableNeighbour(c.getLocatedAt(), tile)) {
					tile.hit(c.attack());
					doGameCycle();
				} else // move normally
					setPlayerMovePath(tile);
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
		public void mousePressed(MouseEvent e) {
			// nothing happens
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// nothing happens
		}

		private boolean isFightableNeighbour(Tile start, Tile destination) {
			if (destination.hasHitableContent(c.getLocatedAt().getContents())
					&& NeighbourFinder.isNeighbor(start.x, start.y, destination.x, destination.y)) {
				return true;
			}
			return false;
		}

		private void setPlayerMovePath(Tile destination) {
			// configure pathfinder
			final PathFinderConfig pfc = new PathFinderConfig();
			// set to blacklist (mode)
			pfc.setDisallowed();
			// add Wall to blacklist
			pfc.addDest(Wall.class);
			try {
				c.addPath(new PathFinder(getTilegrid(), pfc).findPath(c.getLocatedAt(), destination));
				gameTimer.start();
			} catch (PathNotFoundException pnfe) {
				// Could not move
			}
		}
	}

	// static attributes
	private static Gameboard currentInstance;

	public static Gameboard getCurrentInstance() {
		return currentInstance;
	}

	private final IngameButton attackButton;

	private Player c;

	// attributes
	private final Timer gameTimer;

	private final DungeonGenerator level;

	private Tile[][] tilegridInFOV;

	public Gameboard() {
		gameTimer = new Timer(100, this);
		final WindowUtils buttonBounds = new WindowUtils(Constants.GAME_FRAME.getSize(), 0.1F, 0.1F, 1, -0.9F);
		attackButton = new IngameButton(buttonBounds.getWindowPosition(), // position
				new Dimension(Math.min(buttonBounds.getHeight(), buttonBounds.getWidth()), // width
						Math.min(buttonBounds.getHeight(), buttonBounds.getWidth())), // height
				TextureReader.getTextureByString("INVENTORY_TILE_NEW_WEAPON")); // texture

		// add listeners
		Constants.GAME_FRAME.addMouseListener(new GameboardMouseListener());
		Constants.GAME_FRAME.addKeyListener(new GameboardKeyListener());
		attackButton.addActionListener(new AttackButtonListener());

		// remove layout
		setLayout(null);
		// generate level
		LoadingScreen.setCurrentAction("generating Dungeon", (byte) 10);
		level = new DungeonGenerator();
		LoadingScreen.setCurrentAction("finishing", (byte) 100);
		setUp();
	}

	// called by Timer
	@Override
	public void actionPerformed(ActionEvent e) {
		doGameCycle();
	}

	public Tile getFromDisplayLocation(Point at) {
		// size is needed to calculate the tile position in the array from the absolute
		// location
		final double size = Math.ceil((Math.min(getWidth(), getHeight()) / ((double) Constants.RENDER_DISTANCE)));
		// translate frame position to tile position
		int x = (int) Math.floor(at.getX() / size), y = (int) Math.floor(at.getY() / size);

		// check if x is in bounds
		if (x >= tilegridInFOV.length)
			x = tilegridInFOV.length - 1;
		else if (x < 0)
			x = 0;

		// check if y is in bounds
		if (y >= tilegridInFOV[x].length)
			y = tilegridInFOV[x].length - 1;
		else if (y < 0)
			y = 0;

		// retrieve tile from field of view
		return tilegridInFOV[x][y];
	}

	/**
	 * Method to allocate player instance.
	 *
	 * @return current player instance of type Player
	 */
	public Player getPlayer() {
		return c;
	}

	public Tile getTileAt(int x, int y) {
		return level.getTileAt(x, y);
	}

	public Tile[][] getTilegrid() {
		return level.getTilegrid();
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

		attackButton.paint(g2d);

		for (Component comp : getComponents()) {
			comp.repaint();
		}
	}

	private boolean checkComponents(Point at) {
		for (UIElement element : getAllUIElements()) {
			if (element.contains(at)) {
				element.actionPerformed(new ActionEvent(this, 0, "clicked"));
				return true;
			}
		}
		return false;
	}

	/**
	 * does everything that needs to be done in a turn (move enemies, repaint, etc)
	 */
	private void doGameCycle() {
		if (!c.moveStep())
			gameTimer.stop();
		// move enemys
		EnemyController.getInstance().moveEnemies();
		// check if something can be attacked
		boolean changed = false;
		for (Tile t : NeighbourFinder.findNeighbors(c.getLocatedAt().x, c.getLocatedAt().y))
			if (t.hasHitableContent(c)) {
				attackButton.setVisible(true);
				changed = true;
				break;
			}
		if (!changed)
			attackButton.setVisible(false);

		// repaint the screen
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

	private List<UIElement> getAllUIElements() {
		List<UIElement> elements = new ArrayList<>();
		elements.add(attackButton);
		return elements;
	}

	/**
	 * needs to be called every time a Gameboard is loaded. Not a constructor cause
	 * already existing levels can be revisited.
	 */
	private void setUp() {
		currentInstance = this;
		// add enemys
		EnemyController.getInstance().setEnemyCount(10);
		c = level.getPlayer();
		c.getInventoryGUI().setVisible(false);
	}
}
