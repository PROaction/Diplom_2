package com.example;

import com.example.models.requests.user.UserCreateRequestModel;
import com.example.models.requests.user.UserGetReuqestModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserApi extends BaseHttpClient {
    private final String apiPath = "/api/auth";

    @Step("Создание пользователя")
    public Response createUser(String email, String password, String name) {
        UserCreateRequestModel request = new UserCreateRequestModel(email, password, name);

        return doPostRequest(apiPath + "/register", request);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String token) {
        return doDeleteRequest(apiPath + "/user", token);
    }

    @Step("Получение пользователя")
    public Response getUser(String email, String password) {
        UserGetReuqestModel request = new UserGetReuqestModel(email, password);

        return doPostRequest(apiPath + "/login", request);
    }
}
