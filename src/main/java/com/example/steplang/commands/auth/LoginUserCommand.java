package com.example.steplang.commands.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserCommand {
    @NotBlank(message = "'email' is required")
    private String email;
    @NotBlank(message = "'password' is required")
    private String password;
}
