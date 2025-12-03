package com.example.steplang.mappers;

import com.example.steplang.dtos.language.CourseDTO;
import com.example.steplang.dtos.language.CourseModuleDTO;
import com.example.steplang.dtos.language.CourseSubModuleDTO;
import com.example.steplang.entities.language.Course;
import com.example.steplang.entities.language.CourseModule;
import com.example.steplang.entities.language.CourseSubModule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "learningLanguage.id", target = "learningLanguageId")
    @Mapping(source = "nativeLanguage.id", target = "nativeLanguageId")
    CourseDTO courseToDTO(Course course);

    List<CourseDTO> courseToDTOList(List<Course> courses);

    @Mapping(source = "course.id", target = "courseId")
    CourseModuleDTO courseModuleToDTO(CourseModule module);

    List<CourseModuleDTO> courseModuleToDTOList(List<CourseModule> modules);


    @Mapping(source = "module.id", target = "courseModuleId")
    CourseSubModuleDTO courseSubModelToDTO(CourseSubModule subModule);

    List<CourseSubModuleDTO> courseSubModelToDTOList(List<CourseSubModule> subModules);

}
