package utils;

/**
 * TODO
 *
 * @author Tim Bauer
 * @version 1.0 2020-04-04
 */
public class PathNotFoundException extends RuntimeException {

	public PathNotFoundException() {
		super("Could not generate Path!");
	}

	public PathNotFoundException(String s) {
		super(s);
	}
}
