package atividade10.service;

import atividade10.enums.ProductCategory;
import atividade10.exceptions.ObjectNotFoundException;
import atividade10.model.Product;
import atividade10.repository.ProductRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

// André Vinícius Barros Macambira
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }


    public Product addProduct(String sku, String name, String categoryStr, String priceStr, String stockStr) {
        validateSku(sku);
        validateName(name);

        ProductCategory category = parseCategory(categoryStr);
        BigDecimal price = parsePrice(priceStr);
        int stock = parseStock(stockStr);

        if (repository.existsById(sku)) {
            throw new IllegalArgumentException("Esse produto já existe no sistema: " + sku);
        }

        Product product = new Product(category, name, price, sku, stock);
        repository.save(product);
        return product;
    }

    public Product updateStock(String sku, String quantityStr) {
        Product product = findById(sku);
        int quantity = parseStock(quantityStr);
        product.setStock(product.getStock() + quantity);
        repository.save(product);
        return product;
    }

    public Product findById(String sku) {
        return repository.findById(sku).orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado: " + sku));
    }

    public List<Product> listAll(String sortBy) {
        List<Product> products;
        switch (sortBy.toLowerCase()) {
            case "price":
                products = repository.findAllSortedByPrice();
                break;
            case "sku":
                products = repository.findAllSortedBySku();
                break;
            default:
                throw new IllegalArgumentException("Modelo de ordenação inexistente: " + sortBy + ". Use 'sku' ou 'price'.");
        }

        if (products.isEmpty()) {
            throw new RuntimeException("Nenhum produto registrado.");
        }

        return products;
    }

    public List<Product> listByCategory(String categoryStr) {
        ProductCategory category = parseCategory(categoryStr);
        List<Product> products = repository.findByCategory(category);

        if (products.isEmpty()) {
            throw new RuntimeException("Nenhum produto entrado por essa categoria: " + categoryStr);
        }

        return products;
    }

    private void validateSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU não pode ser vazio.");
        }
        if (!sku.matches("^[A-Za-z0-9\\-_]{3,20}$")) {
            throw new IllegalArgumentException(
                    "SKU deve ter entre 3 e 20 caracteres alfanuméricos: " + sku);
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Nome do produto muito curto: " + name);
        }
    }

    private ProductCategory parseCategory(String categoryStr) {
        try {
            return ProductCategory.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Categoria inválida: " + categoryStr +
                            ". Opções válidas: " + Arrays.toString(ProductCategory.values()));
        }
    }

    private BigDecimal parsePrice(String priceStr) {
        try {
            BigDecimal price = new BigDecimal(priceStr.replace(",", "."));
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "Preço deve ser maior que zero: " + priceStr);
            }
            return price.setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de preço inválido: " + priceStr);
        }
    }

    private int parseStock(String stockStr) {
        try {
            int stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                throw new IllegalArgumentException("Estoque não pode ser negativo: " + stockStr);
            }
            return stock;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de estoque inválido: " + stockStr);
        }
    }
}
