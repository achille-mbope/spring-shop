package com.mbope.shop.model;

import java.util.Objects;

public class Order {
    private Long id;
    private String customerId;
    private String itemName;
    private int quantity;
    private double unitPrice;

    public Order() {
    }

    public Order(Long id, String customerId, String itemName, int quantity, double unitPrice) {
        this.id = id;
        this.customerId = customerId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return quantity * unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return quantity == order.quantity
                && Double.compare(order.unitPrice, unitPrice) == 0
                && Objects.equals(id, order.id)
                && Objects.equals(customerId, order.customerId)
                && Objects.equals(itemName, order.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, itemName, quantity, unitPrice);
    }
}

