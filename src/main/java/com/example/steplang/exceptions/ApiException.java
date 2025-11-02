package com.example.steplang.exceptions;

import com.example.steplang.errors.LanguageError;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private String errorCode;
    private String messageText;
    private Object details;
    public ApiException(String e) {
        super(e);
    }
    public ApiException(LanguageError langError, String message){
        this.errorCode = langError.toString();
        this.messageText = message;
    }
    public ApiException(LanguageError langError, String message, Object details){
        this.errorCode = langError.toString();
        this.messageText = message;
        this.details = details;
    }
    public ApiException(LanguageError langError){
        this.errorCode = langError.toString();
    }

}
