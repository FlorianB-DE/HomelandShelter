package main.UI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import textures.Textures;
import utils.Callback;
import utils.WindowUtils;

/**
 * TODO
 * @author Florian M. Becker
 * @version 0.9 06.04.2020
 */
public class Menue extends JPanel implements MouseListener {
	private String[] names = { "title", "start", "options", "exit" };
	private UIElement[] uielements = new UIElement[names.length];

	private Callback<JComponent> callback;

	public Menue() {
	}

	public Menue(Callback<JComponent> callback) {
		addMouseListener(this);
		this.callback = callback;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		WindowUtils bounds = new WindowUtils(getSize(), 0.6F, 0.15F, 0, -0.9F);
		for (int i = 0; i < uielements.length; i++) {
			uielements[i] = new UIElement(names[i], bounds.getWindowPosition(), bounds.getWindowDimensions());
			uielements[i].paint(g2d);
			bounds.setWidthFactor(0.4F);
			bounds.setVerticalOffset(bounds.getVerticalOffset() + 0.5F);
		}
		g2d.drawImage(Textures.FIRE.loadImage().getImage(), 200, 200, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (UIElement element : uielements) {
			if (element.contains(e.getPoint())) {
				switch (element.name) {
				case "start":
					callback.call(this);
					break;
				case "options":
					/* TODO */ break;
				case "exit":
					System.exit(0);
				}
				break;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private class UIElement extends Rectangle {
		private String name;

		public UIElement(String name, Point origin, Dimension size) {
			super(origin, size);
			this.name = name;
		}

		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}

		@SuppressWarnings("unused")
		public void setName(String name) {
			this.name = name;
		}

		public void paint(Graphics2D g) {
			g.fillRect(x, y, width, height);
		}
	}
}
