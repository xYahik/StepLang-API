package com.example.steplang.services.task;

import com.example.steplang.commands.language.CreateCourseActionTaskCommand;
import com.example.steplang.entities.language.Course;
import com.example.steplang.entities.language.Word;
import com.example.steplang.errors.LanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.model.course.CourseActionBase;
import com.example.steplang.model.course.CourseActionChooseWordWithImage;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.model.task.arrangewords.ArrangeWordsData;
import com.example.steplang.model.task.course.ChooseWordWithImageData;
import com.example.steplang.model.task.course.ChooseWordWithImageItem;
import com.example.steplang.repositories.language.WordRepository;
import com.example.steplang.repositories.task.LanguageTaskRepository;
import com.example.steplang.services.language.CourseService;
import com.example.steplang.utils.enums.CourseActionType;
import com.example.steplang.utils.enums.LanguageTaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseTaskService{
    private final LanguageTaskRepository languageTaskRepo;
    private final WordRepository wordRepo;
    private final CourseService courseService;

    public LanguageTask createCourseActionTask(Long userId, CreateCourseActionTaskCommand command){

        CourseActionBase courseAction = courseService.getCourseActionByActionID(command.getCourseId(),command.getModuleId(),command.getSubModuleId(),command.getActionId());
        CourseActionType actionType = courseAction.getActionType();

        LanguageTask languageTask;
        switch (actionType) {
            case CHOOSE_WORD_WITH_IMAGE -> {
                languageTask = createChooseWordWithImageTask(userId,courseService.getCourse(command.getCourseId()),(CourseActionChooseWordWithImage)courseAction);
            }
            default -> throw new ApiException(LanguageError.COURSE_ACTION_TYPE_NOT_FOUND,"Unknown action type: " + actionType);
        }

        return languageTask;
    }
    private LanguageTask createChooseWordWithImageTask(Long userId, Course course, CourseActionChooseWordWithImage actionData) {
        String taskId = UUID.randomUUID().toString();

        ChooseWordWithImageData chooseWordWithImageData = new ChooseWordWithImageData();


        List<ChooseWordWithImageItem> wordList = new ArrayList<>();

        Word chosenWord = wordRepo.findWordByLanguageIdAndWordId(course.getLearningLanguage().getId(),actionData.getChosenWordId()).orElse(null);
        if(chosenWord == null)
            throw new ApiException(LanguageError.WORD_ID_NOT_FOUND,String.format("Couldn't find word with id ='%d' for course with language id ='%d'",actionData.getChosenWordId(),course.getLearningLanguage().getId()));

        ChooseWordWithImageItem chosenWordItem = new ChooseWordWithImageItem(chosenWord.getBaseForm(),chosenWord.getWordId(),-1L,chosenWord.getImageUrl());
        wordList.add(chosenWordItem);
        for(Long extraWordId : actionData.getExtraWordsId()){
            Word extraWord = wordRepo.findWordByLanguageIdAndWordId(course.getLearningLanguage().getId(),extraWordId).orElse(null);
            if(extraWord == null)
                throw new ApiException(LanguageError.WORD_ID_NOT_FOUND,String.format("Couldn't find word with id ='%d' for course with language id ='%d'",extraWordId,course.getLearningLanguage().getId()));
            wordList.add(new ChooseWordWithImageItem(extraWord.getBaseForm(),extraWord.getWordId(),-1L,extraWord.getImageUrl()));
        }
        chooseWordWithImageData.setWordsList(wordList);

        Collections.shuffle(chooseWordWithImageData.getWordsList());
        chooseWordWithImageData.setChosenWordIndex(chooseWordWithImageData.getWordsList().indexOf(chosenWordItem));
        chooseWordWithImageData.setWordImageUrl(chosenWord.getImageUrl());

        LanguageTask languageTask = new LanguageTask(taskId,userId,course.getLearningLanguage().getId(),LanguageTaskType.CHOOSE_WORD_WITH_IMAGE,chooseWordWithImageData);
        languageTaskRepo.save(languageTask);
        return languageTask;
    }
}
