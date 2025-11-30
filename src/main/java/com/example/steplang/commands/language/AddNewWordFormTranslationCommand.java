package com.example.steplang.commands.language;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddNewWordFormTranslationCommand {
    private Long languageId;

    @NotNull(message = "'wordId' is required")
    private final Long wordId;

    @NotNull(message = "'formId' is required")
    private final Long formId;

    @NotNull(message = "'targetLanguageId' is required")
    private final Long targetLanguageId;
    @NotBlank(message = "'translation' is required")
    private final String translation;
}
