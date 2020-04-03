package textures;

import java.net.URL;
import javax.swing.ImageIcon;

public enum Textures {
	WALL,
	CHAR,
	PATH,
	DOOR,
	FIRE,
	ENEMY;
	
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
				System.out.println("Cannot find \"" + toString() + ".png\" or \"" + toString() + ".gif\" at \"" + url + "\"");
			}
		}
		return img;
	}
}
