package main.ui.elements;

import textures.Texture;
import textures.TextureReader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PercentageBar extends UIElement {

    private static final float xOffset = 0.15F;
    private final Color color;
    private final IGetValues values;
    private final Texture fillTexture;

    public PercentageBar(int x, int y, int width, int height, IGetValues values, String color) {
        super(x, y, width, height);
        setTexture(TextureReader.getTextureByString("PERCENTAGE_BAR"));
        this.color = Color.decode(color);
        this.values = values;
        this.fillTexture = null;
    }

    public PercentageBar(int x, int y, int width, int height, IGetValues values, Texture fillTexture) {
        super(x, y, width, height);
        setTexture(TextureReader.getTextureByString("PERCENTAGE_BAR"));
        this.color = null;
        this.values = values;
        this.fillTexture = fillTexture;
    }

    @Override
    public void paint(Graphics2D g) {
        int percentageWidth = Math.round((((float) values.getValue()) / values.getMax()) * width);
        percentageWidth -= Math.round(percentageWidth * (xOffset * 0.4F));
        final int fixedXPos = Math.round((x * xOffset) + x);
        if (color == null)
            g.drawImage(fillTexture.getContent().getImage(), x, y, width, height, null);
        else {
            final Color oldColor = g.getColor();
            g.setColor(color);
            g.fillRect(fixedXPos, y, percentageWidth, height);
            g.setColor(oldColor);
        }
        super.paint(g);
    }

    @Override
    public void addActionListener(ActionListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public interface IGetValues {
        int getValue();

        int getMax();
    }


}
