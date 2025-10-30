package com.example.steplang.dtos.user;

import lombok.Data;

@Data
public class UserProfileDTO {
    private Long id;
    private String username;
    private int level;
    private int exp;
}
