package com.example.steplang.controllers.language;

import com.example.steplang.commands.language.*;
import com.example.steplang.entities.language.Course;
import com.example.steplang.entities.language.CourseModule;
import com.example.steplang.entities.language.CourseSubModule;
import com.example.steplang.errors.LanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.mappers.CourseMapper;
import com.example.steplang.mappers.task.TaskMapper;
import com.example.steplang.model.course.CourseActionBase;
import com.example.steplang.model.course.CourseActionChooseWordWithImage;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.repositories.language.WordRepository;
import com.example.steplang.services.language.CourseService;
import com.example.steplang.services.task.CourseTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final TaskMapper taskMapper;
    private final CourseTaskService courseTaskService;
    private final WordRepository wordRepository;
    @PostMapping("/add")
    public ResponseEntity<?> addNewCourse(@Valid @RequestBody AddCourseCommand command){
        Course course = courseService.addNewCourse(command);
        return ResponseEntity.status(201).body(courseMapper.courseToDTO(course));
    }

    @PostMapping("/{courseId}/add")
    public ResponseEntity<?> addNewModuleToCourse(@PathVariable Long courseId, @Valid @RequestBody AddCourseModuleCommand command){
        command.setCourseId(courseId);
        CourseModule courseModule = courseService.addNewModuleToCourse(command);
        return ResponseEntity.status(201).body(courseMapper.courseModuleToDTO(courseModule));
    }

    @PostMapping("/{courseId}/{moduleId}/add")
    public ResponseEntity<?> addNewSubModuleToModule(@PathVariable Long courseId, @PathVariable Integer moduleId, @Valid @RequestBody AddCourseSubModuleCommand command){
        command.setCourseId(courseId);
        command.setModuleId(moduleId);
        CourseSubModule courseSubModule = courseService.addNewSubModuleToCourse(command);
        return ResponseEntity.status(201).body(courseMapper.courseSubModelToDTO(courseSubModule));
    }

    @GetMapping("{courseId}")
    public ResponseEntity<?> getCourse(@PathVariable Long courseId){
        Course course = courseService.getCourse(courseId);
        return ResponseEntity.ok(courseMapper.courseToDTO(course));
    }
    @GetMapping("/all/{learningLanguageId}/{nativeLanguageId}")
    public ResponseEntity<?> getCourses(@PathVariable Long learningLanguageId, @PathVariable Long nativeLanguageId){
        List<Course> courses = courseService.getCourses(learningLanguageId,nativeLanguageId);
        return ResponseEntity.ok(courseMapper.courseToDTOList(courses));
    }
    @GetMapping("{courseId}/{moduleId}")
    public ResponseEntity<?> getCourseModule(@PathVariable Long courseId, @PathVariable Integer moduleId){
        CourseModule courseModule = courseService.getCourseModule(courseId,moduleId);
        return ResponseEntity.ok(courseMapper.courseModuleToDTO(courseModule));
    }

    @GetMapping("{courseId}/{moduleId}/{subModuleId}")
    public ResponseEntity<?> getCourseSubModule(@PathVariable Long courseId, @PathVariable Integer moduleId, @PathVariable Integer subModuleId){
        CourseSubModule courseSubModule = courseService.getCourseSubModule(courseId,moduleId,subModuleId);
        return ResponseEntity.ok(courseMapper.courseSubModelToDTO(courseSubModule));
    }

    @PostMapping("{courseId}/{moduleId}/{subModuleId}/action/add")
    public ResponseEntity<?> addNewActionToSubModule(@PathVariable Long courseId, @PathVariable Integer moduleId, @PathVariable Integer subModuleId, @Valid @RequestBody AddCourseActionCommand command){
        command.setCourseId(courseId);
        command.setModuleId(moduleId);
        command.setSubModuleId(subModuleId);

        CourseActionBase action;
        switch (command.getActionType()) {
            case INFORMATION -> action = courseService.addNewActionInformation((AddCourseActionInformationCommand)command);
            case CHOOSE_WORD_WITH_IMAGE -> action = courseService.addNewActionChooseWordWithImage((AddCourseActionChooseWordWithImageCommand)command);
            default -> throw new ApiException(LanguageError.COURSE_ACTION_TYPE_NOT_FOUND,"Unknown action type: " + command.getActionType());
        }
        return ResponseEntity.status(201).body(action);
    }

    @GetMapping("{courseId}/{moduleId}/{subModuleId}/action/{actionId}")
    public ResponseEntity<?> getCourseActionFromActionID(@PathVariable Long courseId, @PathVariable Integer moduleId, @PathVariable Integer subModuleId, @PathVariable String actionId){
        CourseActionBase courseAction = courseService.getCourseActionByActionID(courseId,moduleId,subModuleId,actionId);
        return ResponseEntity.ok(courseAction);
    }
    @GetMapping("{courseId}/{moduleId}/{subModuleId}/{actionIndex}")
    public ResponseEntity<?> getCourseActionFromActionIndex(@PathVariable Long courseId, @PathVariable Integer moduleId, @PathVariable Integer subModuleId, @PathVariable Integer actionIndex){
        CourseActionBase courseAction = courseService.getCourseActionByIndex(courseId,moduleId,subModuleId,actionIndex);
        return ResponseEntity.ok(courseAction);
    }

    @PostMapping("{courseId}/{moduleId}/{subModuleId}/action/task/create")
    public ResponseEntity<?> createNewActionTask(@PathVariable Long courseId, @PathVariable Integer moduleId, @PathVariable Integer subModuleId, @Valid @RequestBody AddCourseActionCommand command){
        command.setCourseId(courseId);
        command.setModuleId(moduleId);
        command.setSubModuleId(subModuleId);

        LanguageTask task;
        switch (command.getActionType()) {
            case CHOOSE_WORD_WITH_IMAGE -> {
                CourseActionChooseWordWithImage courseActionChooseWordWIthImage = new CourseActionChooseWordWithImage();
                courseActionChooseWordWIthImage.setChosenWordId(((AddCourseActionChooseWordWithImageCommand)command).getChosenWordId());
                courseActionChooseWordWIthImage.setExtraWordsId(((AddCourseActionChooseWordWithImageCommand)command).getExtraWordsId());
                task = courseTaskService.createChooseWordWithImageTask(courseService.getCourse(courseId),courseActionChooseWordWIthImage);
            }
            default -> throw new ApiException(LanguageError.COURSE_ACTION_TYPE_NOT_FOUND,"Unknown action type: " + command.getActionType());
        }
        return ResponseEntity.status(201).body(taskMapper.toChooseWordWithImageTaskInfoDTO(task,wordRepository));
    }
}
