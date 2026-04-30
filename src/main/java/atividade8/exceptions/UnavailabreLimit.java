package atividade8.exceptions;

public class UnavailabreLimit extends RuntimeException {
    public UnavailabreLimit(String message) {
        super(message);
    }
}
