package com.mbope.shop.model;

import java.util.Objects;

public class Order {
    private Long id;
    private String customerId;
    private String itemName;
    private int quantity;
    private double unitPrice;
    private OrderStatus status;

    public Order(String customerId, String itemName, int quantity, double unitPrice) {
        this(null, customerId, itemName, quantity, unitPrice);
    }

    public Order(Long id, String customerId, String itemName, int quantity, double unitPrice) {
        this.id = id;
        this.customerId = customerId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.status = OrderStatus.NEW;
    }

    public Long getId() {
        return id;
    }

    /**
     * Nur für Repositories gedacht, die die ID setzen müssen, z.B. InMemoryOrderRepository.
     * Wirft eine IllegalStateException, wenn die ID bereits gesetzt ist.
     * 
     * @param id
     */
    public void setId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("ID ist bereits gesetzt: " + this.id);
        }
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

    public OrderStatus getStatus() {
        return status;
    }


    public void cancel() {
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Bestellung kann nicht storniert werden im Status: " + status);
        }
        this.status = OrderStatus.CANCELLED;
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

