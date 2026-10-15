package com.mbope.shop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createOrder_shouldReturn201WithOrderJson() throws Exception {
        String body = """
                {
                  "customerId": "customer-1",
                  "lines": [
                    { "itemName": "item-A", "quantity": 2, "unitPrice": 10.0 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.customerId").value("customer-1"))
                .andExpect(jsonPath("$.lines").isArray())
                .andExpect(jsonPath("$.lines[0].itemName").value("item-A"))
                .andExpect(jsonPath("$.lines[0].quantity").value(2))
                .andExpect(jsonPath("$.lines[0].unitPrice").value(10.0))
                .andExpect(jsonPath("$.total").value(20.0))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void createOrder_shouldReturn201WithMultipleLines() throws Exception {
        String body = """
                {
                  "customerId": "customer-1",
                  "lines": [
                    { "itemName": "item-A", "quantity": 2, "unitPrice": 10.0 },
                    { "itemName": "item-B", "quantity": 1, "unitPrice": 5.0 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lines").isArray())
                .andExpect(jsonPath("$.lines.length()").value(2))
                .andExpect(jsonPath("$.lines[0].itemName").value("item-A"))
                .andExpect(jsonPath("$.lines[1].itemName").value("item-B"))
                .andExpect(jsonPath("$.total").value(25.0));
    }

    @Test
    void createOrder_shouldReturn400WhenQuantityInvalid() throws Exception {
        String body = """
                {
                  "customerId": "customer-1",
                  "lines": [
                    { "itemName": "item-A", "quantity": 0, "unitPrice": 10.0 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrder_shouldReturnOrderWhenExists() throws Exception {
        String body = """
                {
                  "customerId": "customer-1",
                  "lines": [
                    { "itemName": "item-A", "quantity": 1, "unitPrice": 5.0 }
                  ]
                }
                """;

        String location = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("customer-1"))
                .andExpect(jsonPath("$.lines[0].itemName").value("item-A"));
    }

    @Test
    void getOrder_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelOrder_shouldReturnCancelledOrder() throws Exception {
        String body = """
                {
                  "customerId": "customer-1",
                  "lines": [
                    { "itemName": "item-A", "quantity": 1, "unitPrice": 5.0 }
                  ]
                }
                """;

        String location = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(post(location + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}