package com.example.steplang.commands.language;

import com.example.steplang.utils.enums.CourseActionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "actionType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddCourseActionInformationCommand.class, name = "INFORMATION"),
        @JsonSubTypes.Type(value = AddCourseActionChooseWordWithImageCommand.class, name = "CHOOSE_WORD_WITH_IMAGE")
})
public abstract class AddCourseActionCommand {

    @NotNull(message = "'actionType' is required")
    @JsonProperty("actionType")
    private CourseActionType actionType;

    private Long courseId;
    private Integer moduleId;
    private Integer subModuleId;
}
