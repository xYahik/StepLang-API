package com.example.steplang.commands.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAnswerToWordRepetitionTaskCommand {

    @NotBlank(message = "'taskId' is required")
    private String taskId;

    @NotNull(message = "'taskItemId' is required")
    private Long taskItemId;

    @NotNull(message = "'answerId' is required")
    private Long answerId;
}
