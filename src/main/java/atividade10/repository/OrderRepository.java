package atividade10.repository;

import atividade10.enums.OrderStatus;
import atividade10.model.Order;
import java.util.*;
import java.util.stream.Collectors;

// André Vinícius Barros Macambira
public class OrderRepository implements Repository<Order, String> {
    private final Map<String, Order> store = new HashMap<>();

    @Override
    public void save(Order order) {
        store.put(order.getOrderId(), order);
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(store.get(orderId));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(String orderId) {
        store.remove(orderId);
    }

    @Override
    public boolean existsById(String orderId) {
        return store.containsKey(orderId);
    }

    public List<Order> findByCustomerId(String customerId) {
        return store.values().stream()
                .filter(o -> o.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    public List<Order> findByStatus(OrderStatus status) {
        return store.values().stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
    }
}

