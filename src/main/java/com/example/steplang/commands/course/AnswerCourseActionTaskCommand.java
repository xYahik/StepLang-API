package com.example.steplang.commands.course;

import com.example.steplang.utils.enums.LanguageTaskType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class AnswerCourseActionTaskCommand {

    @NotBlank(message = "'taskId' is required")
    private String taskId;

    private LanguageTaskType taskType;
}
