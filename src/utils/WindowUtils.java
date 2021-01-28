package utils;

import java.awt.*;

/**
 * author Florian Mirko Becker Version 0.1 2020
 * <p>
 * This class is used to calculate the exact percentage bounds for a given
 * "Dimension" or "Toolkit" object. It provides the percentage width, height and
 * the point for an object to be exactly centered if the origin is in the
 * left-top corner.
 */
public class WindowUtils {

	private Dimension bounds;

	private float widthFactor, heightFactor, horizontalOffset, verticalOffset;

	private int x, y, width, height;

	public WindowUtils(Dimension bounds, float widthFactor, float heightFactor) {
		if (bounds == null) {
			throw new NullPointerException();
		}

		if (Math.abs(widthFactor) > 1 || widthFactor < 0) {
			throw new IllegalArgumentException("\"widthFactor\" must be between 0 and 1!");
		}

		if (Math.abs(heightFactor) > 1 || heightFactor < 0) {
			throw new IllegalArgumentException("\"heightFactor\" must be between 0 and 1!");
		}

		this.bounds = bounds;
		this.widthFactor = widthFactor;
		this.heightFactor = heightFactor;

		horizontalOffset = 1;
		verticalOffset = 1;

		calculate();
	}

	/**
	 * widthFactor and heightFactor range from 0 to 1. Default value of the offsets
	 * is 0. Their range is from -1 (left) to 1 (right)
	 */
	public WindowUtils(Dimension bounds, float widthFactor, float heightFactor, float verticalOffset,
			float horizontalOffset) {
		this(bounds, widthFactor, heightFactor);

		if (Math.abs(verticalOffset) > 1) {
			this.verticalOffset = 1;
		} else {
			this.verticalOffset = verticalOffset + 1;
		}

		if (Math.abs(horizontalOffset) > 1) {
			this.horizontalOffset = 1;
		} else {
			this.horizontalOffset = horizontalOffset + 1;
		}
		calculate();
	}

	public WindowUtils(int width, int height, float widthFactor, float heightFactor) {
		this(new Dimension(width, height), widthFactor, heightFactor);
	}

	public WindowUtils(int width, int height, float widthFactor, float heightFactor, float verticalOffset,
			float horizontalOffset) {
		this(new Dimension(width, height), widthFactor, heightFactor, verticalOffset, horizontalOffset);
	}

	public WindowUtils(Toolkit toolkit, float widthFactor, float heightFactor) {
		this(toolkit.getScreenSize(), widthFactor, heightFactor);
	}

	/**
	 * widthFactor and heightFactor range from 0 to 1. Default value of the offsets
	 * is 0. Their range is from 1 (left) to -1 (right)
	 */
	public WindowUtils(Toolkit toolkit, float widthFactor, float heightFactor, float verticalOffset,
			float horizontalOffset) {
		this(toolkit.getScreenSize(), widthFactor, heightFactor, horizontalOffset, verticalOffset);
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the heightFactor
	 */
	public float getHeightFactor() {
		return heightFactor;
	}

	/**
	 * @return the horizontalOffset
	 */
	public float getHorizontalOffset() {
		return horizontalOffset - 1;
	}

	/**
	 * @return the original size from where the calculations were made
	 */
	public Dimension getOriginalBounds() {
		return bounds;
	}

	/**
	 * @return the verticalOffset
	 */
	public float getVerticalOffset() {
		return verticalOffset - 1;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the widthFactor
	 */
	public float getWidthFactor() {
		return widthFactor;
	}

	/**
	 * @return the window Dimension with specified specifications
	 */
	public Dimension getWindowDimensions() {
		calculate();
		return new Dimension(width, height);
	}

	/**
	 * @return the window position with specified specifications
	 */
	public Point getWindowPosition() {
		calculate();
		return new Point(x, y);
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * resets all offsets to 0
	 */
	public void resetOffset() {
		horizontalOffset = 1F;
		verticalOffset = 1F;
		calculate();
	}

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(Dimension bounds) {
		this.bounds = bounds;
		calculate();
	}

	/**
	 * @param heightFactor the heightFactor to set
	 */
	public void setHeightFactor(float heightFactor) {
		this.heightFactor = heightFactor;
		calculate();
	}

	/**
	 * @param horizontalOffset the horizontalOffset to set
	 */
	public void setHorizontalOffset(float horizontalOffset) {
		if (Math.abs(horizontalOffset) > 1) {
			this.horizontalOffset = 0;
		} else {
			this.horizontalOffset = horizontalOffset + 1;
		}
		calculate();
	}

	/**
	 * @param toolkit to set
	 */
	public void setToolkit(Toolkit toolkit) {
		bounds = toolkit.getScreenSize();
		calculate();
	}

	/**
	 * @param verticalOffset the verticalOffset to set
	 */
	public void setVerticalOffset(float verticalOffset) {
		if (Math.abs(verticalOffset) > 1) {
			this.verticalOffset = 0;
		} else {
			this.verticalOffset = verticalOffset + 1;
		}
		calculate();
	}

	/**
	 * @param widthFactor the widthFactor to set
	 */
	public void setWidthFactor(float widthFactor) {
		this.widthFactor = widthFactor;
		calculate();
	}

	private void calculate() {
		width = Math.round((float) (bounds.getWidth() * widthFactor));
		height = Math.round((float) (bounds.getHeight() * heightFactor));
		x = Math.round((float) ((bounds.getWidth() / 2F) - (width / 2F)) * verticalOffset);
		y = Math.round((float) ((bounds.getHeight() / 2F) - (height / 2F)) * horizontalOffset);
	}
}
