package main.UI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Constants;
import utils.WindowUtils;

public final class LoadingScreen extends JPanel {

	private final WindowUtils bounds;
	private static String currentAction;
	private static byte currentPercentage;

	public LoadingScreen() {
		bounds = new WindowUtils(Constants.GAME_FRAME.getSize(), 0.8F, 0.1F);
		currentAction = "loading...";
		currentPercentage = 0;
		setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		bounds.resetOffset();
		final Color prev = g.getColor();
		g.setColor(Color.black);
		g.drawRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		g.setColor(Color.green);
		g.fillRect(bounds.getX() + 1, bounds.getY() + 1,
				Math.round((bounds.getWidth() - 1) * (currentPercentage / 100F)), bounds.getHeight() - 1);
		bounds.setHorizontalOffset(-0.25F);
		final int offset = Math.round(g.getFontMetrics().stringWidth(currentAction) / 2F);
		g.setColor(Color.black);
		g.drawString(currentAction, Math.round(getWidth() / 2F) - offset, bounds.getY());
		g.setColor(prev);
	}

	/**
	 * @return the currentAction
	 */
	public static String getCurrentAction() {
		return currentAction;
	}

	/**
	 * @param currentAction the currentAction to set
	 */
	public static void setCurrentAction(String newAction, byte percentage) {
		currentAction = newAction;
		currentPercentage = percentage;
		Constants.GAME_FRAME.repaint();
	}
}
