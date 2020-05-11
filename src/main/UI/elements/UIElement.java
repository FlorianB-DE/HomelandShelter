package main.UI.elements;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import textures.Texture;

public abstract class UIElement extends Rectangle {

	private boolean isVisible;
	private List<ActionListener> aListener = new ArrayList<>();
	private Texture texture;

	public UIElement(Rectangle r) {
		super(r);
	}

	public UIElement(Point p, Dimension d) {
		super(p, d);
	}

	public UIElement(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public UIElement(int width, int height) {
		super(width, height);
	}

	public UIElement(Dimension size) {
		super(size);
	}

	public void paint(Graphics2D g) {
		if (texture != null)
			g.drawImage(texture.getContent().getImage(), x, y, width, height, null);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void addActionListener(ActionListener listener) {
		aListener.add(listener);
	}

	public void actionPerformed(ActionEvent e) {
		for (ActionListener listener : aListener) {
			listener.actionPerformed(e);
		}
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
