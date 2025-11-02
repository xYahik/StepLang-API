package com.example.steplang.commands.language;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddNewLanguageCommand {
    @NotBlank(message = "'name' is required")
    private String name;
}
