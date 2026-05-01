package atividade10.repository;

import atividade10.model.Customer;
import java.util.*;

// André Vinícius Barros Macambira
public class CustomerRepository implements Repository<Customer, String> {
    private final Map<String, Customer> store = new HashMap<>();

    @Override
    public void save(Customer customer) {
        store.put(customer.getCustomerId(), customer);
    }

    @Override
    public Optional<Customer> findById(String customerId) {
        return Optional.ofNullable(store.get(customerId));
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(String customerId) {
        store.remove(customerId);
    }

    @Override
    public boolean existsById(String customerId) {
        return store.containsKey(customerId);
    }

    public Optional<Customer> findByEmail(String email) {
        return store.values().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}
