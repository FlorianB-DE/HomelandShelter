/*
 * PathFinder.java 1.0 03 Apr 2020
 * Author: Tim Bauer
 */
package main.core;

import main.tiles.Tile;
import utils.exceptions.PathNotFoundException;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * PathFinder
 * Find the shortest route that starts at the start room door and ends at the
 * end room door. An adapted version of the A* algorithm is used for this.
 *
 * @author Tim Bauer
 * @version 1.1 2020-04-04
 */
@SuppressWarnings("ALL")
public class PathFinder {

    private static final int MAX_WRONG_COUNT = 7;
    private final PathFinderConfig conf;
    private final int MAX_X;
    private final int MAX_Y;
    private final Tile[][] tiles;
    private List<PathNode> closedNodes;
    private PriorityQueue<PathNode> openNodes;

    /**
     * Constructor
     *
     * @param tiles Grid
     */
    public PathFinder(Tile[][] tiles, PathFinderConfig conf) {
        this.tiles = tiles;
        MAX_X = tiles.length;
        MAX_Y = tiles[0].length;
        this.conf = conf;
    }

    /**
     * public method to start the search for the way
     *
     * @param startDoor
     * @param endDoor
     * @return List of passed Points
     * @throws PathNotFoundException
     */
    public BlockingDeque<Point> findPath(Tile startDoor, Tile endDoor) throws

            PathNotFoundException {
        BlockingDeque<Point> b = new LinkedBlockingDeque<>();
        openNodes = new PriorityQueue<>(new PathNodeComperator());
        closedNodes = new ArrayList<>();

        PathNode s = new PathNode(0, new Point((int) startDoor.getX(),
                (int) startDoor.getY()),
                (int) Math.abs(startDoor.getX() -
                        endDoor.getX()), (int) Math
                .abs(startDoor.getY() - endDoor.getY()));
        openNodes.add(s);
        while (!openNodes.isEmpty()) {
            PathNode cur = openNodes.remove();
            if ((int) cur.getPoint().getX() == (int) endDoor.getX() &&
                    (int) cur.getPoint().getY() == (int) endDoor.getY()) {

                PathNode t = cur;

                do {
                    b.addFirst(t.getPoint());
                } while ((t = t.getParent()) != null);

                return b;
            }

            closedNodes.add(cur);
            expandNode(cur, (int) endDoor.getX(), (int) endDoor.getY());
        }
        throw new PathNotFoundException();
    }

    /**
     * Method to add the node to the list, see A* Algorithm
     *
     * @param c current PathNode
     * @param n next Pathnode
     */
    private void addToOpenNodes(PathNode c, PathNode n) {
        int x = (int) n.getPoint().getX();
        int y = (int) n.getPoint().getY();
        if (x < MAX_X && x >= 0 && y < MAX_Y && y >= 0 && conf.allowedMoveTo(
                tiles[(int) n.getPoint().getX()][(int) n.getPoint().getY()])) {
            if ((n.getXdif() > c.getXdif() || n.getYdif() > c.getYdif()) &&
                    n.getWrongDirectionCount() > MAX_WRONG_COUNT) {
                n.setWrongDirectionCount(c.getWrongDirectionCount() + 1);
            }
            openNodes.add(n);
        }
    }

    /**
     * @param c  current PathNode
     * @param fX x-position of the end room door
     * @param fY y-position of the end room door
     */
    private void expandNode(PathNode c, int fX, int fY) {

        // TODO Find better way for neigbours

        PathNode[] neighbours = new PathNode[4];
        neighbours[0] = new PathNode(c.getCost() + 1,
                new Point((int) c.getPoint().getX() + 1,
                        (int) c.getPoint().getY()), fX,
                fY);
        neighbours[1] = new PathNode(c.getCost() + 1,
                new Point((int) c.getPoint().getX(),
                        (int) c.getPoint().getY() + 1),
                fX, fY);
        neighbours[2] = new PathNode(c.getCost() + 1,
                new Point((int) c.getPoint().getX(),
                        (int) c.getPoint().getY() - 1),
                fX, fY);
        neighbours[3] = new PathNode(c.getCost() + 1,
                new Point((int) c.getPoint().getX() - 1,
                        (int) c.getPoint().getY()), fX,
                fY);
        for (PathNode n : neighbours) {
            n.setParent(c);
            if (!closedNodes.contains(n)) {
                if (openNodes.contains(n)) {
                    Iterator<PathNode> it = openNodes.iterator();
                    while (it.hasNext()) {
                        PathNode p = it.next();
                        if (n.getCost() < p.getCost()) {
                            openNodes.remove(p);
                            addToOpenNodes(c, n);
                        }
                    }
                } else {
                    addToOpenNodes(c, n);
                }
            }
        }
    }

    /**
     * PathNode class. Used for storing and sorting the grid.
     */
    private static class PathNode {

        private final double cost;
        private final Point p;
        private final int xdif;
        private final int ydif;
        private PathNode parent;
        private int wrongDirectionCount = 0;

        public PathNode(double cost, Point p, int fX, int fY) {
            this.p = p;
            this.cost = cost;
            this.xdif = (int) Math.abs(fX - p.getX());
            this.ydif = (int) Math.abs(fY - p.getY());
        }

        /**
         * @param v
         * @return Result, whether the objects are equal
         */
        @Override
        public boolean equals(Object v) {
            if (v instanceof PathNode) {
                PathNode p = (PathNode) v;
                //noinspection RedundantIfStatement
                if (getPoint().getX() == p.getPoint().getX() &&
                        getPoint().getY() == p.getPoint().getY()) {
                    return true;
                }
            }
            return false;
        }

        public double getCost() {
            return cost;
        }

        public PathNode getParent() {
            return parent;
        }

        public void setParent(PathNode parent) {
            this.parent = parent;
        }

        public Point getPoint() {
            return p;
        }

        public int getWrongDirectionCount() {
            return wrongDirectionCount;
        }

        public void setWrongDirectionCount(int i) {
            wrongDirectionCount = i;
        }

        public int getXdif() {
            return xdif;
        }

        public int getYdif() {
            return ydif;
        }

        @Override
        public String toString() {
            return "X: " + ((int) getPoint().getX()) + " Y: " +
                    ((int) getPoint().getY() + " W: " +
                            getWrongDirectionCount());
        }
    }

    /**
     * We need an own comperator to compare our PathNode objects
     */
    private static class PathNodeComperator implements Comparator<PathNode> {
        @Override
        public int compare(PathNode n1, PathNode n2) {
            return Double.compare(n1.getCost(), n2.getCost());
        }
    }
}
