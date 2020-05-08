package main.UI.elements;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class UIElement extends Rectangle {

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
	
	public abstract void paint(Graphics2D g);

}
