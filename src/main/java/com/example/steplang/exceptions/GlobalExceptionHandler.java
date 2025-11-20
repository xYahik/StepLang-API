package com.example.steplang.exceptions;

import com.example.steplang.dtos.ResponseErrorDTO;
import com.example.steplang.errors.LanguageError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e){
        log.error("RuntimeException caught", e);
        return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleRuntimeException(HttpRequestMethodNotSupportedException e){
        log.error("HttpRequestMethodNotSupportedException caught", e);
        return ResponseEntity.badRequest().body(Map.of("HttpRequestMethodNotSupportedException",e.getMessage()));
    }
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleRuntimeException(ApiException e){
        ResponseErrorDTO errorDTO = new ResponseErrorDTO();

        errorDTO.setCode(e.getErrorCode());
        errorDTO.setStatus(400L);

        if(!e.getMessageText().isEmpty())
            errorDTO.setMessage(e.getMessageText());
        //if(e.getDetails() != null)
            //errorDTO.setDetails(e.getDetails());

        log.error("ApiException caught", e);
        return ResponseEntity.badRequest().body(Map.of("error",errorDTO));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        ResponseErrorDTO errorDTO = new ResponseErrorDTO();
        Map<String, String> errors = new HashMap<>();

        //ex.getBindingResult().getFieldErrors().forEach(error ->
        //        errors.put(error.getField(), error.getDefaultMessage()));

        errorDTO.setCode(LanguageError.MISSING_KEY.toString());
        errorDTO.setStatus(400L);

        //errorDTO.setMessage("Validation failed");
        errorDTO.setMessage(ex.getBindingResult().getFieldError().getDefaultMessage());
        //errorDTO.setDetails(errors);

        log.error("MethodArgumentNotValidException caught", ex);
        return ResponseEntity.badRequest().body(Map.of("error",errorDTO));
    }
}
