package com.example.steplang.controllers.task;

import com.example.steplang.commands.task.CreateWordRepetitionTaskCommand;
import com.example.steplang.commands.task.UserAnswerToWordRepetitionTaskCommand;
import com.example.steplang.entities.language.UserWordProgress;
import com.example.steplang.entities.task.LanguageTask;
import com.example.steplang.entities.task.wordrepetition.WordRepetitionAnswer;
import com.example.steplang.entities.task.wordrepetition.WordRepetitionData;
import com.example.steplang.entities.task.wordrepetition.WordRepetitionItem;
import com.example.steplang.mappers.task.TaskMapper;
import com.example.steplang.services.task.LanguageTaskService;
import com.example.steplang.utils.JwtUtil;
import com.example.steplang.utils.enums.LanguageTaskType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/task/wordrepetition")
@RequiredArgsConstructor
public class WordRepetitionController {
    private final LanguageTaskService languageTaskService;
    private final TaskMapper taskMapper;
    private final JwtUtil jwtUtil;
    @PostMapping("/create")
    public ResponseEntity<?> createWordRepetitionTask(@Valid @RequestBody CreateWordRepetitionTaskCommand command){
        LanguageTask languageTask = languageTaskService.createTask(jwtUtil.getUserAuthInfo().getId(),command.getLanguageId(),LanguageTaskType.WORD_REPETITION);
        return ResponseEntity.ok(taskMapper.toWordRepetitionTaskInfoDTO(languageTask));
    }

    @PostMapping("/answer")
    public ResponseEntity<?> userAnswerOnWordRepetitionTask(@Valid @RequestBody UserAnswerToWordRepetitionTaskCommand command){

        return ResponseEntity.ok(languageTaskService.userAnswerOnWordRepetitionTask(command));
    }
}
