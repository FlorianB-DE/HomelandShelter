package utils.exceptions;

public class CanNotMoveException extends RuntimeException {

    public CanNotMoveException() {
        super("Cannot move");
    }

    public CanNotMoveException(String message) {
        super(message);
    }

}
