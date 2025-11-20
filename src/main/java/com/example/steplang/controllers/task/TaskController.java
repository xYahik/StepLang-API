package com.example.steplang.controllers.task;

import com.example.steplang.model.task.TaskStatusInfo;
import com.example.steplang.services.task.LanguageTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/task")
@RequiredArgsConstructor
public class TaskController {
    private final LanguageTaskService languageTaskService;
    @GetMapping("/{taskId}/status")
    public ResponseEntity<?> getTaskStatus(@PathVariable String taskId){
        TaskStatusInfo taskInfo = languageTaskService.getTaskStatus(taskId);
        return ResponseEntity.ok(taskInfo);
    }
}
