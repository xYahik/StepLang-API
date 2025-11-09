package com.example.steplang.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserMeDTO {
    private Long id;
    private String username;
    private String email;
    private int level;
    private int exp;
}