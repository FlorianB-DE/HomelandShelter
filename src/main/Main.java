package main;

import main.UI.Gameboard;
import main.UI.Menu;
import main.entitiys.Player;
import main.entitiys.items.ItemReader;
import textures.TextureReader;
import utils.WindowUtils;

import javax.swing.JFrame;
import java.awt.GraphicsEnvironment;

public final class Main{

	// constants
	private static final float scalefactor = 0.8F;

	// static variables
	private static WindowUtils bounds;

	// class variables
	private final JFrame f;

	// constructor
	public Main() {
		// resource importers
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
		f.add(new Menu());
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
}
