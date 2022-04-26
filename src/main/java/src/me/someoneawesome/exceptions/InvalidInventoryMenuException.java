package src.me.someoneawesome.exceptions;

public class InvalidInventoryMenuException extends RuntimeException {
    public InvalidInventoryMenuException(String message) {
        super(message);
    }
}
