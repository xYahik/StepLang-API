package com.example.steplang.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUserRegisterDTO {
    private String message;
    private Long userId;
}
