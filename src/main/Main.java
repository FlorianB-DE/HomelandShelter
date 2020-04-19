package main;

import main.UI.Gameboard;
import main.UI.Menue;
import utils.WindowUtils;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	}
	
	/**
	 * @return an object of type Dimension with the size(resolution) of the game as
	 *         width and height component
	 */
	public static Dimension getGameDimension() {
		return bounds.getWindowDimensions();
	}
}
