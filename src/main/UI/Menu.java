package main.UI;

import main.Constants;
import main.UI.elements.MenuElement;
import utils.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * TODO
 *
 * @author Florian M. Becker
 * @version 1.0 06.04.2020
 */
public final class Menu extends JPanel implements MouseListener, ActionListener {
	private String[] names = { "title", "START", "SETTINGS", "EXIT" };
	private final Timer refreshTimer;
	private MenuElement[] uielements = new MenuElement[names.length];

	public Menu() {
		refreshTimer = new Timer(100, this);
		refreshTimer.start();
		addMouseListener(this);
		for (int i = 0; i < uielements.length; i++)
			uielements[i] = new MenuElement(names[i], new Point(), new Dimension());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Constants.GAME_FRAME.revalidate();
		Constants.GAME_FRAME.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (MenuElement element : uielements) {
			if (element.contains(e.getPoint())) {
				switch (element.getName()) {
				case "START":
					refreshTimer.stop();
					final JFrame gameFrame = Constants.GAME_FRAME;
					gameFrame.remove(this);
					final LoadingScreen l = new LoadingScreen();
					gameFrame.add(l);
					gameFrame.revalidate();
					gameFrame.repaint();

					try {
						gameFrame.add(new Gameboard());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					gameFrame.remove(l);
					gameFrame.revalidate();
					gameFrame.repaint();

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
			bounds.setHorizontalOffset(bounds.getHorizontalOffset() + 0.5F);
		}
	}
}
