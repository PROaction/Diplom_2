package com.example.models.responses.user;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPatchResponseModel {
    private boolean success;
    private UserResponseModel user;
}
