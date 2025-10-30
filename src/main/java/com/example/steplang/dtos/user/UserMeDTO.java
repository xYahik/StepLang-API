package com.example.steplang.dtos.user;

import lombok.Data;

@Data
public class UserMeDTO {
    private Long id;
    private String username;
    private String email;
    private int level;
    private int exp;
}