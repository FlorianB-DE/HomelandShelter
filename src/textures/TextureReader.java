package textures;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * TODO
 *
 * @author Florian M. Becker
 * @version 1.0
 * <p>
 * Our special thanks goes to Melvin Lannatewitz for creating these
 * amazing sprites and textures
 */
public class TextureReader {
    public static final List<Texture> textures = new ArrayList<>();

    public TextureReader() {
        final String path = "textures";
        final File jarFile = new File(
                getClass().getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/", ""));
        try {
            if (jarFile.isFile()) { // executes when exported
                // open jar file
                final JarFile jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries(); // retrieve entries (in this case folders)
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName(); // retrieve name of the folder
                    if (name.startsWith(path) && (name.endsWith(".png") || name.endsWith(".gif")))
                        // if its image resources in /path
                        textures.add(new Texture( // adds texture
                                // name only without path or suffix
                                name.replace(path + "/", "").replace(".gif", "").replace(".png", ""), new ImageIcon( // content
                                ImageIO.read(getClass().getResourceAsStream( // resource name
                                        name.replace(path + "/", ""))))));// input stream

                }
                jar.close();

            } else { // executes when in IDE
                File dir = new File(getClass().getResource("").getPath()); // returns path
                // iterates all images located in path
                for (File f : (dir.listFiles((dir1, name) -> name.endsWith(".png") || name.endsWith(".gif"))))
                    // add texture to list
                    textures.add(new Texture(f.getName().replace(".gif", "").replace(".png", ""),
                            new ImageIcon(f.toURI().toURL())));
            }
        } catch (IOException e) {
            System.err.println("could not load images");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * @param name of the Image without file suffix (e.g. ".gif" etc.)
     * @return the Texture with set name and null when there is no such Texture
     */
    public static Texture getTextureByString(String name) {
        try {
            return textures.get(textures.indexOf(new Texture(name, null)));
        } catch (Exception e) {
            return null;
        }
    }
}
