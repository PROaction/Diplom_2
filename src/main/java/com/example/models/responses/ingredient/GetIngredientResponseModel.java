package com.example.models.responses.ingredient;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetIngredientResponseModel {
    private boolean success;
    private List<Product> data;
}
