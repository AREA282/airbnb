package com.application.airbnb.application.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class CustomResponseDTO {

    private String code;
    private String message;
    private int state;
    private Object responseObject;

    public CustomResponseDTO() {
    }

    public CustomResponseDTO(String message, HttpStatus state) {
        this.message = message;
        this.state = state.value();
        this.code = state.name();
    }

    public int getStatusCode() {
        return state;
    }
}
