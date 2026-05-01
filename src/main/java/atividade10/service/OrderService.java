package atividade10.service;

import atividade10.enums.OrderStatus;
import atividade10.exceptions.InsufficientStockException;
import atividade10.exceptions.ObjectNotFoundException;
import atividade10.model.Order;
import atividade10.model.OrderItem;
import atividade10.model.Product;
import atividade10.repository.CustomerRepository;
import atividade10.repository.OrderRepository;
import atividade10.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

// André Vinícius Barros Macambira
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public Order createOrder(String customerId, Map<String, Integer> skuQuantities) {
        validateCustomerExists(customerId);
        validateSkuQuantities(skuQuantities);

        List<OrderItem> items = buildOrderItems(skuQuantities);

        Order order = new Order(customerId, LocalDateTime.now(), OrderStatus.PENDING, items);
        orderRepository.save(order);
        return order;
    }

    public Order reserveStock(String orderId) {
        Order order = findById(orderId);

        validateStockAvailability(order);

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getSku()).get();
            product.reserveStock(item.getQuantity());
        }

        order.transitionTo(OrderStatus.RESERVED);
        return order;
    }

    public Order payOrder(String orderId) {
        Order order = findById(orderId);

        if (order.getStatus() != OrderStatus.RESERVED) {
            throw new IllegalStateException(
                    "Apenas pedidos com estoque reservado podem ser pagos. " +
                            "Status atual: " + order.getStatus());
        }

        boolean paymentApproved = simulatePayment(order.totalAmount());

        if (paymentApproved) {
            for (OrderItem item : order.getItems()) {
                Product product = productRepository.findById(item.getSku()).get();
                product.confirmSale(item.getQuantity());
            }
            order.transitionTo(OrderStatus.PAID);
        } else {
            for (OrderItem item : order.getItems()) {
                Product product = productRepository.findById(item.getSku()).get();
                product.releaseReservation(item.getQuantity());
            }
            order.transitionTo(OrderStatus.FAILED);
        }

        return order;
    }

    public Order cancelOrder(String orderId) {
        Order order = findById(orderId);

        if (order.getStatus() == OrderStatus.RESERVED) {
            for (OrderItem item : order.getItems()) {
                Product product = productRepository.findById(item.getSku()).get();
                product.releaseReservation(item.getQuantity());
            }
        }

        order.transitionTo(OrderStatus.CANCELLED);
        return order;
    }


    public Order findById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Pedido não encontrado: " + orderId));
    }

    public List<Order> listByCustomer(String customerId) {
        validateCustomerExists(customerId);
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if (orders.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum pedido encontrado para o cliente: " + customerId);
        }
        return orders;
    }

    public List<Order> listByStatus(String statusStr) {
        OrderStatus status = parseStatus(statusStr);
        List<Order> orders = orderRepository.findByStatus(status);
        if (orders.isEmpty()) {
            throw new ObjectNotFoundException("Nenhum pedido encontrado com status: " + statusStr);
        }
        return orders;
    }

    private List<OrderItem> buildOrderItems(Map<String, Integer> skuQuantities) {
        List<OrderItem> items = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : skuQuantities.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado: " + entry.getKey()));
            items.add(new OrderItem(entry.getValue(), entry.getKey(), product.getPrice()));
        }
        return items;
    }

    private void validateStockAvailability(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getSku())
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Produto não encontrado: " + item.getSku()));

            if (product.getAvailableStock() < item.getQuantity()) {
                throw new InsufficientStockException(
                        "Estoque insuficiente para o produto: " + item.getSku() +
                                ". Disponível: " + product.getAvailableStock() +
                                ", solicitado: " + item.getQuantity());
            }
        }
    }

    private void validateCustomerExists(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ObjectNotFoundException("Cliente não encontrado: " + customerId);
        }
    }

    private void validateSkuQuantities(Map<String, Integer> skuQuantities) {
        if (skuQuantities == null || skuQuantities.isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter ao menos um item.");
        }
        for (Map.Entry<String, Integer> entry : skuQuantities.entrySet()) {
            if (entry.getValue() <= 0) {
                throw new IllegalArgumentException(
                        "Quantidade inválida para o produto " + entry.getKey() +
                                ": " + entry.getValue());
            }
        }
    }

    private boolean simulatePayment(BigDecimal amount) {
        return Math.random() < 0.80;
    }

    private OrderStatus parseStatus(String statusStr) {
        try {
            return OrderStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + statusStr + ". Opções válidas: " + Arrays.toString(OrderStatus.values()));
        }
    }
}
