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

	private List<ActionListener> aListener = new ArrayList<>();
	private boolean isVisible;
	private Texture texture;

	public UIElement(Dimension size) {
		super(size);
	}

	public UIElement(int width, int height) {
		super(width, height);
	}

	public UIElement(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public UIElement(Point p, Dimension d) {
		super(p, d);
	}

	public UIElement(Rectangle r) {
		super(r);
	}

	public void actionPerformed(ActionEvent e) {
		for (ActionListener listener : aListener) {
			listener.actionPerformed(e);
		}
	}

	public void addActionListener(ActionListener listener) {
		aListener.add(listener);
	}

	public Texture getTexture() {
		return texture;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void paint(Graphics2D g) {
		if (texture != null)
			g.drawImage(texture.getContent().getImage(), x, y, width, height, null);
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
