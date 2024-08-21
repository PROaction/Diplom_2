package com.example.models.responses.order;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCreateResponseModel {
    private boolean success;
    private String name;
    private OrderResponseModel order;
}
