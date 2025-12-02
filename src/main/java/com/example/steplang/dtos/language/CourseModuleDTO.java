package com.example.steplang.dtos.language;

import lombok.Data;

import java.util.List;

@Data
public class CourseModuleDTO {
    private Long id;
    private Long courseId;
    private Integer moduleId;
    private String moduleName;
    private List<CourseSubModuleDTO> subModules;
}
