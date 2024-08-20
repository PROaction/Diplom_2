package com.example.models.requests.user;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserCreateRequestModel {
    private String email;
    private String password;
    private String name;
}
