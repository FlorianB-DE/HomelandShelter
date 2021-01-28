package main.ui.elements;

import textures.Texture;

import java.awt.*;

public class InGameButton extends UIElement {

    private Texture texture;

    public InGameButton(int x, int y, int width, int height, Texture texture) {
        super(x, y, width, height);
        this.texture = texture;
        setVisible(false);
    }

    public InGameButton(Point location, Dimension size, Texture texture) {
        super(location, size);
        this.texture = texture;
        setVisible(false);
    }

    @Override
    public void paint(Graphics2D g) {
        if (isVisible() && texture != null)
            g.drawImage(texture.getContent().getImage(), x, y, width, height, null);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
