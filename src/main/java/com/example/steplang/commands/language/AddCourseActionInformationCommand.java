package com.example.steplang.commands.language;

import com.example.steplang.utils.enums.CourseActionType;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddCourseActionInformationCommand extends AddCourseActionCommand{
    @NotBlank(message = "'body' is required")
    private String body;

}
