/*
 * PathFinder.java 1.0 03 Apr 2020
 * Author: Tim Bauer
 */
package main.core;

import main.tiles.Tile;
import utils.exceptions.PathNotFoundException;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
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
public class PathFinder {

	/**
	 * PathNode class. Used for storing and sorting the grid.
	 */
	private class PathNode {

		public final double cost;
		private Point p;
		private PathNode parent;
		private int wrongDirectionCount = 0;
		private final int xdif, ydif;

		public PathNode(double cost, Point p, int fX, int fY) {
			this.p = p;
			this.cost = cost;
			this.xdif = (int) Math.abs(fX - p.getX());
			this.ydif = (int) Math.abs(fY - p.getY());
		}

		/**
		 * @param other
		 * @return Result, whether the objects are equal
		 */
		@Override
		public boolean equals(Object other) {
			if (other instanceof PathNode otherNode) {
				Point otherPoint = otherNode.getPoint(),
						thisPoint = getPoint();
				return thisPoint.equals(otherPoint);
			}
			return false;
		}

		public PathNode getParent() {
			return parent;
		}

		public Point getPoint() {
			return p;
		}

		public int getWrongDirectionCount() {
			return wrongDirectionCount;
		}

		public void setParent(PathNode parent) {
			this.parent = parent;
		}

		public void setWrongDirectionCount(int i) {
			wrongDirectionCount = i;
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
	private class PathNodeComperator implements Comparator<PathNode> {
		@Override
		public int compare(PathNode n1, PathNode n2) {
			return Double.compare(n1.cost, n2.cost);
		}
	}

	private static final int MAX_WRONG_COUNT = 7;
	private final List<PathNode> closedNodes;
	private final PathFinderConfig conf;
	private final int MAX_X;
	private final int MAX_Y;

	private final PriorityQueue<PathNode> openNodes;

	private final Tile[][] tiles;

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
		closedNodes = new ArrayList<>();
		openNodes = new PriorityQueue<>(new PathNodeComperator());
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
		BlockingDeque<Point> b = new LinkedBlockingDeque<Point>();
		openNodes.clear();
		closedNodes.clear();

		PathNode s = new PathNode(0, new Point((int) startDoor.getX(),
				(int) startDoor.getY()),
				(int) Math.abs(startDoor.getX() -
						endDoor.getX()),
				(int) Math
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
			if ((n.xdif > c.xdif || n.ydif > c.ydif) &&
					n.getWrongDirectionCount() > MAX_WRONG_COUNT) {
				n.setWrongDirectionCount(c.getWrongDirectionCount() + 1);
			}
			openNodes.add(n);
		}
	}

	/**
	 * @param node  current PathNode
	 * @param fX x-position of the end room door
	 * @param fY y-position of the end room door
	 */
	private void expandNode(PathNode node, int fX, int fY) {

		// TODO Find better way for neigbours

		final var newCost = node.cost + 1;
		final var parentPoint = node.getPoint();

		PathNode[] neighbours = new PathNode[4];
		neighbours[0] = new PathNode(newCost,
				new Point((int) parentPoint.getX() + 1,
						(int) parentPoint.getY()),
				fX,
				fY);
		neighbours[1] = new PathNode(newCost,
				new Point((int) parentPoint.getX(),
						(int) parentPoint.getY() + 1),
				fX, fY);
		neighbours[2] = new PathNode(newCost,
				new Point((int) parentPoint.getX(),
						(int) parentPoint.getY() - 1),
				fX, fY);
		neighbours[3] = new PathNode(newCost,
				new Point((int) parentPoint.getX() - 1,
						(int) parentPoint.getY()),
				fX,
				fY);

		for (PathNode neighbour : neighbours) {
			neighbour.setParent(node);
			if (closedNodes.contains(neighbour))
				continue;

			if (!openNodes.contains(neighbour)) {
				addToOpenNodes(node, neighbour);
				continue;
			}

			Iterator<PathNode> it = openNodes.iterator();
			while (it.hasNext()) {
				PathNode p = it.next();
				if (neighbour.cost >= p.cost)
					continue;

				openNodes.remove(p);
				addToOpenNodes(node, neighbour);
			}

		}
	}
}
