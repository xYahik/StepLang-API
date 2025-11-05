package com.example.steplang.commands.task;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CreateWordRepetitionTaskCommand {
    @NotNull(message = "'languageId' is required")
    private Long languageId;
}
