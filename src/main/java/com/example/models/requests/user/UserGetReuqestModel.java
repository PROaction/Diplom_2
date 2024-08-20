package com.example.models.requests.user;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserGetReuqestModel {
    private String email;
    private String password;
}
