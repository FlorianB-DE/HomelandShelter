package main.UI.elements;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import textures.TextureReader;

public final class MenuElement extends UIElement {
	private String name;
	private Point mousePosition;

	public MenuElement(String name, Point origin, Dimension size) {
		super(origin, size);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void paint(Graphics2D g) {
		if (name != "title") {
			if (contains(mousePosition)) {
				g.drawImage(TextureReader.getTextureByString(name + "_BUTTON_PRESSED").getContent().getImage(), x, y,
						width, height, null);
			} else {
				g.drawImage(TextureReader.getTextureByString(name + "_BUTTON").getContent().getImage(), x, y, width,
						height, null);
			}
		} else {
			g.fillRect(x, y, width, height);
		}
	}

	public void setMousePosition(Point mousePosition) {
		if (mousePosition != null)
			this.mousePosition = mousePosition;
		else
			this.mousePosition = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
	}
}
