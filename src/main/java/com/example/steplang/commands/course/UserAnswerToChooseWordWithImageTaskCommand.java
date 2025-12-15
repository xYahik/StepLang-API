package com.example.steplang.commands.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAnswerToChooseWordWithImageTaskCommand {
    @NotBlank(message = "'taskId' is required")
    private String taskId;

    @NotNull(message = "'answerIndex' is required")
    private Integer answerIndex;
}
