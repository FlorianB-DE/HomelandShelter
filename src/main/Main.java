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
		f.add(new Menue(new Callback<JComponent>() {

			@Override
			public void call(JComponent o) {
				startGame(o);
			}
		}));

		f.setLocation(bounds.getWindowPosition());
		f.setSize(bounds.getWindowDimensions());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// f.setResizable(false);
		f.setVisible(true);
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
	 * @param j removes j from JFrame and adds a new instance of Gameboard and sets it a board 
	 * (accessible with "getGameDimension()")
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
	 * @return the current object of type Character. This is the only instance of this class at runtime 
	 * therefore this method is valid
	 */
	public static Character getPlayer() {
		if (board != null)
			return board.getPlayer();
		return null;
	}

	/**
	 * @return an object of type Dimension with the size(resolution) of the game as width and height component
	 */
	public static Dimension getGameDimension() {
		return board.getSize();
	}
}
