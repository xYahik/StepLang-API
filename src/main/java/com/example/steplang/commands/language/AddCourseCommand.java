package com.example.steplang.commands.language;

import com.example.steplang.entities.language.Language;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddCourseCommand {

    @NotBlank(message = "'courseName' is required")
    private String courseName;

    @NotNull(message = "'learningLanguageId' is required")
    private Long learningLanguageId;
    @NotNull(message = "'nativeLanguageId' is required")
    private Long nativeLanguageId;
}
