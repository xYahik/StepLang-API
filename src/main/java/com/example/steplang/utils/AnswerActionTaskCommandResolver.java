package com.example.steplang.utils;

import com.example.steplang.commands.course.AnswerCourseActionTaskCommand;
import com.example.steplang.commands.course.AnswerCourseChooseWordWithImageTaskCommand;
import com.example.steplang.errors.LanguageError;
import com.example.steplang.errors.TaskError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.repositories.task.LanguageTaskRepository;
import com.example.steplang.utils.enums.LanguageTaskType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AnswerActionTaskCommandResolver {
    private final LanguageTaskRepository languageTaskRepo;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public AnswerCourseActionTaskCommand resolve(JsonNode rawJson){

        if(!rawJson.has("taskId"))
           throw new RuntimeException("'taskId' is required");

        String taskId = rawJson.get("taskId").asText();

        LanguageTask task = languageTaskRepo.findById(taskId).orElse(null);
        if(task == null)
            throw new ApiException(TaskError.TASK_NOT_EXIST, String.format("Couldn't find task with taskId ='%s'", taskId));

        AnswerCourseActionTaskCommand command = switch(task.getLanguageTaskType()){
            case CHOOSE_WORD_WITH_IMAGE -> objectMapper.convertValue(rawJson, AnswerCourseChooseWordWithImageTaskCommand.class);
            case null, default -> objectMapper.convertValue(rawJson, AnswerCourseActionTaskCommand.class);
        };

        command.setTaskType(task.getLanguageTaskType());

        Set<ConstraintViolation<AnswerCourseActionTaskCommand>> violations = validator.validate(command);

        if(!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }
        return command;
    }
}
