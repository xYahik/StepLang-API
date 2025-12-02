package com.example.steplang.commands.language;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddCourseSubModuleCommand {
    private Long courseId;
    private Integer moduleId;

    @NotNull(message = "'subModuleId' is required")
    private Integer subModuleId;

    @NotBlank(message = "'subModuleName' is required")
    private String subModuleName;
}
