package com.example.steplang.commands.language;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseActionTaskCommand {
    @NotBlank(message = "'actionId' is required")
    private String actionId;


    private Long courseId;
    private Integer moduleId;
    private Integer subModuleId;
}
