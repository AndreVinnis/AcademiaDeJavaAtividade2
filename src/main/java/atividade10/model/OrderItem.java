package atividade10.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

// André Vinícius Barros Macambira
public class OrderItem {

    private String sku;
    private int quantity;
    private BigDecimal unityPrice;

    public OrderItem() {
    }

    public OrderItem(int quantity, String sku, BigDecimal unityPrice) {
        this.quantity = quantity;
        this.sku = sku;
        this.unityPrice = unityPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getUnityPrice() {
        return unityPrice;
    }

    public void setUnityPrice(BigDecimal unityPrice) {
        this.unityPrice = unityPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity && Objects.equals(sku, orderItem.sku) && Objects.equals(unityPrice, orderItem.unityPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, quantity, unityPrice);
    }

    public BigDecimal subtotal(){
        return unityPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }
}
