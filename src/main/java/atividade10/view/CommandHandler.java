package atividade10.view;

import atividade10.enums.OrderStatus;
import atividade10.enums.ProductCategory;
import atividade10.exceptions.InsufficientStockException;
import atividade10.exceptions.ObjectNotFoundException;
import atividade10.model.Customer;
import atividade10.model.Order;
import atividade10.model.Product;
import atividade10.service.CustomerService;
import atividade10.service.OrderService;
import atividade10.service.ProductService;
import atividade10.service.ReportService;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// André Vinícius Barros Macambira
public class CommandHandler {

    private final ProductService productService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final ReportService reportService;

    public CommandHandler(ProductService productService, CustomerService customerService, OrderService orderService, ReportService reportService) {
        this.productService = productService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.reportService = reportService;
    }

    public void handle(String input) {
        if (input == null || input.isBlank()) return;

        String[] parts = input.trim().split("\\s+");
        String command = parts[0].toLowerCase();

        try {
            switch (command) {
                case "add-product"    -> handleAddProduct(parts);
                case "list-products"  -> handleListProducts(parts);
                case "add-client"     -> handleAddClient(parts);
                case "list-clients"   -> handleListClients();
                case "create-order"   -> handleCreateOrder(parts);
                case "reserve-stock"  -> handleReserveStock(parts);
                case "pay-order"      -> handlePayOrder(parts);
                case "cancel-order"   -> handleCancelOrder(parts);
                case "report"         -> handleReport(parts);
                case "help"           -> printHelp();
                case "exit"           -> System.exit(0);
                default               -> System.out.println(
                        "Comando desconhecido: " + command + ". Digite 'help' para ver os comandos.");
            }
        }
        catch (IllegalArgumentException | IllegalStateException | ObjectNotFoundException |
                 InsufficientStockException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    private void handleAddProduct(String[] parts) {
        validateArgCount(parts, 6,
                "Uso: add-product <sku> <nome> <categoria> <preco> <estoque>");

        Product product = productService.addProduct(
                parts[1], parts[2], parts[3], parts[4], parts[5]);

        System.out.println("Produto cadastrado com sucesso!");
        printProduct(product);
    }

    private void handleListProducts(String[] parts) {
        String sortBy = parts.length > 1 ? parts[1] : "sku";
        List<Product> products = productService.listAll(sortBy);
        System.out.println("\n── Produtos (" + products.size() + ") ──────────────────");
        products.forEach(this::printProduct);
    }


    private void handleAddClient(String[] parts) {
        validateArgCount(parts, 4,
                "Uso: add-client <nome> <email> <cpf>");

        Customer customer = customerService.createCustomer(
                parts[1], parts[2], parts[3]);

        System.out.println("Cliente cadastrado com sucesso!");
        printCustomer(customer);
    }

    private void handleListClients() {
        List<Customer> customers = customerService.listAll();
        System.out.println("\n── Clientes (" + customers.size() + ") ──────────────────");
        customers.forEach(this::printCustomer);
    }


    private void handleCreateOrder(String[] parts) {
        validateArgCount(parts, 3,
                "Uso: create-order <customerId> <sku:quantidade> ...");

        String customerId = parts[1];
        Map<String, Integer> skuQuantities = parseSkuQuantities(parts);

        Order order = orderService.createOrder(customerId, skuQuantities);
        System.out.println("Pedido criado com sucesso!");
        printOrder(order);
    }

    private void handleReserveStock(String[] parts) {
        validateArgCount(parts, 2, "Uso: reserve-stock <orderId>");
        Order order = orderService.reserveStock(parts[1]);
        System.out.println("Estoque reservado com sucesso!");
        printOrder(order);
    }

    private void handlePayOrder(String[] parts) {
        validateArgCount(parts, 2, "Uso: pay-order <orderId>");
        Order order = orderService.payOrder(parts[1]);

        if (order.getStatus() == OrderStatus.PAID) {
            System.out.println("Pagamento aprovado!");
        } else {
            System.out.println("Pagamento recusado. Pedido marcado como FAILED.");
        }
        printOrder(order);
    }

    private void handleCancelOrder(String[] parts) {
        validateArgCount(parts, 2, "Uso: cancel-order <orderId>");
        Order order = orderService.cancelOrder(parts[1]);
        System.out.println("Pedido cancelado com sucesso!");
        printOrder(order);
    }


    private void handleReport(String[] parts) {
            BigDecimal total = reportService.totalRevenue();
            System.out.println("\n── Faturamento total ────────────────────");
            System.out.printf("Total: R$ %.2f%n", total);

            System.out.println("\n── Top 3 produtos mais vendidos ─────────");
            List<Map.Entry<String, Integer>> topProducts = reportService.topThreeProducts();
            int rank = 1;
            for (Map.Entry<String, Integer> entry : topProducts) {
                System.out.printf("%dº  %-15s %d unidades%n", rank++, entry.getKey(), entry.getValue());
            }


            System.out.println("\n── Faturamento por categoria ────────────");
            Map<ProductCategory, BigDecimal> byCategory = reportService.revenueByCategory();
            byCategory.entrySet()
                    .stream()
                    .sorted(Map.Entry.<ProductCategory, BigDecimal>comparingByValue().reversed())
                    .forEach(e -> System.out.printf("%-15s R$ %.2f%n", e.getKey(), e.getValue()));

            System.out.println("\n── Top 3 clientes com mais pedidos ────────────");
            List<Map.Entry<String, Long>> topCustomers = reportService.topCustomersByOrderCount();
            rank = 1;
            for (Map.Entry<String, Long> entry : topCustomers.stream().limit(3).toList()) {
                System.out.printf("%dº  %-15s %d pedidos%n", rank++, entry.getKey(), entry.getValue());
            }
    }


    private Map<String, Integer> parseSkuQuantities(String[] parts) {
        Map<String, Integer> skuQuantities = new LinkedHashMap<>();
        for (int i = 2; i < parts.length; i++) {
            String[] skuQty = parts[i].split(":");
            if (skuQty.length != 2) {
                throw new IllegalArgumentException(
                        "Formato inválido: " + parts[i] + ". Use sku:quantidade (ex: SKU001:2)");
            }
            try {
                int quantity = Integer.parseInt(skuQty[1]);
                if (quantity <= 0) {
                    throw new IllegalArgumentException(
                            "Quantidade deve ser maior que zero: " + parts[i]);
                }
                skuQuantities.put(skuQty[0], quantity);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Quantidade inválida: " + parts[i]);
            }
        }
        return skuQuantities;
    }

    private void validateArgCount(String[] parts, int min, String usage) {
        if (parts.length < min) {
            throw new IllegalArgumentException(usage);
        }
    }


    private void printProduct(Product p) {
        System.out.printf("[%s] %-20s %-12s R$ %8.2f  estoque: %d%n",
                p.getSku(), p.getName(), p.getCategory(),
                p.getPrice(), p.getAvailableStock());
    }

    private void printCustomer(Customer c) {
        System.out.printf("[%s] %-20s %s%n",
                c.getCustomerId(), c.getName(), c.getEmail());
    }

    private void printOrder(Order o) {
        System.out.printf("Pedido: %s | Cliente: %s | Status: %s | Total: R$ %.2f%n",
                o.getOrderId(), o.getCustomerId(),
                o.getStatus(), o.totalAmount());
        o.getItems().forEach(item ->
                System.out.printf("    - %-15s x%d  R$ %.2f%n",
                        item.getSku(), item.getQuantity(), item.subtotal()));
    }

    private void printHelp() {
        System.out.println("""
            
            ── Comandos disponíveis ──────────────────────────────────
            add-product <sku> <nome> <categoria> <preco> <estoque>
            list-products [sku|price]
            add-client <id> <nome> <email> <cpf>
            list-clients
            create-order <customerId> <sku:qty> <sku:qty> ...
            reserve-stock <orderId>
            pay-order <orderId>
            cancel-order <orderId>
            report
            help
            exit
            ─────────────────────────────────────────────────────────
            """);
    }
}
