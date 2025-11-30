package com.example.steplang.commands.language;

import com.example.steplang.utils.enums.language.WordType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class CreateNewWordCommand {
    private Long languageId;

    @NotBlank(message = "'baseForm' is required")
    private final String baseForm;

    @NotNull(message = "'wordType' is required")
    private final WordType wordType;

}
