package main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

import main.entitiys.Character;
import main.UI.Gameboard;
import main.UI.Menue;
import main.core.DungeonGenerator;
import utils.Callback;
import utils.WindowUtils;

public class Main implements ActionListener {

	// constants
	private static final String title = "Pixel Bunker";
	private static final float scalefactor = 0.8F;

	// static variables
	private static WindowUtils bounds;

	// class variables
	private Timer t;
	private JFrame f;
	private Gameboard board;

	// program start
	public static void main(String[] args) {
		new Main();
	}

	// constructor
	public Main() {
		// window settings
		bounds = new WindowUtils(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
						.getBounds().width,
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
						.getBounds().height,
				scalefactor, scalefactor);

		// JFrame settings
		f = new JFrame(title);
		f.setLocation(bounds.getWindowPosition());
		f.setSize(bounds.getWindowDimensions());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// f.setResizable(false);
		f.setVisible(true);

		/* adding the Menue with 'startGame' as parameter for it to start the Game once
		 * the 'start' button is pressed
		 */
		f.add(new Menue(o -> startGame(o)));

		//animation Timer for repainting the JFrame therefore updating .gif images
		t = new Timer(100, this);
		t.start();
	}

	/**
	 * @return the frames ContentPane
	 */
	public Container getContentPane() {
		return f.getContentPane();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * repaints the JFrame and all its' components on methods call
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		f.repaint();
	}

	/**
	 * @param j removes j from JFrame and adds a new instance of Gameboard and sets
	 *          it a board (accessible with "getGameDimension()")
	 */
	private void startGame(JComponent j) {
		f.remove(j);
		board = new Gameboard();
		board.addActionListener(new Callback<ActionEvent>() {

			@Override
			public void call(ActionEvent o) {
				actionPerformed(o);

			}
		});
		t.stop();
		f.add(board);
		f.revalidate();
		f.repaint();
	}

	/**
	 * @return the current object of type Character. This is the only instance of
	 *         this class at runtime therefore this method is valid
	 */
	public static Character getPlayer() throws NullPointerException {
		if (DungeonGenerator.getPlayer() != null)
			return DungeonGenerator.getPlayer();
		throw new NullPointerException("Player was not generated yet!");
	}

	/**
	 * @return an object of type Dimension with the size(resolution) of the game as
	 *         width and height component
	 */
	public static Dimension getGameDimension() {
		return bounds.getBounds();
	}
}
