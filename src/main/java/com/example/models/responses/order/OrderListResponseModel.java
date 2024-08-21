package com.example.models.responses.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponseModel {
    private boolean success;
    private List<Order> orders;
    private int total;
    private int totalToday;
}
