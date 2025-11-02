package com.example.steplang.dtos;

import lombok.Data;

@Data

public class ResponseErrorDTO {
    private String code;
    private String message;
    private Long status;
    //private Object details;
}
