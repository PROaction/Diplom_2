package com.example;


import com.example.models.responses.ErrorResponseModel;
import com.example.models.responses.ingredient.GetIngredientResponseModel;
import com.example.models.responses.ingredient.Product;
import com.example.models.responses.order.Order;
import com.example.models.responses.order.OrderCreateResponseModel;
import com.example.models.responses.order.OrderListResponseModel;
import com.example.models.responses.user.UserGetCreateResponseModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    public UserApi userApi = new UserApi();
    public OrderApi orderApi = new OrderApi();
    public IngredientApi ingredientApi = new IngredientApi();

    public String email = "test.user@yandex.ru";
    private final String password = "123";
    public String userName = "proaction";

    @Test
    public void testCreateOrderWithAuth() {
        // Создание пользователя.
        Response responseUser = userApi.createUser(
                email,
                password,
                userName
        );
        assertEquals(200, responseUser.statusCode());
        UserGetCreateResponseModel user = responseUser.then().extract().body().as(UserGetCreateResponseModel.class);
        String token = user.getAccessToken();

        // Получение интеградинетов для создания заказа.
        Response responseIngredient = ingredientApi.getIngredients();
        assertEquals(200, responseIngredient.statusCode());
        GetIngredientResponseModel ingredientBody = responseIngredient.then().extract().body()
                .as(GetIngredientResponseModel.class);
        String ingredientId = ingredientBody.getData().get(0).get_id();

        // Создание заказа.
        List<String> ingredientList = new ArrayList<>();
        ingredientList.add(ingredientId);

        Response responseOrder = orderApi.createOrder(ingredientList, token);
        assertEquals(200, responseOrder.statusCode());

        OrderCreateResponseModel orderResponse = responseOrder.then().extract().body()
                .as(OrderCreateResponseModel.class);
        assertTrue(orderResponse.isSuccess());
        assertThat(orderResponse.getName()).isNotEmpty();
        assertThat(orderResponse.getOrder().getNumber()).isInstanceOf(Integer.class);


        // Получение заказа пользователя.
        Response responseGetOrder = orderApi.getOrderByUser(token);
        assertEquals(200, responseGetOrder.statusCode());

        // Проверка ответа на получение заказа пользователя
        OrderListResponseModel orderListResponse = responseGetOrder.then().extract().body()
                .as(OrderListResponseModel.class);

        // success = true
        assertTrue(orderListResponse.isSuccess());

        // список orders не пуст
        assertThat(orderListResponse.getOrders()).isNotEmpty();

        // ingredients содержит ожидаемый ingredientId
        assertThat(orderListResponse.getOrders().get(0).getIngredients()).contains(ingredientId);

        // остальные поля не null
        Order order = orderListResponse.getOrders().get(0);
        assertThat(order.get_id()).isNotNull();
        assertThat(order.getStatus()).isNotNull();
        assertThat(order.getName()).isNotNull();
        assertThat(order.getCreatedAt()).isNotNull();
        assertThat(order.getUpdatedAt()).isNotNull();
        assertThat(order.getNumber()).isInstanceOf(Integer.class);
    }

    @Test
    public void testCreateOrderWithoutAuth() {
        // Получение интеградинетов для создания заказа.
        Response responseIngredient = ingredientApi.getIngredients();
        assertEquals(200, responseIngredient.statusCode());
        GetIngredientResponseModel ingredientBody = responseIngredient.then().extract().body()
                .as(GetIngredientResponseModel.class);
        String ingredientId = ingredientBody.getData().get(0).get_id();

        // Создание заказа.
        List<String> ingredientList = new ArrayList<>();
        ingredientList.add(ingredientId);

        Response responseOrder = orderApi.createOrder(ingredientList);
        assertEquals(200, responseOrder.statusCode());

        OrderCreateResponseModel orderResponse = responseOrder.then().extract().body()
                .as(OrderCreateResponseModel.class);
        assertTrue(orderResponse.isSuccess());
        assertThat(orderResponse.getName()).isNotEmpty();
        assertThat(orderResponse.getOrder().getNumber()).isInstanceOf(Integer.class);

        // Получение заказа пользователя.
        Response responseGetOrder = orderApi.getOrderByUser();
        assertEquals(401, responseGetOrder.statusCode());

        // Проверка ответа на получение заказа неавторизованного пользователя пользователя
        ErrorResponseModel error = responseGetOrder.then().extract().body()
                .as(ErrorResponseModel.class);
        assertFalse(error.isSuccess());
        assertEquals(error.getMessage(), "You should be authorised");

    }

    @Test
    public void testCreateOrderWithAuthAndWithoutIngredients() {
        // Создание пользователя.
        Response responseUser = userApi.createUser(
                email,
                password,
                userName
        );
        assertEquals(200, responseUser.statusCode());
        UserGetCreateResponseModel user = responseUser.then().extract().body().as(UserGetCreateResponseModel.class);
        String token = user.getAccessToken();

        // Создание заказа.
        List<String> ingredientList = new ArrayList<>();

        Response responseOrder = orderApi.createOrder(ingredientList, token);
        assertEquals(400, responseOrder.statusCode());

        ErrorResponseModel orderResponse = responseOrder.then().extract().body()
                .as(ErrorResponseModel.class);
        assertFalse(orderResponse.isSuccess());
        assertEquals(orderResponse.getMessage(), "Ingredient ids must be provided");
    }

    @Test
    public void testCreateOrderWithoutAuthAndInvalidIngredient() {
        // Получение интеградинетов для создания заказа.
        Response responseIngredient = ingredientApi.getIngredients();
        assertEquals(200, responseIngredient.statusCode());
        GetIngredientResponseModel ingredientBody = responseIngredient.then().extract().body()
                .as(GetIngredientResponseModel.class);
        String ingredientId = ingredientBody.getData().get(0).get_id();

        // Создание заказа.
        List<String> ingredientList = new ArrayList<>();
        ingredientList.add(ingredientId + "123");

        Response responseOrder = orderApi.createOrder(ingredientList);
        assertEquals(400, responseOrder.statusCode());

        ErrorResponseModel orderResponse = responseOrder.then().extract().body()
                .as(ErrorResponseModel.class);
        assertFalse(orderResponse.isSuccess());
        assertEquals(orderResponse.getMessage(), "One or more ids provided are incorrect");
    }

    @AfterEach
    @Step("tearDown")
    public void tearDown() {
        UserGetCreateResponseModel userGetResponse = userApi.getUser(
                email,
                password
        ).as(UserGetCreateResponseModel.class);
        if (userGetResponse.isSuccess()) {
            Response response = userApi.deleteUser(userGetResponse.getAccessToken());
            assertEquals(202, response.statusCode());
        }
    }
}
