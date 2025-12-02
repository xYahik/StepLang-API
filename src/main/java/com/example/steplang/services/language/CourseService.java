package com.example.steplang.services.language;

import com.example.steplang.commands.language.AddCourseActionCommand;
import com.example.steplang.commands.language.AddCourseCommand;
import com.example.steplang.commands.language.AddCourseModuleCommand;
import com.example.steplang.commands.language.AddCourseSubModuleCommand;
import com.example.steplang.entities.language.Course;
import com.example.steplang.entities.language.CourseModule;
import com.example.steplang.entities.language.CourseSubModule;
import com.example.steplang.entities.language.Language;
import com.example.steplang.errors.LanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.model.course.CourseActionBase;
import com.example.steplang.model.course.CourseActionInformation;
import com.example.steplang.repositories.language.CourseModuleRepository;
import com.example.steplang.repositories.language.CourseRepository;
import com.example.steplang.repositories.language.CourseSubModuleRepository;
import com.example.steplang.repositories.language.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepo;
    private final LanguageRepository languageRepo;
    private final CourseModuleRepository courseModuleRepo;
    private final CourseSubModuleRepository courseSubModuleRepo;

    @Transactional
    public Course addNewCourse(AddCourseCommand command) {
        Language learningLanguage = languageRepo.findById(command.getLearningLanguageId()).orElse(null);
        if(learningLanguage == null){
            throw new ApiException(LanguageError.LANGUAGE_ID_NOT_FOUND,String.format("Couldn't find Language with learningLanguageId = '%d'", command.getLearningLanguageId()));
        }
        Language nativeLanguage = languageRepo.findById(command.getNativeLanguageId()).orElse(null);
        if(nativeLanguage == null){
            throw new ApiException(LanguageError.LANGUAGE_ID_NOT_FOUND,String.format("Couldn't find Language with nativeLanguageId = '%d'", command.getNativeLanguageId()));
        }

        if(courseRepo.existsByNameAndLanguages(command.getCourseName(),command.getLearningLanguageId(), command.getNativeLanguageId())){
            throw new ApiException(LanguageError.ALREADY_EXISTS,String.format("Course with name '%s' for learningLanguageId ='%d' and nativeLanguageId = '%d' already exists", command.getCourseName(), command.getLearningLanguageId(),command.getNativeLanguageId()));
        }


        Course course = new Course(command.getCourseName(),learningLanguage,nativeLanguage);
        courseRepo.save(course);
        return course;
    }

    @Transactional
    public CourseModule addNewModuleToCourse(AddCourseModuleCommand command) {
        Course course = courseRepo.findById(command.getCourseId()).orElse(null);
        if(course == null){
            throw new ApiException(LanguageError.COURSE_ID_NOT_FOUND,String.format("Couldn't find Course with courseId = '%d'", command.getCourseId()));
        }
        if(courseModuleRepo.existsByModuleIdAndCourseId(command.getModuleId(), command.getCourseId())){
            throw new ApiException(LanguageError.COURSE_MODULE_ID_ALREADY_EXISTS, String.format("Module with moduleId = '%d' in course with courseId ='%d' already exists", command.getModuleId(),command.getCourseId()));
        }

        CourseModule courseModule = new CourseModule(command.getModuleId(),command.getModuleName());
        course.addModule(courseModule);
        courseRepo.save(course);
        return courseModule;
    }
    @Transactional
    public CourseSubModule addNewSubModuleToCourse(AddCourseSubModuleCommand command) {
        Course course = courseRepo.findById(command.getCourseId()).orElse(null);
        if(course == null){
            throw new ApiException(LanguageError.COURSE_ID_NOT_FOUND,String.format("Couldn't find Course with courseId = '%d'", command.getCourseId()));
        }
        CourseModule courseModule = courseModuleRepo.findByModuleIdAndCourseId(command.getModuleId(),command.getCourseId()).orElse(null);
        if(courseModule == null){
            throw new ApiException(LanguageError.COURSE_MODULE_ID_NOT_FOUND,String.format("Couldn't find Module with moduleId = '%d' for courseId = '%d'", command.getModuleId(),command.getCourseId()));
        }

        if(courseSubModuleRepo.existsBySubModuleIdAndModule(command.getSubModuleId(), courseModule)){
            throw new ApiException(LanguageError.COURSE_SUBMODULE_ID_ALREADY_EXISTS, String.format("SubModule with subModuleId = '%d' for moduleId = '%d' and courseId ='%d' already exists", command.getSubModuleId(),command.getModuleId(),command.getCourseId()));
        }

        CourseSubModule courseSubModule = new CourseSubModule(command.getSubModuleId(),command.getSubModuleName());
        courseModule.addSubModule(courseSubModule);
        courseModuleRepo.save(courseModule);
        return courseSubModule;
    }

    public CourseActionBase addNewActionToSubModule(AddCourseActionCommand command) {
        Course course = courseRepo.findById(command.getCourseId()).orElse(null);
        if(course == null){
            throw new ApiException(LanguageError.COURSE_ID_NOT_FOUND,String.format("Couldn't find Course with courseId = '%d'", command.getCourseId()));
        }
        CourseModule courseModule = courseModuleRepo.findByModuleIdAndCourseId(command.getModuleId(),command.getCourseId()).orElse(null);
        if(courseModule == null){
            throw new ApiException(LanguageError.COURSE_MODULE_ID_NOT_FOUND,String.format("Couldn't find Module with moduleId = '%d' and courseId = '%d'", command.getModuleId(),command.getCourseId()));
        }
        CourseSubModule courseSubModule = courseSubModuleRepo.findBySubModuleIdAndModule(command.getModuleId(),courseModule).orElse(null);
        if(courseSubModule == null){
            throw new ApiException(LanguageError.COURSE_SUBMODULE_ID_NOT_FOUND,String.format("Couldn't find SubModule with subModuleId = '%d' for moduleId ='%d' and courseId = '%d'", command.getSubModuleId(),command.getModuleId(),command.getCourseId()));
        }

        CourseActionInformation courseAction = new CourseActionInformation();
        courseSubModule.addAction(courseAction);
        courseSubModuleRepo.save(courseSubModule);
        return courseAction;

    }
}
