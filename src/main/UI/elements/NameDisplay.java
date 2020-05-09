package main.UI.elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

final class NameDisplay extends UIElement {

	private String displayText;

	public NameDisplay(int width, int height) {
		super(width, height);
		setVisible(false);
	}

	public void setDisplayText(String text) {
		this.displayText = text;
	}

	@Override
	public void paint(Graphics2D g) {
		final Font prevFont = g.getFont();
		final Font newFont = new Font(prevFont.getName(), Font.PLAIN, (int) Math.round(prevFont.getSize() * 1.5));
		final int strHeight = newFont.getSize() + 2;
		final int strWidth = strHeight * (displayText.length() - 2);
		final Color prevColor = g.getColor();
		g.setColor(Color.white);
		g.fillRect(x, y, strWidth, strHeight);
		g.setColor(Color.black);
		g.setFont(newFont);
		g.drawString(displayText, x + 1, Math.round(y + strHeight * 0.8));
		g.setFont(prevFont);
		g.setColor(prevColor);
	}

}
