package com.example.steplang.mappers.task;

import com.example.steplang.dtos.task.TaskDataBaseDTO;
import com.example.steplang.dtos.task.WordRepetitionDataDTO;
import com.example.steplang.dtos.task.WordRepetitionItemDTO;
import com.example.steplang.dtos.task.WordRepetitionTaskInfoDTO;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.model.task.TaskDataBase;
import com.example.steplang.model.task.wordrepetition.WordRepetitionAnswer;
import com.example.steplang.model.task.wordrepetition.WordRepetitionData;
import com.example.steplang.model.task.wordrepetition.WordRepetitionItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.SubclassMapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @SubclassMapping(source = WordRepetitionData.class, target = WordRepetitionDataDTO.class)
    //Add more subclassmapping as we need more different data subclasses
    TaskDataBaseDTO mapTaskData(TaskDataBase taskData);
    @Mapping(target = "taskData", source="taskData")
    WordRepetitionTaskInfoDTO toWordRepetitionTaskInfoDTO(LanguageTask languageTask);

    @Mapping(target ="itemList", source = "itemList")
    WordRepetitionDataDTO toWordRepetitionDataDTO(WordRepetitionData wordRepetitionData);

    @Mapping(target = "possibleAnswers", source="possibleAnswers",qualifiedByName = "answersToStrings")
    WordRepetitionItemDTO toWordRepetitionItemDTO(WordRepetitionItem item);

    @Named("answersToStrings")
    default List<String> answersToString(List<WordRepetitionAnswer> answers){
        if(answers == null)
            return List.of();

        return answers.stream()
                .map(WordRepetitionAnswer::getAnswer)
                .collect(Collectors.toList());
    }
}
