package com.example;

import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;

import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class BaseHttpClient {

    public static RequestSpecification baseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(URL.BASE_URL)
                .addHeader("Content-Type", "application/json")
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .build();

    }

    protected Response doGetRequest(String path) {
        return given().spec(baseRequestSpec())
                .get(path)
                .thenReturn();
    }

    protected Response doGetRequest(String path, Map<String, Object> params) {
        return given().spec(baseRequestSpec())
                .params(params)
                .get(path)
                .thenReturn();
    }

    protected Response doPostRequest(String path, Object body) {
        return given().spec(baseRequestSpec())
                .body(body)
                .post(path)
                .thenReturn();
    }

    protected Response doPostRequest(String path, Map<String, Object> params) {
        return given().spec(baseRequestSpec())
                .params(params)
                .post(path)
                .thenReturn();
    }

    protected Response doPutRequest(String path, Object body) {
        return given().spec(baseRequestSpec())
                .body(body)
                .put(path)
                .thenReturn();
    }

    protected Response doPutRequest(String path, Map<String, Object> params) {
        return given().spec(baseRequestSpec())
                .params(params)
                .put(path)
                .thenReturn();
    }

    protected Response doPatchRequest(String path, Object body, String token) {
        return given().spec(baseRequestSpec())
                .header("Authorization", token)
                .body(body)
                .patch(path)
                .thenReturn();
    }

    protected Response doPatchRequest(String path, Map<String, Object> params, String token) {
        return given().spec(baseRequestSpec())
                .header("Authorization", token)
                .params(params)
                .patch(path)
                .thenReturn();
    }

    protected Response doDeleteRequest(String path, String token) {
        return given().spec(baseRequestSpec())
                .header("Authorization", token)
                .delete(path)
                .thenReturn();
    }
}
