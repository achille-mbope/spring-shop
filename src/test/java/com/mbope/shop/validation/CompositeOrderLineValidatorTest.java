package com.mbope.shop.validation;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mbope.shop.model.OrderLineRequest;

class CompositeOrderLineValidatorTest {
    private final CompositeOrderLineValidator validator = new CompositeOrderLineValidator(
            List.of(new ItemNameValidator(), new QuantityValidator(), new UnitPriceValidator())
    );

    @Test 
    void validate_shouldThrowOnFirstInvalidLine(){
        List<OrderLineRequest> lines = List.of(
            new OrderLineRequest("Item A", 1, 10.0),
            new OrderLineRequest("Item B", 0, 7.99), // Invalid
            new OrderLineRequest("Item C", 4, 15.95)
        );

        assertThatThrownBy(() -> validator.validate(lines))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Menge muss größer als 0 sein");
    }

    @Test 
    void validate_shouldPassWhenAllLinesValid() {
        List<OrderLineRequest> lines = List.of(
            new OrderLineRequest("Item A", 1, 10.0),
            new OrderLineRequest("Item B", 5, 7.99),
            new OrderLineRequest("Item C", 4, 15.95)
        );

        assertThatCode(() -> validator.validate(lines)).doesNotThrowAnyException();
    }
}
