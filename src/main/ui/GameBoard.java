package main.ui;

import main.Constants;
import main.core.*;
import main.entities.Movable;
import main.entities.Player;
import main.tiles.Tile;
import main.tiles.Wall;
import main.ui.elements.InGameButton;
import main.ui.elements.PercentageBar;
import main.ui.elements.UIElement;
import textures.TextureReader;
import utils.WindowUtils;
import utils.exceptions.PathNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author Florian M. Becker and Tim Bauer
 * @version 1.0 06.04.2020
 */
public final class GameBoard extends JPanel implements ActionListener {
    // static attributes
    private static GameBoard currentInstance;
    private final Map<String, UIElement> uiElements;
    // attributes
    private final Timer gameTimer;
    private final DungeonGenerator level;
    private Player c;
    private Tile[][] tileGridInFOV;

    public GameBoard() {
        gameTimer = new Timer(100, this);
        final WindowUtils buttonBounds = new WindowUtils(Constants.GAME_FRAME.getSize(),
                0.1F, 0.1F, 1, -0.9F);

        final InGameButton attackButton = new InGameButton(buttonBounds.getWindowPosition(), // position
                new Dimension(Math.min(buttonBounds.getHeight(), buttonBounds.getWidth()), // width
                        Math.min(buttonBounds.getHeight(), buttonBounds.getWidth())), // height
                TextureReader.getTextureByString("INVENTORY_TILE_NEW_WEAPON")); // texture

        final WindowUtils barBounds = new WindowUtils(Constants.GAME_FRAME.getSize(),
                (Constants.GAME_FRAME.getWidth() / 6.5F) / Constants.GAME_FRAME.getWidth(),
                (Constants.GAME_FRAME.getWidth() / 10F) / Constants.GAME_FRAME.getWidth(),
                -0.95F, 0.8F);

        final PercentageBar healthBar = new PercentageBar(barBounds.getWindowPosition().x,
                barBounds.getWindowPosition().y,
                barBounds.getWidth(), barBounds.getHeight(), new PercentageBar.IGetValues() {
            @Override
            public int getValue() {
                return (int) Math.round(c.getHealth());
            }

            @Override
            public int getMax() {
                return (int) Math.round(c.getMaxHealth());
            }
        }, "#00ff00");
        barBounds.setVerticalOffset((float) (barBounds.getVerticalOffset() - 0.1));

        // add listeners
        Constants.GAME_FRAME.addMouseListener(new GameBoardMouseListener());
        Constants.GAME_FRAME.addKeyListener(new GameBoardKeyListener());
        attackButton.addActionListener(new AttackButtonListener());

        uiElements = new Hashtable<>(5);
        uiElements.put("attack-button", attackButton);
        uiElements.put("health-bar", healthBar);

        // remove layout
        setLayout(null);
        // generate level
        LoadingScreen.setCurrentAction("generating Dungeon", (byte) 10);
        level = new DungeonGenerator();
        LoadingScreen.setCurrentAction("finishing", (byte) 100);
        setUp();
        Constants.GAME_TIMER.start();
    }

    public static GameBoard getCurrentInstance() {
        return currentInstance;
    }

    // called by Timer
    @Override
    public void actionPerformed(ActionEvent e) {
        doGameCycle();
    }

    public Tile getFromDisplayLocation(Point at) {
        // size is needed to calculate the tile position in the array from the absolute location
        final double size = Math.ceil((Math.min(getWidth(), getHeight()) / ((double) Constants.RENDER_DISTANCE)));
        // translate frame position to tile position
        int x = (int) Math.floor(at.getX() / size), y = (int) Math.floor(at.getY() / size);

        // check if x is in bounds
        if (x >= tileGridInFOV.length)
            x = tileGridInFOV.length - 1;
        else if (x < 0)
            x = 0;

        // check if y is in bounds
        if (y >= tileGridInFOV[x].length)
            y = tileGridInFOV[x].length - 1;
        else if (y < 0)
            y = 0;

        // retrieve tile from field of view
        return tileGridInFOV[x][y];
    }

    /**
     * Method to allocate player instance.
     *
     * @return current player instance of type Player
     */
    public Player getPlayer() {
        return c;
    }

    public Tile getTileAt(int x, int y) {
        return level.getTileAt(x, y);
    }

    public Tile[][] getTileGrid() {
        return level.getTileGrid();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        final int size = (int) (Math.ceil((Math.min(getWidth(), getHeight()) / Constants.RENDER_DISTANCE)));

        tileGridInFOV = new Tile[(int) Math.ceil(getWidth() / (double) size)][(int) Math
                .ceil(getHeight() / (double) size)];
        fetchTiles();
        for (int i = 0; i < tileGridInFOV.length; i++) {
            for (int j = 0; j < tileGridInFOV[i].length; j++) {
                // fills with a Wall if screen exceeds tile grid
                if (tileGridInFOV[i][j] == null)
                    tileGridInFOV[i][j] = new Wall(0, 0, size, 0);
                else
                    tileGridInFOV[i][j].setSize(size, size);
                tileGridInFOV[i][j].show(g2d, size * i, size * j);
            }
        }

        for (UIElement uiElement : uiElements.values())
            uiElement.paint(g2d);

        for (Component comp : getComponents())
            comp.repaint();
    }

