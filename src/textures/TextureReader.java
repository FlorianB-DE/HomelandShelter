package textures;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;
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
		final String path = "textures";
		final File jarFile = new File(
				getClass().getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/", ""));
		try {
			if (jarFile.isFile()) { // executes when exported

				final JarFile jar;
				jar = new JarFile(jarFile);
				final Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					final String name = entries.nextElement().getName();
					if (name.startsWith(path) && (name.endsWith(".png") || name.endsWith(".gif")))
						textures.add(new Texture( // adds texture
								name.replace(path + "/", "").replace(".gif", "").replace(".png", ""), // name
								new ImageIcon( // content
										ImageIO.read(getClass().getResourceAsStream( // resource name
												name.replace(path + "/", ""))))));// input stream

				}
				jar.close();

			} else { // executes when in ide
				File dir = new File(getClass().getResource("").getPath());
				for (File f : (dir.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".png") || name.endsWith(".gif");
					}
				})))

					textures.add(new Texture(f.getName().replace(".gif", "").replace(".png", ""),
							new ImageIcon(f.toURI().toURL())));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return;
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
