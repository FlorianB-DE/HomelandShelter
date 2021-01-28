package main.ui.elements;

import main.entities.items.Item;
import textures.Texture;
import textures.TextureReader;
import utils.exceptions.NoSuchAttributeException;

import java.awt.*;

public final class InventoryElement extends UIElement {

    private static final Texture defaultTexture = TextureReader.getTextureByString("INVENTORY_TILE_NEW");
    private final NameDisplay displayName;
    private Item content;

    public InventoryElement(int x, int y, int size) {
        this(x, y, size, defaultTexture);
    }

    public InventoryElement(int x, int y, int size, Texture texture) {
        super(x, y, size, size);
        displayName = new NameDisplay(width, height / 3);
        setTexture(texture);
    }

    public void displayContentName(Point location) {
        if (content != null) {
            displayName.setLocation(location.x + width / 10, location.y);
            displayName.setVisible(true);
        }
    }

    public Item getContent() {
        return content;
    }

    public void setContent(Item content) {
        this.content = content;
        if (content != null)
            try {
                //displayName.setDisplayText(Item.getAttributeByString(content, "name", String.class));
                displayName.setDisplayText(content.toString());
            } catch (NoSuchAttributeException e) {
                displayName.setDisplayText("ERROR");
            }
    }

    @Override
    public void paint(Graphics2D g) {
        if (content != null) {
            final Texture prev = getTexture();
            setTexture(defaultTexture);
            super.paint(g);
            setTexture(prev);
            content.show(g, x, y);
        } else
            super.paint(g);
    }

    public void paintNameDisplay(Graphics2D g) {
        if (displayName.isVisible())
            displayName.paint(g);
    }

    public Item removeContent() {
        Item returnContent = content;
        content = null;
        removeNameDisplay();
        return returnContent;
    }

    /**
     * same as "displayName.setVisible(false)"
     */
    public void removeNameDisplay() {
        displayName.setVisible(false);
    }
}