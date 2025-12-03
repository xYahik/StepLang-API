package com.example.steplang.controllers.language;

import com.example.steplang.commands.language.AddCourseActionCommand;
import com.example.steplang.commands.language.AddCourseCommand;
import com.example.steplang.commands.language.AddCourseModuleCommand;
import com.example.steplang.commands.language.AddCourseSubModuleCommand;
import com.example.steplang.entities.language.Course;
import com.example.steplang.entities.language.CourseModule;
import com.example.steplang.entities.language.CourseSubModule;
import com.example.steplang.mappers.CourseMapper;
import com.example.steplang.model.course.CourseActionBase;
import com.example.steplang.services.language.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;
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
        CourseActionBase action = courseService.addNewActionToSubModule(command);
        return ResponseEntity.status(201).body(action);
    }

    @GetMapping("{courseId}/{moduleId}/{subModuleId}/{actionId}")
    public ResponseEntity<?> getCourseAction(@PathVariable Long courseId, @PathVariable Integer moduleId, @PathVariable Integer subModuleId, @PathVariable String actionId){
        CourseActionBase courseAction = courseService.getCourseAction(courseId,moduleId,subModuleId,actionId);
        return ResponseEntity.ok(courseAction);
    }

}
