package atividade10;

import atividade10.repository.CustomerRepository;
import atividade10.repository.OrderRepository;
import atividade10.repository.ProductRepository;
import atividade10.service.CustomerService;
import atividade10.service.OrderService;
import atividade10.service.ProductService;
import atividade10.service.ReportService;
import atividade10.view.CommandHandler;

import java.util.Scanner;

public class Main {

    /* Olá professor, Julio! Esse sistema é mais complexo de se testar devido à quantidade de funcionalidades.
    * Para facilitar e para testar o programa, eu fiz uma classe de teste que simula os teste unitários, ela
    * está no pacote de teste.
    * Além disso, vou deixar o scripts para criação de 3 usuários, 5 produtos e 4 pedidos:
    * Customers:
    * add-client João joao@email.com 529.982.247-25
    * add-client Maria maria@email.com 871.843.897-00
    * add-client Pedro pedro@email.com 737.063.481-79
    *
    * Products:
    * add-product SKU001 Geladeira ELECTRONICS 2999,90 10
    * add-product SKU002 Fogao ELECTRONICS 1499,90 5
    * add-product SKU003 Camiseta CLOTHING 89,90 50
    * add-product SKU004 Notebook ELECTRONICS 4599,90 8
    * add-product SKU005 Livro-Java BOOKS 149,90 30
    *
    * Orders: (Essa não está completo, pois precisa do id do usuário que será mostrado quando o usuário for criado)
    * create-order (userID1) SKU001:1 SKU005:2
    * create-order (userID2) SKU003:3 SKU005:1
    * create-order (userID2) SKU004:1 SKU001:1
    * create-order (userID1)SKU002:1 SKU003:2
    */

    public static void main(String[] args) {
        ProductRepository productRepository   = new ProductRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        OrderRepository orderRepository = new OrderRepository();

        ProductService productService   = new ProductService(productRepository);
        CustomerService customerService = new CustomerService(customerRepository);
        OrderService orderService       = new OrderService(orderRepository, productRepository, customerRepository);
        ReportService relatorioService = new ReportService(orderRepository, productRepository, customerRepository);

        CommandHandler handler = new CommandHandler(productService, customerService, orderService, relatorioService);

        printBanner();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine();
            handler.handle(input);
        }
    }

    private static void printBanner() {
        System.out.println("""
            ╔═══════════════════════════════════════════╗
            ║   Simulador de Processamento de Pedidos   ║
            ║              E-Commerce CLI               ║
            ╚═══════════════════════════════════════════╝
            Digite 'help' para ver os comandos disponíveis.
            """);
    }
}
