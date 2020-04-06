package textures;

import java.net.URL;
import javax.swing.ImageIcon;

/**
 * TODO
 * 
 * @author Florian M. Becker
 * @version 1.0 05.04.2020 
 * 
 * Our special thanks goes to Melvin Lannatewitz for
 * creating these amazing sprites and textures
 */
public enum Textures {
	CHAR, DOOR, ENEMY, FIRE, PATH, WALL, WALL2;

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
