package com.mbope.shop.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {
    private final String customerId;
    private final String itemName;
    private final int quantity;
    private final double unitPrice;

    @JsonCreator 
    public OrderRequest(
        @JsonProperty("customerId") String customerId, 
        @JsonProperty("itemName") String itemName, 
        @JsonProperty("quantity") int quantity, 
        @JsonProperty("unitPrice") double unitPrice
    ) {
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