    private boolean checkComponents(Point at) {
        for (UIElement element : getAllUIElements()) {
            if (element.contains(at)) {
                element.actionPerformed(new ActionEvent(this, 0, "clicked"));
                return true;
            }
        }
        return false;
    }

    /**
     * does everything that needs to be done in a turn (move enemies, repaint, etc)
     */
    private void doGameCycle() {
        if (!c.moveStep())
            gameTimer.stop();
        // move enemies
        EnemyController.getInstance().moveEnemies();
        // check if something can be attacked
        boolean changed = false;
        for (Tile t : NeighbourFinder.findNeighbors(c.getLocatedAt().x, c.getLocatedAt().y))
            if (t == null) continue;
            else if (t.hasHittableContent(c)) {
                uiElements.get("attack-button").setVisible(true);
                changed = true;
                break;
            }
        if (!changed)
            uiElements.get("attack-button").setVisible(false);

        // repaint the screen
        //Constants.GAME_FRAME.repaint();
    }

    /**
     * Method for fetching the needed Tiles from the original Tiles Array with all
     * available Tiles. Needed because not all the Tiles are showed at the same
     * Time.
     */
    private void fetchTiles() {
        for (int i = 0; i < tileGridInFOV.length; i++) {
            for (int j = 0; j < tileGridInFOV[i].length; j++) {
                final int ix = c.x + i - tileGridInFOV.length / 2;
                final int iy = c.y + j - tileGridInFOV[i].length / 2;
                if (ix >= 0 && ix < Constants.DUNGEON_SIZE && iy >= 0 && iy < Constants.DUNGEON_SIZE) {
                    tileGridInFOV[i][j] = level.getTileAt(ix, iy);
                }
            }
        }
    }

    private List<UIElement> getAllUIElements() {
        return new ArrayList<>(uiElements.values());
    }

    /**
     * needs to be called every time a GameBoard is loaded. Not a constructor cause
     * already existing levels can be revisited.
     */
    private void setUp() {
        currentInstance = this;
        // add enemies
        EnemyController.getInstance().setEnemyCount(10);
        c = level.getPlayer();
        c.getInventoryGUI().setVisible(false);
    }

    private class AttackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (uiElements.get("attack-button").isVisible()) {
                final Tile[] neighbors = NeighbourFinder.findNeighbors(c.getLocatedAt().x, c.getLocatedAt().y);
                for (Tile tile : neighbors) {
                    if (tile.hasHittableContent(c)) {
                        tile.hit(c.attack());
                        Movable.setDirection(c, tile);
                        doGameCycle();
                        return;
                    }
                }
            }
        }
    }

    private class GameBoardKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            // Neighbor tiles
            final Tile[] n = NeighbourFinder.findNeighbors(c.x, c.y);
            try {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: // move up
                        if (n[0].isWalkable()) {
                            c.move(n[0]);
                            doGameCycle();
                        }
                        break;
                    case KeyEvent.VK_RIGHT: // move right
                        if (n[1].isWalkable()) {
                            c.move(n[1]);
                            doGameCycle();
                        }
                        break;
                    case KeyEvent.VK_DOWN: // move down
                        if (n[2].isWalkable()) {
                            c.move(n[2]);
                            doGameCycle();
                        }
                        break;
                    case KeyEvent.VK_LEFT: // move left
                        if (n[3].isWalkable()) {
                            c.move(n[3]);
                            doGameCycle();
                        }
                        break;
                    case KeyEvent.VK_I: // open inventory
                        c.getInventoryGUI().setVisible((!c.getInventoryGUI().isVisible()));
                        Constants.GAME_FRAME.repaint();
                        break;
                    case KeyEvent.VK_SPACE: // collect items
                        c.detection(c.getLocatedAt());
                        doGameCycle();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException ignore) {
                // do nothing
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // do nothing
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // do nothing
        }
    }

    private final class GameBoardMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (c.getInventoryGUI().mouseClicked(e)) // checks if the inventory receives this event
                return;
            if (checkComponents(e.getPoint())) // check for active components
                return;

            // when the event doesn't belong to any component the character moves
            // get destination Tile
            final Tile tile = getFromDisplayLocation(e.getPoint());
            if (tile.isWalkable())
                // if there is an enemy(Fightable content) nearby, attack it
                if (isFightableNeighbour(c.getLocatedAt(), tile)) {
                    tile.hit(c.attack());
                    doGameCycle();
                } else // move normally
                    setPlayerMovePath(tile);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // nothing happens
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // nothing happens
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // nothing happens
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // nothing happens
        }

        private boolean isFightableNeighbour(Tile start, Tile destination) {
            return destination.hasHittableContent(c.getLocatedAt().getContents())
                    && NeighbourFinder.isNeighbor(start.x, start.y, destination.x, destination.y);
        }

        private void setPlayerMovePath(Tile destination) {
            // configure pathfinder
            final PathFinderConfig pfc = new PathFinderConfig();
            // set to blacklist (mode)
            pfc.setDisallowed();
            // add Wall to blacklist
            pfc.addDest(Wall.class);
            try {
                c.addPath(new PathFinder(getTileGrid(), pfc).findPath(c.getLocatedAt(), destination));
                gameTimer.start();
            } catch (PathNotFoundException ignore) {
                // Could not move
            }
        }
    }
}
