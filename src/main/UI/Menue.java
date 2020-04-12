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
 * 
 * @author Florian M. Becker
 * @version 1.0 06.04.2020
 */
public class Menue extends JPanel implements MouseListener {
	private String[] names = { "title", "START", "SETTINGS", "EXIT" };
	private UIElement[] uielements = new UIElement[names.length];

	private Callback<JComponent> callback;

	public Menue() {
	}

	public Menue(Callback<JComponent> callback) {
		addMouseListener(this);
		this.callback = callback;
		for (int i = 0; i < uielements.length; i++)
			uielements[i] = new UIElement(names[i], new Point(), new Dimension());
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		WindowUtils bounds = new WindowUtils(getSize(), 0.6F, 0.15F, 0, -0.9F);
		for (int i = 0; i < uielements.length; i++) {
			uielements[i].setLocation(bounds.getWindowPosition());
			uielements[i].setSize(bounds.getWindowDimensions());
			try {
				uielements[i].paint(g2d, getMousePosition());
			} catch (NullPointerException e) {
				uielements[i].paint(g2d, new Point(0, 0));
			}
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
				case "START":
					callback.call(this);
					break;
				case "OPTIONS":
					// TODO
					break;
				case "EXIT":
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

//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}

		public void paint(Graphics2D g, Point mousePos) {
			if (name != "title")
				if (contains(mousePos))
					g.drawImage(Textures.valueOf(name + "_BUTTON_PRESSED").loadImage().getImage(), x, y, width, height,
							null);
				else
					g.drawImage(Textures.valueOf(name + "_BUTTON").loadImage().getImage(), x, y, width, height, null);
			else
				g.fillRect(x, y, width, height);
		}
	}
}
