package atividade10.repository;

import atividade10.enums.ProductCategory;
import atividade10.model.Product;

import java.util.*;
import java.util.stream.Collectors;

// André Vinícius Barros Macambira
public class ProductRepository implements Repository<Product, String>{
    private final Map<String, Product> store = new HashMap<>();

    @Override
    public void save(Product product) {
        store.put(product.getSku(), product);
    }

    @Override
    public Optional<Product> findById(String sku) {
        return Optional.ofNullable(store.get(sku));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(String sku) {
        store.remove(sku);
    }

    @Override
    public boolean existsById(String sku) {
        return store.containsKey(sku);
    }

    public List<Product> findAllSortedBySku() {
        return store.values().stream()
                .sorted(Comparator.comparing(Product::getSku))
                .collect(Collectors.toList());
    }

    public List<Product> findAllSortedByPrice() {
        return store.values().stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());
    }

    public List<Product> findByCategory(ProductCategory category) {
        return store.values().stream()
                .filter(p -> p.getCategory() == category)
                .collect(Collectors.toList());
    }
}
