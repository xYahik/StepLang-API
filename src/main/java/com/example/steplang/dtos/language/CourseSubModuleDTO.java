package com.example.steplang.dtos.language;

import com.example.steplang.model.course.CourseActionBase;
import lombok.Data;

import java.util.List;

@Data
public class CourseSubModuleDTO {
    private Long id;
    private Integer subModuleId;
    private String subModuleName;
    private Integer courseModuleId;
    private List<CourseActionBase> actions;
}
