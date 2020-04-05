package utils;

public class RoomGenerationObstructedException extends RuntimeException {

	public RoomGenerationObstructedException() {
		super("Tile was already used!");
	}

	public RoomGenerationObstructedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
