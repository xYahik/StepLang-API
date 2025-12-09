package com.example.steplang.services.language;

import com.example.steplang.commands.language.*;
import com.example.steplang.entities.language.Course;
import com.example.steplang.entities.language.CourseModule;
import com.example.steplang.entities.language.CourseSubModule;
import com.example.steplang.entities.language.Language;
import com.example.steplang.errors.LanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.model.course.CourseActionBase;
import com.example.steplang.model.course.CourseActionChooseWordWithImage;
import com.example.steplang.model.course.CourseActionInformation;
import com.example.steplang.repositories.language.CourseModuleRepository;
import com.example.steplang.repositories.language.CourseRepository;
import com.example.steplang.repositories.language.CourseSubModuleRepository;
import com.example.steplang.repositories.language.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

        CourseModule savedModule = course.getModules()
                .stream()
                .filter(m -> m.getModuleId().equals(courseModule.getModuleId()))
                .findFirst()
                .orElseThrow();
        return savedModule;
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

        CourseSubModule savedSubModule = courseModule.getSubModules()
                .stream()
                .filter(m -> m.getSubModuleId().equals(courseSubModule.getSubModuleId()))
                .findFirst()
                .orElseThrow();
        return savedSubModule;
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
        CourseSubModule courseSubModule = courseSubModuleRepo.findBySubModuleIdAndModule(command.getSubModuleId(),courseModule).orElse(null);
        if(courseSubModule == null){
            throw new ApiException(LanguageError.COURSE_SUBMODULE_ID_NOT_FOUND,String.format("Couldn't find SubModule with subModuleId = '%d' for moduleId ='%d' and courseId = '%d'", command.getSubModuleId(),command.getModuleId(),command.getCourseId()));
        }

        CourseActionInformation courseAction = new CourseActionInformation();
        courseSubModule.addAction(courseAction);
        courseSubModuleRepo.save(courseSubModule);
        return courseAction;

    }

    public Course getCourse(Long courseId) {
        Course course = courseRepo.findById(courseId).orElse(null);
        if(course == null){
            throw new ApiException(LanguageError.COURSE_ID_NOT_FOUND,String.format("Couldn't find Course with courseId = '%d'",courseId));
        }
        return course;
    }

    public CourseModule getCourseModule(Long courseId, Integer moduleId) {
        Course course = courseRepo.findById(courseId).orElse(null);
        if(course == null){
            throw new ApiException(LanguageError.COURSE_ID_NOT_FOUND,String.format("Couldn't find Course with courseId = '%d'", courseId));
        }
        CourseModule courseModule = courseModuleRepo.findByModuleIdAndCourseId(moduleId,courseId).orElse(null);
        if(courseModule == null){
            throw new ApiException(LanguageError.COURSE_MODULE_ID_NOT_FOUND,String.format("Couldn't find Module with moduleId = '%d' and courseId = '%d'", moduleId,courseId));
        }

        return courseModule;
    }

    public CourseSubModule getCourseSubModule(Long courseId, Integer moduleId, Integer subModuleId) {

        Course course = courseRepo.findById(courseId).orElse(null);
        if(course == null){
            throw new ApiException(LanguageError.COURSE_ID_NOT_FOUND,String.format("Couldn't find Course with courseId = '%d'", courseId));
        }
        CourseModule courseModule = courseModuleRepo.findByModuleIdAndCourseId(moduleId,courseId).orElse(null);
        if(courseModule == null){
            throw new ApiException(LanguageError.COURSE_MODULE_ID_NOT_FOUND,String.format("Couldn't find Module with moduleId = '%d' and courseId = '%d'", moduleId,courseId));
        }

        CourseSubModule courseSubModule = courseSubModuleRepo.findBySubModuleIdAndModule(subModuleId,courseModule).orElse(null);
        if(courseSubModule == null){
            throw new ApiException(LanguageError.COURSE_SUBMODULE_ID_NOT_FOUND,String.format("Couldn't find SubModule with subModuleId = '%d' for moduleId ='%d' and courseId = '%d'",subModuleId,moduleId,courseId));
        }

        return courseSubModule;
    }

    public CourseActionBase getCourseAction(Long courseId, Integer moduleId, Integer subModuleId, String actionId) {

        Course course = courseRepo.findById(courseId).orElse(null);
        if(course == null){
            throw new ApiException(LanguageError.COURSE_ID_NOT_FOUND,String.format("Couldn't find Course with courseId = '%d'", courseId));
        }
        CourseModule courseModule = courseModuleRepo.findByModuleIdAndCourseId(moduleId,courseId).orElse(null);
        if(courseModule == null){
            throw new ApiException(LanguageError.COURSE_MODULE_ID_NOT_FOUND,String.format("Couldn't find Module with moduleId = '%d' and courseId = '%d'", moduleId,courseId));
        }

        CourseSubModule courseSubModule = courseSubModuleRepo.findBySubModuleIdAndModule(subModuleId,courseModule).orElse(null);
        if(courseSubModule == null){
            throw new ApiException(LanguageError.COURSE_SUBMODULE_ID_NOT_FOUND,String.format("Couldn't find SubModule with subModuleId = '%d' for moduleId ='%d' and courseId = '%d'",subModuleId,moduleId,courseId));
        }

        CourseActionBase courseAction = courseSubModule.getActions().stream().filter(action -> Objects.equals(action.getId(), actionId)).findFirst().orElse(null);
        if(courseAction == null){
            throw new ApiException(LanguageError.COURSE_ACTION_ID_NOT_FOUND,String.format("Couldn't find Course Action with actionId ='%s' for moduleId ='%d' and courseId = '%d' and subModule with subModuleId = '%d' ",actionId,moduleId,courseId, subModuleId));
        }
        return courseAction;
    }

    public CourseActionBase addNewActionInformation(AddCourseActionInformationCommand command) {

        Course course = courseRepo.findById(command.getCourseId()).orElse(null);
        if(course == null){
            throw new ApiException(LanguageError.COURSE_ID_NOT_FOUND,String.format("Couldn't find Course with courseId = '%d'", command.getCourseId()));
        }
        CourseModule courseModule = courseModuleRepo.findByModuleIdAndCourseId(command.getModuleId(),command.getCourseId()).orElse(null);
        if(courseModule == null){
            throw new ApiException(LanguageError.COURSE_MODULE_ID_NOT_FOUND,String.format("Couldn't find Module with moduleId = '%d' and courseId = '%d'", command.getModuleId(),command.getCourseId()));
        }
        CourseSubModule courseSubModule = courseSubModuleRepo.findBySubModuleIdAndModule(command.getSubModuleId(),courseModule).orElse(null);
        if(courseSubModule == null){
            throw new ApiException(LanguageError.COURSE_SUBMODULE_ID_NOT_FOUND,String.format("Couldn't find SubModule with subModuleId = '%d' for moduleId ='%d' and courseId = '%d'", command.getSubModuleId(),command.getModuleId(),command.getCourseId()));
        }

        CourseActionInformation courseAction = new CourseActionInformation(command.getBody());
        courseSubModule.addAction(courseAction);
        courseSubModuleRepo.save(courseSubModule);
        return courseAction;
    }

    public CourseActionBase addNewActionChooseWordWithImage(AddCourseActionChooseWordWithImageCommand command) {
        Course course = courseRepo.findById(command.getCourseId()).orElse(null);
        if(course == null){
            throw new ApiException(LanguageError.COURSE_ID_NOT_FOUND,String.format("Couldn't find Course with courseId = '%d'", command.getCourseId()));
        }
        CourseModule courseModule = courseModuleRepo.findByModuleIdAndCourseId(command.getModuleId(),command.getCourseId()).orElse(null);
        if(courseModule == null){
            throw new ApiException(LanguageError.COURSE_MODULE_ID_NOT_FOUND,String.format("Couldn't find Module with moduleId = '%d' and courseId = '%d'", command.getModuleId(),command.getCourseId()));
        }
        CourseSubModule courseSubModule = courseSubModuleRepo.findBySubModuleIdAndModule(command.getSubModuleId(),courseModule).orElse(null);
        if(courseSubModule == null){
            throw new ApiException(LanguageError.COURSE_SUBMODULE_ID_NOT_FOUND,String.format("Couldn't find SubModule with subModuleId = '%d' for moduleId ='%d' and courseId = '%d'", command.getSubModuleId(),command.getModuleId(),command.getCourseId()));
        }

        CourseActionChooseWordWithImage courseAction = new CourseActionChooseWordWithImage(command.getChosenWordId(),command.getExtraWordsId());
        courseSubModule.addAction(courseAction);
        courseSubModuleRepo.save(courseSubModule);
        return courseAction;
    }
}
