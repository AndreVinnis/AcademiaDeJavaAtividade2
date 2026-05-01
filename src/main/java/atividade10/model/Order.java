package atividade10.model;

import atividade10.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

// André Vinícius Barros Macambira
public class Order {

    private String orderId;
    private String customerId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItem> items;

    public Order() {
    }

    public Order(String customerId, LocalDateTime createdAt, OrderStatus status, List<OrderItem> items) {
        this.orderId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.status = status;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId) && Objects.equals(customerId, order.customerId) && status == order.status && Objects.equals(createdAt, order.createdAt) && Objects.equals(updatedAt, order.updatedAt) && Objects.equals(items, order.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, customerId, status, createdAt, updatedAt, items);
    }

    public BigDecimal totalAmount() {
        return items.stream()
                .map(OrderItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean canBeReserved() {
        return status == OrderStatus.PENDING;
    }

    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING || status == OrderStatus.RESERVED;
    }

    public void transitionTo(OrderStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException("Transição inválida para o pedido " + orderId + ": " + this.status + " → " + newStatus);
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
}
