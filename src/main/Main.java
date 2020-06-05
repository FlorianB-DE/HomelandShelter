package main;

import main.UI.Gameboard;
import main.UI.Menu;
import main.entitys.Player;
import main.entitys.items.ItemReader;
import textures.TextureReader;

public final class Main{
	
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

	// constructor
	public Main() {
		// resource importers
		new ItemReader();
		new TextureReader();
		//new LoottableReader();

		/*
		 * adding the Menue with 'startGame' as parameter for it to start the Game once
		 * the 'start' button is pressed
		 */
		Constants.GAME_FRAME.add(new Menu());
	}
}
