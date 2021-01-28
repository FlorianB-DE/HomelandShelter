package main.ui;

import main.Constants;
import utils.WindowUtils;

import javax.swing.*;
import java.awt.*;

public final class LoadingScreen extends JPanel {

    private static String currentAction;
    private static byte currentPercentage;
    private final WindowUtils bounds;

    public LoadingScreen() {
        bounds = new WindowUtils(Constants.GAME_FRAME.getSize(), 0.8F, 0.1F);
        currentAction = "loading...";
        currentPercentage = 0;
        setLayout(null);
    }

    /**
     * @return the currentAction
     */
    public static String getCurrentAction() {
        return currentAction;
    }

    /**
     * @param newAction the currentAction to set
     */
    public static void setCurrentAction(String newAction, byte percentage) {
        currentAction = newAction;
        currentPercentage = percentage;
        Constants.GAME_FRAME.repaint();
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
}
