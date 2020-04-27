package utils.exceptions;

public class CommandNotFoundException extends RuntimeException {

	public CommandNotFoundException() {
		super("Command not listed");
	}

	public CommandNotFoundException(String message) {
		super(message);
	}

}
