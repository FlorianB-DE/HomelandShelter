package main.UI.elements;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import textures.TextureReader;

public final class UIElement extends Rectangle {
	private String name;

	public UIElement(String name, Point origin, Dimension size) {
		super(origin, size);
		this.name = name;
	}

	 public String getName() {
	 return name;
	 }
	
	 public void setName(String name) {
	 this.name = name;
	 }

	public void paint(Graphics2D g, Point mousePos) {
		if (name != "title") {
			if (contains(mousePos)) {
				g.drawImage(TextureReader.getTextureByString(name + "_BUTTON_PRESSED").getContent().getImage(), x, y, width,
						height, null);
			} else {
				g.drawImage(TextureReader.getTextureByString(name + "_BUTTON").getContent().getImage(), x, y, width, height,
						null);
			}
		} else {
			g.fillRect(x, y, width, height);
		}
	}
}
