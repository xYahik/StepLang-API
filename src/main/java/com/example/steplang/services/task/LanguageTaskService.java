package com.example.steplang.services.task;

import com.example.steplang.commands.task.UserAnswerToWordRepetitionTaskCommand;
import com.example.steplang.dtos.task.WordRepetitionAnswerResponseDTO;
import com.example.steplang.entities.language.UserLanguage;
import com.example.steplang.entities.language.UserWordProgress;
import com.example.steplang.entities.task.LanguageTask;
import com.example.steplang.entities.task.TaskDataBase;
import com.example.steplang.entities.task.wordrepetition.WordRepetitionAnswer;
import com.example.steplang.entities.task.wordrepetition.WordRepetitionData;
import com.example.steplang.entities.task.wordrepetition.WordRepetitionItem;
import com.example.steplang.errors.TaskError;
import com.example.steplang.errors.UserLanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.repositories.language.UserLanguageRepository;
import com.example.steplang.repositories.language.UserWordProgressRepository;
import com.example.steplang.repositories.task.LanguageTaskRepository;
import com.example.steplang.services.UserService;
import com.example.steplang.utils.JwtUtil;
import com.example.steplang.utils.enums.LanguageTaskType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class LanguageTaskService {
    private final LanguageTaskRepository languageTaskRepo;
    private final WordRepetitionTaskService wordRepetitionTaskService;

    public LanguageTask createTask(Long userId,Long languageId,LanguageTaskType taskType){
        String taskId = UUID.randomUUID().toString();
        TaskDataBase taskData = null;
        switch(taskType){
            case LanguageTaskType.WORD_REPETITION:
                taskData = wordRepetitionTaskService.createWordRepetitionTask(userId,languageId);
        }

        if(taskData == null)
            throw new ApiException(TaskError.COULD_NOT_CREATE_TASK,"Couldn't create WordRepetitionTask");

        LanguageTask languageTask = new LanguageTask(taskId,userId,languageId,taskType,taskData);
        languageTaskRepo.save(languageTask);
        return languageTask;
    }

    public Optional<LanguageTask> getTask(String taskId){
        return languageTaskRepo.findById(taskId);
    }

    public void deleteTask(String taskId){
        languageTaskRepo.deleteById(taskId);
    }
}
