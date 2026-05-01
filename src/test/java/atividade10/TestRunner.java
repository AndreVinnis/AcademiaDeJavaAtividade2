package atividade10;

import atividade10.enums.OrderStatus;
import atividade10.enums.ProductCategory;
import atividade10.exceptions.InsufficientStockException;
import atividade10.model.Customer;
import atividade10.model.Order;
import atividade10.model.Product;
import atividade10.repository.CustomerRepository;
import atividade10.repository.OrderRepository;
import atividade10.repository.ProductRepository;
import atividade10.service.CustomerService;
import atividade10.service.OrderService;
import atividade10.service.ProductService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// André Vinícius Barros Macambira
public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║            Executando testes...           ║");
        System.out.println("╚═══════════════════════════════════════════╝");

        runProductTests();
        runCustomerTests();
        runOrderTests();
        runOrderStatusTests();

        System.out.println("\n─────────────────────────────────────────────");
        System.out.printf("  Resultado: %d passaram | %d falharam%n", passed, failed);
        System.out.println("─────────────────────────────────────────────");
    }

    private static void test(String name, Runnable testCase) {
        try {
            testCase.run();
            System.out.println("  [OK] " + name);
            passed++;
        } catch (AssertionError e) {
            System.out.println("  [FALHOU] " + name + " → " + e.getMessage());
            failed++;
        } catch (Exception e) {
            System.out.println("  [ERRO] " + name + " → " + e.getMessage());
            failed++;
        }
    }

    private static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("esperado: " + expected + ", obtido: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void assertThrows(Class<? extends Exception> expectedEx, Runnable action) {
        try {
            action.run();
            throw new AssertionError("Esperava " + expectedEx.getSimpleName() + " mas nenhuma exceção foi lançada.");
        } catch (Exception e) {
            if (!expectedEx.isInstance(e)) {
                throw new AssertionError("Esperava " + expectedEx.getSimpleName()
                        + " mas obteve " + e.getClass().getSimpleName());
            }
        }
    }

    private static ProductService buildProductService() {
        return new ProductService(new ProductRepository());
    }

    private static CustomerService buildCustomerService() {
        return new CustomerService(new CustomerRepository());
    }

    private static OrderService buildOrderService(ProductRepository pr, CustomerRepository cr) {
        return new OrderService(new OrderRepository(), pr, cr);
    }


    private static void runProductTests() {
        System.out.println("\n── ProductService ───────────────────────────");

        test("Cadastrar produto com sucesso", () -> {
            ProductService service = buildProductService();
            Product p = service.addProduct("SKU001", "Geladeira", "ELECTRONICS", "2999,90", "10");
            assertEquals("SKU001", p.getSku());
            assertEquals(10, p.getAvailableStock());
        });

        test("Lançar exceção para SKU duplicado", () -> {
            ProductService service = buildProductService();
            service.addProduct("SKU001", "Geladeira", "ELECTRONICS", "2999,90", "10");
            assertThrows(IllegalArgumentException.class, () ->
                    service.addProduct("SKU001", "Fogao", "ELECTRONICS", "1499,90", "5"));
        });

        test("Lançar exceção para preço zero", () -> {
            assertThrows(IllegalArgumentException.class, () ->
                    buildProductService().addProduct("SKU001", "Geladeira", "ELECTRONICS", "0", "10"));
        });

        test("Lançar exceção para categoria inválida", () -> {
            assertThrows(IllegalArgumentException.class, () ->
                    buildProductService().addProduct("SKU001", "Geladeira", "INVALIDA", "2999,90", "10"));
        });

        test("Listar produtos ordenados por SKU", () -> {
            ProductService service = buildProductService();
            service.addProduct("SKU003", "Camiseta", "CLOTHING", "89,90", "50");
            service.addProduct("SKU001", "Geladeira", "ELECTRONICS", "2999,90", "10");
            service.addProduct("SKU002", "Fogao", "ELECTRONICS", "1499,90", "5");
            List<Product> products = service.listAll("sku");
            assertEquals("SKU001", products.get(0).getSku());
            assertEquals("SKU003", products.get(2).getSku());
        });

        test("Estoque disponível após reserva", () -> {
            ProductRepository repo = new ProductRepository();
            repo.save(new Product(ProductCategory.ELECTRONICS, "Geladeira", new BigDecimal("2999.90"), "SKU001", 10));
            Product p = repo.findById("SKU001").get();
            p.reserveStock(3);
            assertEquals(7, p.getAvailableStock());
        });

        test("Confirmar venda desconta estoque e reserva", () -> {
            Product p = new Product(ProductCategory.ELECTRONICS, "Geladeira", new BigDecimal("2999.90"), "SKU001", 10);
            p.reserveStock(3);
            p.confirmSale(3);
            assertEquals(7, p.getStock());
            assertEquals(0, p.getReservedStock());
        });

        test("Lançar exceção ao reservar mais do que disponível", () -> {
            Product p = new Product(ProductCategory.ELECTRONICS, "Geladeira", new BigDecimal("2999.90"), "SKU001", 10);
            assertThrows(InsufficientStockException.class, () -> p.reserveStock(11));
        });
    }


    private static void runCustomerTests() {
        System.out.println("\n── CustomerService ──────────────────────────");

        test("Cadastrar cliente com sucesso", () -> {
            CustomerService service = buildCustomerService();
            Customer c = service.createCustomer("João", "joao@email.com", "529.982.247-25");
            assertEquals("529.982.247-25", c.getCpf());
            assertEquals("João", c.getName());
        });

        test("Lançar exceção para CPF inválido", () -> {
            assertThrows(IllegalArgumentException.class, () ->
                    buildCustomerService().createCustomer("João", "joao@email.com", "737.063.481-60"));
        });

        test("Lançar exceção para email inválido", () -> {
            assertThrows(IllegalArgumentException.class, () ->
                    buildCustomerService().createCustomer("João", "emailinvalido", "529.982.247-25"));
        });

        test("Lançar exceção para email duplicado", () -> {
            CustomerService service = buildCustomerService();
            service.createCustomer("João", "joao@email.com", "529.982.247-25");
            assertThrows(IllegalArgumentException.class, () ->
                    service.createCustomer( "Maria", "joao@email.com", "529.982.247-25"));
        });

    }

    private static void runOrderTests() {
        System.out.println("\n── OrderService ─────────────────────────────");

        test("Criar pedido com sucesso", () -> {
            ProductRepository pr = new ProductRepository();
            CustomerRepository cr = new CustomerRepository();
            Customer customer = new Customer("529.982.247-25", "João", "joao@email.com", LocalDateTime.now());
            Product product = new Product(ProductCategory.ELECTRONICS, "Geladeira", new BigDecimal("2999.90"), "SKU001", 10);
            String customerId = "sdasdasdasd-dasdasdasd-32edasdasda";

            customer.setCustomerId(customerId);
            pr.save(product);
            cr.save(customer);
            OrderService service = buildOrderService(pr, cr);
            Order order = service.createOrder(customerId, Map.of("SKU001", 2));

            assertEquals(OrderStatus.PENDING, order.getStatus());
            assertEquals(1, order.getItems().size());
        });

        test("Reservar estoque move status para RESERVED", () -> {
            ProductRepository pr = new ProductRepository();
            CustomerRepository cr = new CustomerRepository();
            Customer customer = new Customer("529.982.247-25", "João", "joao@email.com", LocalDateTime.now());
            Product product = new Product(ProductCategory.ELECTRONICS, "Geladeira", new BigDecimal("2999.90"), "SKU001", 10);
            String customerId = "sdasdasdasd-dasdasdasd-32edasdasda";

            customer.setCustomerId(customerId);
            pr.save(product);
            cr.save(customer);
            OrderService service = buildOrderService(pr, cr);
            Order order = service.createOrder(customerId, Map.of("SKU001", 2));
            service.reserveStock(order.getOrderId());

            assertEquals(OrderStatus.RESERVED, order.getStatus());
            assertEquals(8, pr.findById("SKU001").get().getAvailableStock());
        });

        test("Cancelar pedido RESERVED libera estoque", () -> {
            ProductRepository pr = new ProductRepository();
            CustomerRepository cr = new CustomerRepository();
            Customer customer = new Customer("529.982.247-25", "João", "joao@email.com", LocalDateTime.now());
            Product product = new Product(ProductCategory.ELECTRONICS, "Geladeira", new BigDecimal("2999.90"), "SKU001", 10);
            String customerId = "sdasdasdasd-dasdasdasd-32edasdasda";

            customer.setCustomerId(customerId);
            pr.save(product);
            cr.save(customer);
            OrderService service = buildOrderService(pr, cr);
            Order order = service.createOrder(customerId, Map.of("SKU001", 3));
            service.reserveStock(order.getOrderId());
            service.cancelOrder(order.getOrderId());

            assertEquals(OrderStatus.CANCELLED, order.getStatus());
            assertEquals(10, pr.findById("SKU001").get().getAvailableStock());
        });

        test("Lançar exceção ao pagar pedido PENDING", () -> {
            ProductRepository pr = new ProductRepository();
            CustomerRepository cr = new CustomerRepository();
            Customer customer = new Customer("529.982.247-25", "João", "joao@email.com", LocalDateTime.now());
            Product product = new Product(ProductCategory.ELECTRONICS, "Geladeira", new BigDecimal("2999.90"), "SKU001", 10);
            String customerId = "sdasdasdasd-dasdasdasd-32edasdasda";

            customer.setCustomerId(customerId);
            pr.save(product);
            cr.save(customer);
            OrderService service = buildOrderService(pr, cr);
            Order order = service.createOrder(customerId, Map.of("SKU001", 1));
            assertThrows(IllegalStateException.class, () ->
                    service.payOrder(order.getOrderId()));
        });

        test("Lançar exceção para estoque insuficiente", () -> {
            ProductRepository pr = new ProductRepository();
            CustomerRepository cr = new CustomerRepository();
            Customer customer = new Customer("529.982.247-25", "João", "joao@email.com", LocalDateTime.now());
            Product product = new Product(ProductCategory.ELECTRONICS, "Geladeira", new BigDecimal("2999.90"), "SKU001", 10);
            String customerId = "sdasdasdasd-dasdasdasd-32edasdasda";

            customer.setCustomerId(customerId);
            pr.save(product);
            cr.save(customer);
            OrderService service = buildOrderService(pr, cr);
            Order order = service.createOrder(customerId, Map.of("SKU001", 99));
            assertThrows(RuntimeException.class, () ->
                    service.reserveStock(order.getOrderId()));
        });
    }

    private static void runOrderStatusTests() {
        System.out.println("\n── OrderStatus ──────────────────────────────");

        test("PENDING → RESERVED permitido", () ->
                assertTrue(OrderStatus.PENDING.canTransitionTo(OrderStatus.RESERVED), "deveria permitir"));

        test("PENDING → CANCELLED permitido", () ->
                assertTrue(OrderStatus.PENDING.canTransitionTo(OrderStatus.CANCELLED), "deveria permitir"));

        test("RESERVED → PAID permitido", () ->
                assertTrue(OrderStatus.RESERVED.canTransitionTo(OrderStatus.PAID), "deveria permitir"));

        test("PENDING → PAID bloqueado", () ->
                assertTrue(!OrderStatus.PENDING.canTransitionTo(OrderStatus.PAID), "deveria bloquear"));

        test("PAID → CANCELLED bloqueado", () ->
                assertTrue(!OrderStatus.PAID.canTransitionTo(OrderStatus.CANCELLED), "deveria bloquear"));

        test("CANCELLED → RESERVED bloqueado", () ->
                assertTrue(!OrderStatus.CANCELLED.canTransitionTo(OrderStatus.RESERVED), "deveria bloquear"));
    }
}
