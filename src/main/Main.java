package main;

import main.entities.Player;
import main.entities.items.ItemReader;
import main.entities.items.LootTableReader;
import main.ui.GameBoard;
import main.ui.Menu;
import textures.TextureReader;

@SuppressWarnings("InstantiationOfUtilityClass")
public final class Main {

    // constructor
    public Main() {
        // resource importers
        new ItemReader();
        new TextureReader();
        new LootTableReader();

        /*
         * adding the Menu with 'startGame' as parameter for it to start the Game once
         * the 'start' button is pressed
         */
        Constants.GAME_FRAME.add(new Menu());
    }

    /**
     * @return the current object of type Player. This is the only instance of this
     * class at runtime therefore this method is valid
     */
    public static Player getPlayer() throws NullPointerException {
        final Player mainChar = GameBoard.getCurrentInstance().getPlayer();
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
