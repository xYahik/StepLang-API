package com.example.steplang.controllers.task;

import com.example.steplang.commands.task.*;
import com.example.steplang.mappers.task.TaskMapper;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.model.task.WordRepetitionStatusInfo;
import com.example.steplang.model.task.arrangewords.ArrangeWordsStatusInfo;
import com.example.steplang.repositories.language.WordRepository;
import com.example.steplang.services.task.ArrangeWordsTaskService;
import com.example.steplang.services.task.LanguageTaskService;
import com.example.steplang.services.task.WordRepetitionTaskService;
import com.example.steplang.utils.JwtUtil;
import com.example.steplang.utils.enums.LanguageTaskType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/task/arrangewords")
@RequiredArgsConstructor
public class ArrangeWordsController {

    private final LanguageTaskService languageTaskService;
    private final JwtUtil jwtUtil;
    private final TaskMapper taskMapper;
    private final ArrangeWordsTaskService arrangeWordsTaskService;
    @PostMapping("/create")
    public ResponseEntity<?> createArrangeWordsTask(@Valid @RequestBody CreateArrangeWordsTaskCommand command) throws JsonProcessingException {
        LanguageTask languageTask = languageTaskService.createTask(jwtUtil.getUserAuthInfo().getId(),command.getLanguageId(),command.getTargetLanguageId(), LanguageTaskType.ARRANGE_WORDS);
        System.out.println(languageTask.toString());
        return ResponseEntity.ok(taskMapper.toArrangeWordsTaskInfoDTO(languageTask));
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answerArrangeWordsTask(@Valid @RequestBody UserAnswerToArrangeWordsTaskCommand command){
        return ResponseEntity.ok(arrangeWordsTaskService.userAnswerArrangeWordsTask(command));
    }
}
