package com.example.steplang.commands.language;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddCourseModuleCommand {

    private Long courseId;

    @NotBlank(message = "'moduleName' is required")
    private String moduleName;
    @NotNull(message = "'moduleId' is required")
    private Integer moduleId;
}
