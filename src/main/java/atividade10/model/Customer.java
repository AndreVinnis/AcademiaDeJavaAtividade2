package atividade10.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

// André Vinícius Barros Macambira
public class Customer {

    private String customerId;
    private String name;
    private String email;
    private String cpf;
    private LocalDateTime registeredAt;

    public Customer() {
    }

    public Customer(String cpf, String email, String name, LocalDateTime registeredAt) {
        this.cpf = cpf;
        this.customerId = UUID.randomUUID().toString();
        this.email = email;
        this.name = name;
        this.registeredAt = registeredAt;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId) && Objects.equals(name, customer.name) && Objects.equals(email, customer.email) && Objects.equals(registeredAt, customer.registeredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, name, email, registeredAt);
    }
}
