package com.mbope.shop.model;

public class OrderRequest {
    private final String customerId;
    private final String itemName;
    private final int quantity;
    private final double unitPrice;

    public OrderRequest(String customerId, String itemName, int quantity, double unitPrice) {
        this.customerId = customerId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
}
