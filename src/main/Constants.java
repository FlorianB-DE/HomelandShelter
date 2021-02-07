package main;

import main.ui.GameFrame;

import javax.swing.*;

public abstract class Constants {
    public static final int DUNGEON_SIZE = 100; // square this number to get the total amount if Tiles
    public static final GameFrame GAME_FRAME = new GameFrame();
    public static final boolean FULLSCREEN = false;
    public static final String ITEM_SOURCE_FILE = "Items.txt";
    public static final String LOOT_TABLE_SOURCE_FILE = "LootTables.txt";
    public static final double MAX_PLAYER_HEALTH = 90.0;
    public static final double PLAYER_HEALTH_GROWTH = 10.0;
    public static final int PLAYER_INVENTORY_SIZE = 20;
    public static final int PLAYER_INVENTORY_TILES_PER_ROW = 4;
    public static final float RENDER_DISTANCE = 10F;
    public static final int TARGET_FPS = 60;
    public static final String TITLE = "Homeland Shelter";
    public static final Timer GAME_TIMER = new Timer(TARGET_FPS == 0 ? 0 : 1000 / TARGET_FPS, e -> GAME_FRAME.repaint());
}
