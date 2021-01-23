package main.ui;

import main.Constants;
import textures.TextureReader;
import utils.WindowUtils;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

	private static final float scaleFactor = 0.8F;

	public GameFrame() {
		super(Constants.TITLE);
		if (Constants.FULLSCREEN)
		{
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setUndecorated(true);
			setVisible(true);
		}
		else
		{
			final WindowUtils bounds = new WindowUtils( //
					GraphicsEnvironment //
							.getLocalGraphicsEnvironment() //
							.getDefaultScreenDevice() //
							.getDefaultConfiguration() //
							.getBounds().width, //
					GraphicsEnvironment //
							.getLocalGraphicsEnvironment() //
							.getDefaultScreenDevice() //
							.getDefaultConfiguration() //
							.getBounds().height, //
					scaleFactor, scaleFactor);
			setLocation(bounds.getWindowPosition());
			setSize(bounds.getWindowDimensions());
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setIconImage(TextureReader.getTextureByString("CHAR").getContent().getImage());
	}
}
