package main;

import main.UI.Gameboard;
import main.UI.Menu;
import main.entitiys.Player;
import main.entitiys.items.ItemReader;
import textures.TextureReader;
import utils.WindowUtils;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class Main implements ActionListener {

	// constants
	private static final float scalefactor = 0.8F;

	// static variables
	private static WindowUtils bounds;

	// class variables
	private final Timer t;
	private final JFrame f;
	private Gameboard board;

	// constructor
	public Main() {
		new ItemReader();
		new TextureReader();
		// window settings
		bounds = new WindowUtils(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
						.getBounds().width,
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
						.getBounds().height,
				scalefactor, scalefactor);

		// JFrame settings
		f = Constants.GAME_FRAME;
		f.setLocation(bounds.getWindowPosition());
		f.setSize(bounds.getWindowDimensions());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// f.setResizable(false);
		f.setVisible(true);

		/*
		 * adding the Menue with 'startGame' as parameter for it to start the Game once
		 * the 'start' button is pressed
		 */
		f.add(new Menu(o -> startGame(o)));

		// animation Timer for repainting the JFrame therefore updating .gif images
		t = new Timer(100, this);
		t.start();
	}

	/**
	 * @return an object of type Dimension with the size(resolution) of the game as
	 *         width and height component
	 */
	public static WindowUtils getGameDimension() {
		return bounds;
	}

	/**
	 * @return the current object of type Player. This is the only instance of this
	 *         class at runtime therefore this method is valid
	 */
	public static Player getPlayer() throws NullPointerException {
		final Player mainChar = Gameboard.getCurrentInstance().getPlayer();
		if (mainChar != null) {
			return mainChar;
		}
		throw new NullPointerException("Player was not generated yet!");
	}

	// program start
	public static void main(String[] args) {
		new Main();
	}

	/**
	 * repaints the JFrame and all its' components on methods call
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		f.revalidate();
		f.repaint();
	}

	/**
	 * @param j removes j from JFrame and adds a new instance of Gameboard and sets
	 *          it a board (accessible with "getGameDimension()")
	 */
	private void startGame(JComponent j) {
		f.remove(j);
		board = new Gameboard();
		board.addActionListener(this);
		f.addKeyListener(board);
		t.stop();
		f.add(board);
		f.revalidate();
		f.repaint();
		f.revalidate();
		f.repaint();
	}

	/**
	 * @return the frames ContentPane
	 */
	public Container getContentPane() {
		return f.getContentPane();
	}
}
