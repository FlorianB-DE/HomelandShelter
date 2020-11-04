package main.UI;

import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import main.Constants;
import textures.TextureReader;
import utils.WindowUtils;

public class GameFrame extends JFrame {

	private static final float scalefactor = 0.8F;

	public GameFrame() {
		super(Constants.TITLE);
		final WindowUtils bounds = new WindowUtils( //
				GraphicsEnvironment //
						.getLocalGraphicsEnvironment() //
						.getDefaultScreenDevice() //
						.getDefaultConfiguration() //
						.getBounds() //
								.width, //
				GraphicsEnvironment //
						.getLocalGraphicsEnvironment() //
						.getDefaultScreenDevice() //
						.getDefaultConfiguration() //
						.getBounds() //
								.height, //
				scalefactor, scalefactor);
		setLocation(bounds.getWindowPosition());
		setSize(bounds.getWindowDimensions());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setIconImage(TextureReader.getTextureByString("CHAR").getContent().getImage());
	}
}
