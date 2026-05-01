package atividade10.exceptions;

// André Vinícius Barros Macambira
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
