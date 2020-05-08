package main.UI;

import utils.Callback;
import utils.WindowUtils;

import javax.swing.JComponent;
import javax.swing.JPanel;

import main.UI.elements.MenuElement;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * TODO
 *
 * @author Florian M. Becker
 * @version 1.0 06.04.2020
 */
public final class Menu extends JPanel implements MouseListener {
	private String[] names = { "title", "START", "SETTINGS", "EXIT" };
	private MenuElement[] uielements = new MenuElement[names.length];

	private Callback<JComponent> callback;

	public Menu() {
	}

	public Menu(Callback<JComponent> callback) {
		addMouseListener(this);
		this.callback = callback;
		for (int i = 0; i < uielements.length; i++)
			uielements[i] = new MenuElement(names[i], new Point(), new Dimension());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (MenuElement element : uielements) {
			if (element.contains(e.getPoint())) {
				switch (element.getName()) {
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
	public void mouseEntered(MouseEvent e) {
		// not used

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// not used

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// not used

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// not used

	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		WindowUtils bounds = new WindowUtils(getSize(), 0.6F, 0.15F, 0, -0.9F);
		for (MenuElement element : uielements) {
			element.setLocation(bounds.getWindowPosition());
			element.setSize(bounds.getWindowDimensions());
			element.setMousePosition(getMousePosition());
			element.paint(g2d);
			bounds.setWidthFactor(0.4F);
			bounds.setVerticalOffset(bounds.getVerticalOffset() + 0.5F);
		}
	}
}
