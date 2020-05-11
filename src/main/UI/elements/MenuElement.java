package main.UI.elements;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import textures.TextureReader;

public final class MenuElement extends UIElement {
	private Point mousePosition;
	private String name;

	public MenuElement(String name, Point origin, Dimension size) {
		super(origin, size);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void paint(Graphics2D g) {
		if (name != "title") {
			if (contains(mousePosition)) {
				setTexture(TextureReader.getTextureByString(name + "_BUTTON_PRESSED"));
				super.paint(g);
			} else {
				setTexture(TextureReader.getTextureByString(name + "_BUTTON"));
				super.paint(g);
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

	public void setName(String name) {
		this.name = name;
	}
}
