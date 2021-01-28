package main.ui.elements;

import java.awt.*;

final class NameDisplay extends UIElement {

    private Color backgroundColor;
    private String displayText;

    public NameDisplay(int width, int height) {
        super(width, height);
        setVisible(false);
        backgroundColor = Color.white;
    }

    @Override
    public void paint(Graphics2D g) {
        final Font prevFont = g.getFont();
        final Font newFont = new Font(prevFont.getName(), Font.PLAIN, (int) Math.round(prevFont.getSize() * 1.5));
        final int strHeight = newFont.getSize() + 2;
        final int strWidth = Math.round(g.getFontMetrics(newFont).stringWidth(displayText) * 1.05F);
        final Color prevColor = g.getColor();
        g.setColor(backgroundColor);
        if (getTexture() == null)
            g.fillRect(x, y, strWidth, strHeight);
        else
            super.paint(g);
        g.setColor(Color.black);
        g.setFont(newFont);
        g.drawString(displayText, x + 1, Math.round(y + strHeight * 0.8));
        g.setFont(prevFont);
        g.setColor(prevColor);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setDisplayText(String text) {
        this.displayText = text;
    }
}
