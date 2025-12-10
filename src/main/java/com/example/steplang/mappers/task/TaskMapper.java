package com.example.steplang.mappers.task;

import com.example.steplang.dtos.task.*;
import com.example.steplang.entities.language.Word;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.model.task.TaskDataBase;
import com.example.steplang.model.task.arrangewords.ArrangeWordsData;
import com.example.steplang.model.task.course.ChooseWordWithImageData;
import com.example.steplang.model.task.wordrepetition.WordRepetitionAnswer;
import com.example.steplang.model.task.wordrepetition.WordRepetitionData;
import com.example.steplang.model.task.wordrepetition.WordRepetitionItem;
import com.example.steplang.repositories.UserRepository;
import com.example.steplang.repositories.language.WordRepository;
import org.mapstruct.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @SubclassMapping(source = WordRepetitionData.class, target = WordRepetitionDataDTO.class)
    @SubclassMapping(source = ArrangeWordsData.class, target = ArrangeWordsDataDTO.class)
    @SubclassMapping(source = ChooseWordWithImageData.class, target = ChooseWordWithImageDataDTO.class)
    //Add more subclassmapping as we need more different data subclasses
    TaskDataBaseDTO mapTaskData(TaskDataBase taskData, @Context WordRepository wordRepository);
    @Mapping(target = "taskData", source="taskData")
    WordRepetitionTaskInfoDTO toWordRepetitionTaskInfoDTO(LanguageTask languageTask, @Context WordRepository repo);

    @Mapping(target = "taskData", source="taskData")
    ArrangeWordsTaskInfoDTO toArrangeWordsTaskInfoDTO(LanguageTask languageTask, @Context WordRepository repo);

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


    // ---------- ChooseWordWithImageWordTask Mapping -----------

    @Mapping(target = "chosenWord", source = "actionData.chosenWordId",
            qualifiedByName = "wordIdToWordDTO")
    @Mapping(target = "extraWords", source = "actionData.extraWordsId",
            qualifiedByName = "wordIdsToWordDTOList")
    ChooseWordWithImageDataDTO toChooseWordWithImageDataDTO(
            ChooseWordWithImageData data,
            @Context WordRepository wordRepository);

    @Named("wordIdToWordDTO")
    default ChooseWordWithImageWordDTO mapWordId(Long id, @Context WordRepository wordRepository) {
        return wordRepository.findById(id)
                .map(word -> new ChooseWordWithImageWordDTO(word.getId(), word.getBaseForm()))
                .orElse(null);
    }

    @Named("wordIdsToWordDTOList")
    default List<ChooseWordWithImageWordDTO> mapWordIds(List<Long> ids, @Context WordRepository wordRepository) {
        if (ids == null)
            return List.of();

        return ids.stream()
                .map(id -> wordRepository.findById(id)
                        .map(w -> new ChooseWordWithImageWordDTO(w.getId(), w.getBaseForm()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    @Mapping(target = "taskData", source="taskData")
    ChooseWordWithImageTaskInfoDTO toChooseWordWithImageTaskInfoDTO(LanguageTask languageTask, @Context WordRepository repo);
}
