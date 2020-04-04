package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import main.UI.*;
import main.entitiys.Character;
import utils.*;

public class Main implements ActionListener {
	private JFrame f;
	private WindowUtils bounds;
	private Timer t;

	private final String title = "Pixel Dungeon";
	private final float scalefactor = 0.8F;
	private static Gameboard board = null;

	/**
	 * 
	 * 
	 */
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		// window settings

		bounds = new WindowUtils(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
						.getBounds().width,
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
						.getBounds().height,
				scalefactor, scalefactor);
		f = new JFrame(title);
//		f.add(new Menue(o -> startGame(o)));

		f.setLocation(bounds.getWindowPosition());
		f.setSize(bounds.getWindowDimensions());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new JPanel() {
			@Override
			public void paint(Graphics g) {
				final int scalefactor = 50;
				int maxW = f.getWidth() + 1;
				int maxH = f.getHeight() + 1;
				for (int i = 0; i < maxW; i += scalefactor) {
					for (int j = 0; j < maxH; j += scalefactor) {
						int num = (int) (((MathUtils.perlinNoise(i * 0.075, j * 0.075, 0.8) + 1) * 0.5) * 255);
						g.setColor(new Color(0, 0, 0, num));
						g.fillRect(i, j, scalefactor, scalefactor);
					}
				}
			}
		});
		//f.setResizable(false);
		f.setVisible(true);
		t = new Timer(100, this);
		t.start();
	}

	double average(double[] values) {
		double avg = 0;
		for (int i = 0; i < values.length; i++)
			avg += values[i] / values.length;
		return avg;
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
	public static Character getPlayer() {
		if (board != null)
			return board.getPlayer();
		return null;
	}

	/**
	 * @return an object of type Dimension with the size(resolution) of the game as
	 *         width and height component
	 */
	public static Dimension getGameDimension() {
		return board.getSize();
	}
}
