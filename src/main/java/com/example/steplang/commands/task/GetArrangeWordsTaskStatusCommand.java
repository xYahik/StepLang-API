package com.example.steplang.commands.task;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetArrangeWordsTaskStatusCommand {
    @NotBlank(message = "'taskId' is required")
    private String taskId;
}
