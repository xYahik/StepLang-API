package com.example.steplang.commands.language;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddCategoryToWordCommand {

    @NotBlank(message = "'categoryId' is required")
    private Long categoryId;
    @NotBlank(message = "'languageId' is required")
    private Long languageId;
    @NotBlank(message = "'wordId' is required")
    private Long wordId;
}
