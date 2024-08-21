package com.example;

import com.example.models.requests.order.OrderCreateRequestModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

public class OrderApi extends BaseHttpClient {
    private String apiPath = "/api/orders";

    @Step("Создание заказа с авторизацией")
    public Response createOrder(List<String> ingredients, String token) {
        OrderCreateRequestModel request = new OrderCreateRequestModel(ingredients);

        return doPostRequest(apiPath, request, token);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrder(List<String> ingredients) {
        OrderCreateRequestModel request = new OrderCreateRequestModel(ingredients);

        return doPostRequest(apiPath, request);
    }

    @Step("Получение заказа пользователя")
    public Response getOrderByUser(String token) {
        return doGetRequest(apiPath, token);
    }

    @Step("Получение заказа пользователя без токена")
    public Response getOrderByUser() {
        return doGetRequest(apiPath);
    }
}
