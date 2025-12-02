package com.example.steplang.controllers.language;

import com.example.steplang.commands.language.AddCourseActionCommand;
import com.example.steplang.commands.language.AddCourseCommand;
import com.example.steplang.commands.language.AddCourseModuleCommand;
import com.example.steplang.commands.language.AddCourseSubModuleCommand;
import com.example.steplang.entities.language.Course;
import com.example.steplang.entities.language.CourseModule;
import com.example.steplang.entities.language.CourseSubModule;
import com.example.steplang.model.course.CourseActionBase;
import com.example.steplang.services.language.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/add")
    public ResponseEntity<?> addNewCourse(@Valid @RequestBody AddCourseCommand command){
        Course course = courseService.addNewCourse(command);
        //need dto to not loop langauges in response
        return ResponseEntity.ok(course);
    }

    @PostMapping("/{courseId}/add")
    public ResponseEntity<?> addNewModuleToCourse(@PathVariable Long courseId, @Valid @RequestBody AddCourseModuleCommand command){
        command.setCourseId(courseId);
        CourseModule courseModule = courseService.addNewModuleToCourse(command);
        return ResponseEntity.ok(courseModule);
    }

    @PostMapping("/{courseId}/{moduleId}/add")
    public ResponseEntity<?> addNewSubModuleToModule(@PathVariable Long courseId, @PathVariable Integer moduleId, @Valid @RequestBody AddCourseSubModuleCommand command){
        command.setCourseId(courseId);
        command.setModuleId(moduleId);
        CourseSubModule courseSubModule = courseService.addNewSubModuleToCourse(command);
        return ResponseEntity.ok(courseSubModule);
    }

    /*@GetMapping("{courseId}")
    @GetMapping("{courseId}/{moduleId}")
    @GetMapping("{courseId}/{moduleId}/{subModuleId}")*/

    @PostMapping("{courseId}/{moduleId}/{subModuleId}/action/add")
    public ResponseEntity<?> addNewActionToSubModule(@PathVariable Long courseId, @PathVariable Integer moduleId, @PathVariable Integer subModuleId, @Valid @RequestBody AddCourseActionCommand command){
        command.setCourseId(courseId);
        command.setModuleId(moduleId);
        command.setSubModuleId(subModuleId);
        CourseActionBase action = courseService.addNewActionToSubModule(command);
        return ResponseEntity.ok(action);
    }

    /*@GetMapping("{courseId}/{moduleId}/{subModuleId}/{actionId}")*/


}
