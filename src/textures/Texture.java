package textures;

import javax.swing.ImageIcon;

public final class Texture implements Comparable<String> {
	private final ImageIcon content;
	private final String name;

	public Texture(String name, ImageIcon i) {
		this.name = name;
		content = i;
	}

	@Override
	public int compareTo(String o) {
		return name.compareTo(o);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Texture)
			return ((Texture) obj).compareTo(this.getName()) == 0;
		return false;
	}

	public ImageIcon getContent() {
		return content;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "name: " + name + "; ImageIcon: " + content.toString();
	}
}
