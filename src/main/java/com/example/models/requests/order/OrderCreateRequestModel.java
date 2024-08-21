package com.example.models.requests.order;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderCreateRequestModel {
    private List<String> ingredients;
}
