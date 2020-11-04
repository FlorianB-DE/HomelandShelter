package main.UI.elements;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import textures.Texture;

public class IngameButton extends UIElement {

	private Texture texture;

	public IngameButton(int x, int y, int width, int height, Texture texture) {
		super(x, y, width, height);
		this.texture = texture;
		setVisible(false);
	}

	public IngameButton(Point location, Dimension size, Texture texture) {
		super(location, size);
		this.texture = texture;
		setVisible(false);
	}

	@Override
	public void paint(Graphics2D g) {
		if (isVisible() && texture != null)
			g.drawImage(texture.getContent().getImage(), x, y, width, height, null);
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
