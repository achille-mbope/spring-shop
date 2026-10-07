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
        // Arrange
        String body = """
            {
                "customerId": "customer-1",
                "itemName": "Item A",
                "quantity": 2,
                "unitPrice": 10.0
            }
        """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.customerId").value("customer-1"))
                .andExpect(jsonPath("$.itemName").value("Item A"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.total").value(20.0))
                .andExpect(jsonPath("$.unitPrice").value(10.0))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void createOrder_shouldReturn400WhenQuantityInvalid() throws Exception {
        // Arrange
        String body = """
            {
                "customerId": "customer-1",
                "itemName": "Item A",
                "quantity": 0,
                "unitPrice": 10.0
            }
        """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrder_shouldReturnOrderWhenExists()  throws Exception {
        String body = """
                {
                  "customerId": "customer-1",
                  "itemName": "item-A",
                  "quantity": 1,
                  "unitPrice": 5.0
                }
                """;
        
        String location = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("customer-1"))
                .andExpect(jsonPath("$.itemName").value("item-A"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.unitPrice").value(5.0));
    }

    @Test
    void getOrder_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelOrder_shouldReturnCancelledOrder() throws Exception {
        String body = """
                {
                  "customerId": "customer-1",
                  "itemName": "item-A",
                  "quantity": 1,
                  "unitPrice": 5.0
                }
                """;
        
        String location = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getHeader("Location");

        mockMvc.perform(post(location + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
