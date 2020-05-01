package textures;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * TODO
 * 
 * @author Florian M. Becker
 * @version 1.0
 * 
 *          Our special thanks goes to Melvin Lannatewitz for creating these
 *          amazing sprites and textures
 */
public class TextureReader {
	public static final List<Texture> textures = new ArrayList<>();

	public TextureReader() {
		File dir = new File(getClass().getResource("").getPath());
		File[] images = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".png") || name.endsWith(".gif");
			}
		});

		for (File file : images) {
			try {
				textures.add(new Texture(file.getName().replace(".png", "").replace(".gif", ""), new ImageIcon(file.toURI().toURL()).getImage()));
			} catch (IOException e) {
				// do nothing
			}
		}
	}

	public static Texture getTextureByString(String name) {
		try {
			return textures.get(textures.indexOf(new Texture(name, null)));
		} catch (Exception e) {
			return null;
		}
	}
}