package com.belchiorsapalo.codeFormater.exceptions;

import java.sql.Time;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(InvalidCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidCodeException(InvalidCodeException ex) {
        return new ApiError(ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleResourceAlreadyExistsException(ResourceAlreadyExistsException e){
        return new ApiError(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleResourceNotFoundException(ResourceNotFoundException e){
        return new ApiError(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleRunTimeException(RuntimeException e){
        return new ApiError(e.getMessage());
    }

    @ExceptionHandler(TimeExpiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleTimeExpiredExeption(TimeExpiredException e){
        return new ApiError(e.getMessage());
    }

    @SuppressWarnings("null")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException e) {
        return new ApiError(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(InvalidLanguageException.class)
    public ApiError handleInvalidLanguageException(InvalidLanguageException e){
        return new ApiError(e.getMessage());
    }
}
