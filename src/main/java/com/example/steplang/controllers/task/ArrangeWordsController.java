package com.example.steplang.controllers.task;

import com.example.steplang.commands.task.CreateWordRepetitionTaskCommand;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.services.task.LanguageTaskService;
import com.example.steplang.utils.JwtUtil;
import com.example.steplang.utils.enums.LanguageTaskType;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/task/arrangewords")
@RequiredArgsConstructor
public class ArrangeWordsController {

    private final LanguageTaskService languageTaskService;
    private final JwtUtil jwtUtil;
    @PostMapping("/create")
    public ResponseEntity<?> createArrangeWordsTask(@Valid @RequestBody CreateWordRepetitionTaskCommand command){
        LanguageTask languageTask = languageTaskService.createTask(jwtUtil.getUserAuthInfo().getId(),command.getLanguageId(), LanguageTaskType.ARRANGE_WORDS);
        return ResponseEntity.ok(languageTask);
    }
}
