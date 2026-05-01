package atividade10.enums;

// André Vinícius Barros Macambira
public enum OrderStatus {
    PENDING,
    RESERVED,
    PAID,
    FAILED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case PENDING   -> next == RESERVED || next == CANCELLED;
            case RESERVED  -> next == PAID || next == FAILED || next == CANCELLED;
            case PAID, FAILED, CANCELLED -> false;
        };
    }
}
