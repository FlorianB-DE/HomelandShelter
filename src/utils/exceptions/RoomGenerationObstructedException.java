package utils.exceptions;

/**
 * TODO
 *
 * @author Florian M. Becker
 * @version 1.0 05.04.2020
 */
public class RoomGenerationObstructedException extends RuntimeException {

    public RoomGenerationObstructedException() {
        super("Tile was already used!");
    }

    public RoomGenerationObstructedException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
}
