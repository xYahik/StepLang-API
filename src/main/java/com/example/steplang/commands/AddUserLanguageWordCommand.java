package com.example.steplang.commands;

import com.example.steplang.utils.enums.UnderstandingLevel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddUserLanguageWordCommand {
    @NotNull(message = "'wordFormId' is required")
    private Long wordFormId;

    private UnderstandingLevel understandingLevel;
    private Long understandingProgress;
}
