package main.tiles;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import textures.Textures;

public class Wall extends Tile {

	public Wall(Point p, int size) {
		super(p, size);
		// TODO Auto-generated constructor stub
	}

	public Wall(int x, int y, int size) {
		super(x, y, size);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show(Graphics2D g) {
//		float alpha = 1 - (float) MathUtils.map(0,
//				Math.max(Main.getGameDimension().width, Main.getGameDimension().height), 0, 1,
//				getDistance(Main.getGameDimension().getWidth() / 2d, Main.getGameDimension().getHeight() / 2d));
//		if (alpha > 1)
//			alpha = 1;
//		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

		g.drawImage(Textures.WALL.loadImage().getImage(), x, y, width, height, Color.black, null);
	}

}
