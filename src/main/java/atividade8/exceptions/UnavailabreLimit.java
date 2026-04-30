package atividade8.exceptions;

// André Vinícius Barros Macambira
public class UnavailabreLimit extends RuntimeException {
    public UnavailabreLimit(String message) {
        super(message);
    }
}
