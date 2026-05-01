package atividade10.model;

import atividade10.enums.ProductCategory;
import atividade10.exceptions.InsufficientStockException;
import java.math.BigDecimal;
import java.util.Objects;

// André Vinícius Barros Macambira
public class Product {

    private String sku;
    private String name;
    private ProductCategory category;
    private BigDecimal price;
    private int stock;
    private int reservedStock;

    public Product() {
    }

    public Product(ProductCategory category, String name, BigDecimal price, String sku, int stock) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.reservedStock = 0;
        this.sku = sku;
        this.stock = stock;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getReservedStock() {
        return reservedStock;
    }

    public void setReservedStock(int reservedStock) {
        this.reservedStock = reservedStock;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return stock == product.stock && reservedStock == product.reservedStock && Objects.equals(sku, product.sku) && Objects.equals(name, product.name) && category == product.category && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, name, category, price, stock, reservedStock);
    }

    public void reserveStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantidade para reserva deve ser maior que zero: " + quantity);
        }
        if (getAvailableStock() < quantity) {
            throw new InsufficientStockException(
                    "Estoque insuficiente para reserva do produto " + sku +
                            ". Disponível: " + getAvailableStock() + ", solicitado: " + quantity);
        }
        this.reservedStock += quantity;
    }

    public void releaseReservation(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantidade para liberação deve ser maior que zero: " + quantity);
        }
        if (quantity > this.reservedStock) {
            throw new IllegalStateException(
                    "Não é possível liberar mais do que o reservado. " +
                            "Reservado: " + this.reservedStock + ", solicitado: " + quantity);
        }
        this.reservedStock -= quantity;
    }

    public void confirmSale(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantidade para confirmação deve ser maior que zero: " + quantity);
        }
        if (quantity > this.reservedStock) {
            throw new IllegalStateException(
                    "Não é possível confirmar venda sem reserva prévia. " +
                            "Reservado: " + this.reservedStock + ", solicitado: " + quantity);
        }
        this.stock -= quantity;
        this.reservedStock -= quantity;
    }

    public int getAvailableStock(){
        return stock - reservedStock;
    }
}
