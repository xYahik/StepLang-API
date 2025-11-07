package com.example.steplang.commands.task;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetWordRepetitionTaskCommand {

    @NotBlank(message = "'taskId' is required")
    private String taskId;
}
