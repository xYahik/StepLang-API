package com.example.steplang.commands;

import com.example.steplang.utils.enums.UnderstandingLevel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddUserLanguageWordCommand {

    @NotNull(message = "'wordId' is required")
    private Long wordId;

    private UnderstandingLevel understandingLevel;
    private Long understandingProgress;
}
