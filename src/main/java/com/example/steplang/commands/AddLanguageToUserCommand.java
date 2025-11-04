package com.example.steplang.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddLanguageToUserCommand {

    @NotNull(message = "'languageId' is required")
    private Long languageId;
}
