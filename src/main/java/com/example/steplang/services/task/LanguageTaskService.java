package com.example.steplang.services.task;

import com.example.steplang.errors.LanguageError;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.model.task.TaskDataBase;
import com.example.steplang.errors.TaskError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.model.task.TaskStatusInfo;
import com.example.steplang.model.task.WordRepetitionStatusInfo;
import com.example.steplang.model.task.arrangewords.ArrangeWordsStatusInfo;
import com.example.steplang.repositories.task.LanguageTaskRepository;
import com.example.steplang.utils.enums.LanguageTaskType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class LanguageTaskService {
    private final LanguageTaskRepository languageTaskRepo;
    private final WordRepetitionTaskService wordRepetitionTaskService;
    private final ArrangeWordsTaskService arrangeWordsTaskService;

    public LanguageTask createTask(Long userId,Long languageId,LanguageTaskType taskType){
        String taskId = UUID.randomUUID().toString();
        TaskDataBase taskData = null;
        switch(taskType){
            case LanguageTaskType.WORD_REPETITION -> taskData = wordRepetitionTaskService.createWordRepetitionTask(userId,languageId);
            case LanguageTaskType.ARRANGE_WORDS -> taskData = arrangeWordsTaskService.createArrangeWordsTask(userId,languageId);
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

    public TaskStatusInfo getTaskStatus(String taskId){
        LanguageTask task = languageTaskRepo.findById(taskId).orElse(null);
        if(task == null)
            throw new ApiException(TaskError.TASK_NOT_EXIST,String.format("Couldn't find task with id = '%s'",taskId));

        TaskStatusInfo taskStatusInfo = null;
        switch(task.getLanguageTaskType()){
            case LanguageTaskType.WORD_REPETITION -> {
                taskStatusInfo = wordRepetitionTaskService.getWordRepetitionTaskStatus(taskId);
            }
            case LanguageTaskType.ARRANGE_WORDS -> {
                taskStatusInfo = arrangeWordsTaskService.getArrangeWordsTaskStatus(taskId);
            }
        }
        return taskStatusInfo;
    }
}
