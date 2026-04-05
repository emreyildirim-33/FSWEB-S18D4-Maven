package com.workintech.s18d1.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BurgerErrorResponse {
    private int status;
    private String message;
    private long timestamp;
}
