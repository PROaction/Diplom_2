package com.example;

import io.restassured.response.Response;

public class IngredientApi extends BaseHttpClient {
    private String apiPath = "/api/ingredients";

    public Response getIngredients() {
        return doGetRequest(apiPath);
    }
}
