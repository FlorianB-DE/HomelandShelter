package textures;

import java.awt.Image;

public final class Texture implements Comparable<String> {
	private final String name;
	private final Image content;

	public Texture(String name, Image i) {
		this.name = name;
		content = i;
	}

	public String getName() {
		return name;
	}

	public Image getContent() {
		return content;
	}

	@Override
	public int compareTo(String o) {
		return name.compareTo(o);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Texture)
			return ((Texture) obj).compareTo(this.getName()) == 0 ? true : false;
		return false;
	}
}
