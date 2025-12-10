package com.example.steplang.services.task;

import com.example.steplang.entities.language.Course;
import com.example.steplang.entities.language.Word;
import com.example.steplang.errors.LanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.model.course.CourseActionChooseWordWithImage;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.model.task.arrangewords.ArrangeWordsData;
import com.example.steplang.model.task.course.ChooseWordWithImageData;
import com.example.steplang.repositories.language.WordRepository;
import com.example.steplang.repositories.task.LanguageTaskRepository;
import com.example.steplang.utils.enums.LanguageTaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseTaskService{
    private final LanguageTaskRepository languageTaskRepo;
    private final WordRepository wordRepo;

    public LanguageTask createChooseWordWithImageTask(Course course, CourseActionChooseWordWithImage actionData) {
        String taskId = UUID.randomUUID().toString();

        ChooseWordWithImageData chooseWordWithImageData = new ChooseWordWithImageData();

        Word chosenWord = wordRepo.findWordByLanguageIdAndWordId(course.getLearningLanguage().getId(),actionData.getChosenWordId()).orElse(null);
        if(chosenWord == null)
            throw new ApiException(LanguageError.WORD_ID_NOT_FOUND,String.format("Couldn't find word with id ='%d' for course with language id ='%d'",actionData.getChosenWordId(),course.getLearningLanguage().getId()));
        chooseWordWithImageData.setActionData(actionData);

        LanguageTask languageTask = new LanguageTask(taskId,-1L,course.getLearningLanguage().getId(),LanguageTaskType.CHOOSE_WORD_WITH_IMAGE,chooseWordWithImageData);
        languageTaskRepo.save(languageTask);
        return languageTask;
    }
}
