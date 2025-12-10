package com.example.steplang.controllers.task;

import com.example.steplang.commands.task.CreateWordRepetitionTaskCommand;
import com.example.steplang.commands.task.GetWordRepetitionTaskCommand;
import com.example.steplang.commands.task.UserAnswerToWordRepetitionTaskCommand;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.mappers.task.TaskMapper;
import com.example.steplang.model.task.WordRepetitionStatusInfo;
import com.example.steplang.repositories.language.WordRepository;
import com.example.steplang.services.task.LanguageTaskService;
import com.example.steplang.services.task.WordRepetitionTaskService;
import com.example.steplang.utils.JwtUtil;
import com.example.steplang.utils.enums.LanguageTaskType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/task/wordrepetition")
@RequiredArgsConstructor
public class WordRepetitionController {
    private final LanguageTaskService languageTaskService;
    private final TaskMapper taskMapper;
    private final JwtUtil jwtUtil;
    private final WordRepetitionTaskService wordRepetitionTaskService;
    private final WordRepository wordRepository;
    @PostMapping("/create")
    public ResponseEntity<?> createWordRepetitionTask(@Valid @RequestBody CreateWordRepetitionTaskCommand command){
        LanguageTask languageTask = languageTaskService.createTask(jwtUtil.getUserAuthInfo().getId(),command.getLanguageId(),command.getTargetLanguageId(),LanguageTaskType.WORD_REPETITION);

        return ResponseEntity.ok(taskMapper.toWordRepetitionTaskInfoDTO(languageTask,wordRepository));
    }

    @PostMapping("/answer")
    public ResponseEntity<?> userAnswerOnWordRepetitionTask(@Valid @RequestBody UserAnswerToWordRepetitionTaskCommand command){

        return ResponseEntity.ok(wordRepetitionTaskService.userAnswerOnWordRepetitionTask(command));
    }
}
