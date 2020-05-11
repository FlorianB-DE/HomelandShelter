package main;

import javax.swing.JFrame;

public abstract class Constants {
	public static final int PLAYER_INVENTORY_SIZE = 20;
	public static final int DUNGEON_SIZE = 100;	//square this number to get the total amount if Tiles
	public static final int PLAYER_INVENTORY_TILES_PER_ROW = 4;
	public static final float RENDER_DISTANCE = 10F;
	public static final double MAX_PLAYER_HEALTH = 100.0;
	
	public static final String TITLE = "Homeland Shelter";
	public static final String ITEM_SOURCE_FILE = "Items.txt";
	public static final JFrame GAME_FRAME = new JFrame(TITLE);
}
