package com.application.airbnb.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class ErrorResponse {

    private Map<String, String> messages;
    private int statusCode;
    private String error;
}
