package utils.exceptions;

public class ItemCreationFailedException extends RuntimeException {

    public ItemCreationFailedException() {
        super("Could not read Item!");
    }

    public ItemCreationFailedException(String message) {
        super(message);
    }
}
