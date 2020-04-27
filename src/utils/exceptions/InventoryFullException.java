package utils.exceptions;

public class InventoryFullException extends RuntimeException {

	public InventoryFullException() {
		super("Inventory is Full!");
	}

	public InventoryFullException(String message) {
		super(message);
	}
}
