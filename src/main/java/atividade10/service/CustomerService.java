package atividade10.service;

import atividade10.exceptions.ObjectNotFoundException;
import atividade10.model.Customer;
import atividade10.repository.CustomerRepository;
import java.time.LocalDateTime;
import java.util.List;

// André Vinícius Barros Macambira
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer createCustomer(String name, String email, String cpf) {
        validateName(name);
        validateEmail(email);
        validateCpf(cpf);

        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Este email já está em uso: " + email);
        }

        Customer newCustomer = new Customer(cpf, email, name, LocalDateTime.now());
        repository.save(newCustomer);
        return newCustomer;
    }

    public List<Customer> listAll() {
        List<Customer> customers = repository.findAll();
        if (customers.isEmpty()) {
            throw new RuntimeException("Nenhum usuário registrado.");
        }
        return customers;
    }

    public Customer findById(String id){
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Nome muito curto: " + name);
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser vazio.");
        }
        if (!(email.contains("@") && email.contains("."))) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
    }

    private void validateCpf(String cpf){
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF não pode ser vazio.");
        }
        String digits = cpf.replaceAll("[^0-9]", "");

        if (digits.length() != 11){
            throw new IllegalArgumentException("CPF deve ter 11 dígitos: " + cpf);
        }

        if (!isValidCpfChecksum(digits)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
    }

    private boolean isValidCpfChecksum(String digits) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * (10 - i);
        }
        int first = 11 - (sum % 11);
        if (first >= 10){
            first = 0;
        }
        if (first != (digits.charAt(9) - '0')){
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (digits.charAt(i) - '0') * (11 - i);
        }
        int second = 11 - (sum % 11);
        if (second >= 10){
            second = 0;
        }
        return second == (digits.charAt(10) - '0');
    }
}
