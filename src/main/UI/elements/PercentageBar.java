package main.UI.elements;

import textures.TextureReader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PercentageBar extends UIElement{

    public interface getValues{
        int getValue();
        int getMax();
    }

    public PercentageBar(int x, int y, int width, int height, getValues values) {
        super(x, y, width, height);
        setTexture(TextureReader.getTextureByString("PERCENTAGE_BAR"));
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
    }

    @Override
    public void addActionListener(ActionListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void actionPerformed(ActionEvent e) {}


}
