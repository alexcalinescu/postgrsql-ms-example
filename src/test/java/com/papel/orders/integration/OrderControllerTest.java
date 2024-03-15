package com.papel.orders.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Tag("ComponentTest")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateOrderSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/papel/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "order_number" : "RO-XYZ-0129",
                                    "order_items":  [
                                        {
                                            "product_name":  "Printer",
                                            "quantity": 1,
                                            "price": 350.50
                                        },
                                		{
                                            "product_name":  "Ink set",
                                            "quantity": 1,
                                            "price": 39.99
                                        }
                                    ]
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testCreateOnExistingOrder() throws Exception {
        String validCreatePayload = """
                                {
                                    "order_number" : "RO-XYZ-0129",
                                    "order_items":  [
                                        {
                                            "product_name":  "Printer",
                                            "quantity": 1,
                                            "price": 350.50
                                        },
                                		{
                                            "product_name":  "Ink set",
                                            "quantity": 1,
                                            "price": 39.99
                                        }
                                    ]
                                }
                                """;


        mockMvc.perform(MockMvcRequestBuilders.post("/papel/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreatePayload))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/papel/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreatePayload))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testCreateOrderInvalidValueForFieldsInPayload() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/papel/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "order_number" : "RO-XYZ-0129",
                                    "order_items":  [
                                        {
                                            "product_name":  "Printer",
                                            "quantity": -1,
                                            "price": 350.50
                                        },
                                		{
                                            "product_name":  "",
                                            "quantity": 1,
                                            "price": 39.99
                                        }
                                    ]
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
