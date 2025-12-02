package com.example.steplang.commands.language;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddCourseActionCommand {
    private Long courseId;
    private Integer moduleId;
    private Integer subModuleId;

}
