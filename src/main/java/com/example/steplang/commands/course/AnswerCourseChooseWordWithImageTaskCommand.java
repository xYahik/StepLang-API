package com.example.steplang.commands.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerCourseChooseWordWithImageTaskCommand extends AnswerCourseActionTaskCommand{

    @NotNull(message = "'answerIndex' is required")
    private Integer answerIndex;
}
