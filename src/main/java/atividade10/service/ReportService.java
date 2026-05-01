package atividade10.service;

import atividade10.enums.OrderStatus;
import atividade10.enums.ProductCategory;
import atividade10.model.Order;
import atividade10.model.OrderItem;
import atividade10.model.Product;
import atividade10.repository.CustomerRepository;
import atividade10.repository.OrderRepository;
import atividade10.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// André Vinícius Barros Macambira
public class ReportService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public ReportService(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public BigDecimal totalRevenue() {
        BigDecimal total = orderRepository.findByStatus(OrderStatus.PAID)
                .stream()
                .map(Order::totalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            throw new RuntimeException("Nenhum pedido pago encontrado.");
        }

        return total;
    }

    public List<Map.Entry<String, Integer>> topThreeProducts() {
        Map<String, Integer> quantityPerSku = new HashMap<>();

        for (Order order : orderRepository.findByStatus(OrderStatus.PAID)) {
            for (OrderItem item : order.getItems()) {
                quantityPerSku.merge(item.getSku(), item.getQuantity(), Integer::sum);
            }
        }

        if (quantityPerSku.isEmpty()) {
            throw new RuntimeException("Nenhum pedido pago encontrado.");
        }

        return quantityPerSku.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());
    }


    public Map<ProductCategory, BigDecimal> revenueByCategory() {
        Map<ProductCategory, BigDecimal> revenueMap = new HashMap<>();

        for (Order order : orderRepository.findByStatus(OrderStatus.PAID)) {
            for (OrderItem item : order.getItems()) {
                Product product = productRepository.findById(item.getSku())
                        .orElseThrow(() -> new RuntimeException(
                                "Produto não encontrado: " + item.getSku()));

                revenueMap.merge(
                        product.getCategory(),
                        item.subtotal(),
                        BigDecimal::add
                );
            }
        }

        if (revenueMap.isEmpty()) {
            throw new RuntimeException("Nenhum pedido pago encontrado.");
        }

        return revenueMap;
    }

    public List<Map.Entry<String, Long>> topCustomersByOrderCount() {
        Map<String, Long> ordersPerCustomer = orderRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Order::getCustomerId,
                        Collectors.counting()
                ));

        if (ordersPerCustomer.isEmpty()) {
            throw new RuntimeException("Nenhum pedido encontrado.");
        }

        return ordersPerCustomer.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());
    }
}
