package com.example.models.responses;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ErrorResponseModel {
    private String success;
    private String message;
}
