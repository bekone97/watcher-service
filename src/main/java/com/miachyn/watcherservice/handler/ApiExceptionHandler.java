package com.miachyn.watcherservice.handler;

import com.miachyn.watcherservice.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse resourceNotFoundExceptionHandler(HttpServletRequest request, ResourceNotFoundException exception){
        log.error("The {}. There is no entity in database : {} and url of request : {}",
                exception.getClass().getSimpleName(),exception.getMessage(),request.getRequestURL());
        return new ApiErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse methodArgumentNotValidExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException exception){
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(exception.getBindingResult().getAllErrors().stream()
                .map(FieldError.class::cast)
                .map(error -> new ValidationMessage(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList()));
        log.error("The {}. Validation messages : {} and url of request : {}",
                exception.getClass().getSimpleName(), errorResponse.getValidationMessages(),request.getRequestURL());
        return errorResponse;
    }
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse constraintViolationExceptionHandler(HttpServletRequest request, ConstraintViolationException exception) {

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(exception.getConstraintViolations().stream()
                .map(set -> new ValidationMessage(set.getPropertyPath().toString(), set.getMessage()))
                .collect(Collectors.toList()));
        log.error("The {}.Validation messages :{} and url of request :{}",
                exception.getClass().getSimpleName(), errorResponse.getValidationMessages(), request.getRequestURL());

        return errorResponse;
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleRuntimeException(HttpServletRequest request, RuntimeException exception){
        log.error("The {}. Exception message : {} and url of request : {}",
                exception.getClass().getSimpleName(),exception.getMessage(),request.getRequestURL());
        return new ApiErrorResponse("Something wrong. Try again");
    }

}
