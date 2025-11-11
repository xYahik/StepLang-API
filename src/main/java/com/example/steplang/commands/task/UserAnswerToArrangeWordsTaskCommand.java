package com.example.steplang.commands.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserAnswerToArrangeWordsTaskCommand {
    @NotBlank(message = "'taskId' is required")
    private String taskId;

    @NotNull(message = "'taskItemId' is required")
    private Long taskItemId;

    @NotNull(message = "'answerIdsOrder' is required")
    private List<Long> answerIdsOrder;
}
