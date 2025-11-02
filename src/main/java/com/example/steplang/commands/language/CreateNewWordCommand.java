package com.example.steplang.commands.language;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class CreateNewWordCommand {
    private Long languageId;

    @NotBlank(message = "'word' is required")
    private final String word;

    @NotBlank(message = "'translation' is required")
    private final String translation;

}
