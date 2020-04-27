package utils.exceptions;

public class NoSuchAttributeException extends RuntimeException {

	public NoSuchAttributeException() {
		super("Attribute not found");
	}

	public NoSuchAttributeException(String message) {
		super(message);
	}

}
