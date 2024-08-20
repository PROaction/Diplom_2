package com.example;

import com.example.models.responses.ErrorResponseModel;
import com.example.models.responses.UserGetCreateResponseModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    public UserApi userApi = new UserApi();

    public String email = "test.user@yandex.ru";
    private final String password = "123";
    public String userName = "proaction";

    @Test
    public void testCreateUser() {
        Response response = userApi.createUser(
                email,
                password,
                userName
        );
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testCreateDuplicateUser() {
        Response responseFirstUser = userApi.createUser(
                email,
                password,
                userName
        );
        assertEquals(200, responseFirstUser.statusCode());

        Response responseSecondUser = userApi.createUser(email, password, userName);
        assertEquals(403, responseSecondUser.statusCode());
    }

    @Test
    public void testLogin() {
        Response responseCreateUser = userApi.createUser(
                email,
                password,
                userName
        );
        assertEquals(200, responseCreateUser.statusCode());

        Response responseGetUser = userApi.getUser(email, password);
        assertEquals(200, responseGetUser.statusCode());
    }

    @Test
    public void testLoginWithIncorrectData() {
        Response responseCreateUser = userApi.createUser(
                email,
                password,
                userName
        );
        assertEquals(200, responseCreateUser.statusCode());

        Response responseGetUser = userApi.getUser("gfds@qwe.ru", "1");
        assertEquals(401, responseGetUser.statusCode());

        ErrorResponseModel error = responseGetUser.then().extract().body().as(ErrorResponseModel.class);
        assertFalse(error.isSuccess());
        assertEquals(error.getMessage(), "email or password are incorrect");
    }

    @ParameterizedTest
    @CsvSource({
            ",123,proaction",  // Пропущен email
            "test.user@yandex.ru,,proaction",  // Пропущен пароль
            "test.user@yandex.ru,123,"  // Пропущено имя
    })
    public void testInvalidUserCreation(String email, String password, String name) {
        Response response = userApi.createUser(email, password, name);
        assertEquals(403, response.statusCode());
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
