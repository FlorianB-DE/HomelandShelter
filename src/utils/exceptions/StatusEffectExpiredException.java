package utils.exceptions;

public class StatusEffectExpiredException extends RuntimeException {

	public StatusEffectExpiredException() {
		super("Effect expired!");
	}

	public StatusEffectExpiredException(String message) {
		super(message);
	}
}
