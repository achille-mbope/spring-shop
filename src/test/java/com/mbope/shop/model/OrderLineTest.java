package com.mbope.shop.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderLineTest {
    
    @Test 
    void getTotal_shouldMultiplyQuantityAndUnitPrice() {
        OrderLine line = new OrderLine("Item A",3,2.5);

        assertThat(line.getTotal()).isEqualTo(7.5);
    }    

    void getters_shouldReturnConstructorValues() {
        OrderLine line = new OrderLine("Item B",4,2.5);

        assertThat(line.getItemName()).isEqualTo("Item B");
        assertThat(line.getQuantity()).isEqualTo(3);
        assertThat(line.getUnitPrice()).isEqualTo(2.5);
    }

}
