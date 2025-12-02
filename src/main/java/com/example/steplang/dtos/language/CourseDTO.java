package com.example.steplang.dtos.language;

import lombok.Data;

import java.util.List;

@Data
public class CourseDTO {
    private Long id;
    private Long learningLanguageId;
    private Long nativeLanguageId;
    private String name;
    List<CourseModuleDTO> modules;
}
