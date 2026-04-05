package com.workintech.s18d1.exceptions;
import com.workintech.s18d1.exceptions.BurgerErrorResponse;
import com.workintech.s18d1.exceptions.BurgerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<BurgerErrorResponse> handleException(BurgerException exception) {
        log.error("BurgerException occurred: ", exception);
        BurgerErrorResponse errorResponse = new BurgerErrorResponse(
                exception.getStatus().value(),
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, exception.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<BurgerErrorResponse> handleException(Exception exception) {
        log.error("Unknown exception occurred: ", exception);
        BurgerErrorResponse errorResponse = new BurgerErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}