package textures;

import java.net.URL;
import javax.swing.ImageIcon;


public enum Textures {
	CHAR, DOOR, DOWNSTAIR_LEFT, DOWNSTAIR_RIGHT, ENEMY, EXIT_BUTTON, EXIT_BUTTON_PRESSED, INVENTORY_BACKGROUND,
	INVENTORY_TILE, LEFT_DOOR, FIRE, SETTINGS_BUTTON, SETTINGS_BUTTON_PRESSED, START_BUTTON, START_BUTTON_PRESSED,
	UPSTAIR_LEFT, UPSTAIR_RIGHT, PATH, RIGHT_DOOR, WALL, WALL2;

	public ImageIcon loadImage() {
		ImageIcon img = null;
		URL url = getClass().getResource(toString() + ".png");
		try {
			img = new ImageIcon(url);
		} catch (Exception e) {
			url = getClass().getResource(toString() + ".gif");
			try {
				img = new ImageIcon(url);
			} catch (Exception e1) {
				System.out.println(
						"Cannot find \"" + toString() + ".png\" or \"" + toString() + ".gif\" at \"" + url + "\"");
			}
		}
		return img;
	}
}
