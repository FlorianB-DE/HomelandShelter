package main.UI;

import main.Main;
import main.core.DungeonGenerator;
import main.core.EnemyController;
import main.entitiys.Character;
import main.tiles.Door;
import main.tiles.Floor;
import main.tiles.RoomFloor;
import main.tiles.Tile;
import main.tiles.Wall;
import utils.Callback;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * TODO
 * 
 * @author Florian M. Becker and Tim Bauer
 * @version 1.0 06.04.2020
 */
public class Gameboard extends Menue {
	private static Tile[][] tilegrid;
	private Tile[][] tilegridInFOV;

	private Character c;
	private Callback<ActionEvent> repaint;

	public Gameboard() {
		addMouseListener(this);
		tilegridInFOV = new Tile[DungeonGenerator.SIZE / 10][];
		tilegrid = DungeonGenerator.generateDungeon();
		c = Main.getPlayer();
		EnemyController.getInstance().setEnemyCount(10);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int size = (int) (Math.ceil((Math.min(getWidth(), getHeight()) / (double) tilegridInFOV.length)));
		tilegridInFOV = new Tile[tilegridInFOV.length][(int) (Math
				.ceil((double) Math.max(getWidth(), getHeight()) / (double) size))];
		fetchTiles();
		for (int i = 0; i < tilegridInFOV.length; i++) {
			for (int j = 0; j < tilegridInFOV[0].length; j++) {
				if (tilegridInFOV[i][j] == null) {
					if (getWidth() > getHeight()) {
						tilegridInFOV[i][j] = new Wall(0, 0, size);
						tilegridInFOV[i][j].show(g2d, size * j, size * i);
					} else {
						tilegridInFOV[i][j] = new Wall(size * i, size * j, size);
						tilegridInFOV[i][j].show(g2d, size * i, size * j);
					}
				} else {
					if (getWidth() > getHeight()) {
						tilegridInFOV[i][j].setSize(size, size);
						tilegridInFOV[i][j].show(g2d, size * j, size * i);
					} else {
						tilegridInFOV[i][j].setSize(size, size);
						tilegridInFOV[i][j].show(g2d, size * i, size * j);
					}
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
		for (int i = 0; i < tilegrid.length; i++)
			for (int j = 0; j < tilegrid[0].length; j++)
				try {
					tilegrid[i][j].getPlayer().setLocation(i, j);
					;
				} catch (Exception e) {
					// Nothing
				}
		for (int i = 0; i < tilegridInFOV.length; i++) {
			for (int j = 0; j < tilegridInFOV[0].length; j++) {
				int ix = c.x + i - tilegridInFOV.length / 2;
				int iy = c.y + j - tilegridInFOV[0].length / 2;
				if (ix >= 0 && ix < tilegrid.length && iy >= 0 && iy < tilegrid[0].length)
					tilegridInFOV[i][j] = tilegrid[ix][iy];
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		double size = Math.ceil((Math.min(getWidth(), getHeight()) / (double) tilegridInFOV.length));
		int x, y;
		if (getWidth() > getHeight()) {
			y = (int) Math.floor(e.getX() / size);
			x = (int) Math.floor(e.getY() / size);
		} else {
			x = (int) Math.floor(e.getX() / size);
			y = (int) Math.floor(e.getY() / size);
		}
		Tile tile = tilegridInFOV[x][y];
		if ((tile instanceof RoomFloor || tile instanceof Door || tile instanceof Floor)) {
			if (tile instanceof Door)
				if (((Door) tile).isClosed())
					return;
				else {
					c.getLocatedAt().removeContent(c);
					tile.addContent(c);
					repaint.call(null);
					return;
				}
			c.move(tile);
			repaint.call(null);
			return;
		}
	}

	/**
	 * Needed to tell the JFrame to repaint without directly referring to the
	 * JFrame.
	 *
	 * @param e a reference to an actiobPerformed method
	 */
	public void addActionListener(Callback<ActionEvent> e) {
		repaint = e;
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
}
