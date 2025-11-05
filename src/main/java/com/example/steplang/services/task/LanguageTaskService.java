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

    private final UserWordProgressRepository userWordProgressRepo;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserLanguageRepository userLanguageRepo;
    public LanguageTask createTask(Long userId,Long languageId,LanguageTaskType taskType){
        String taskId = UUID.randomUUID().toString();
        TaskDataBase taskData = null;
        switch(taskType){
            case LanguageTaskType.WORD_REPETITION:
                taskData = createWordRepetitionTask(userId,languageId);
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


    /*WordRepetitionTask*/
    public List<UserWordProgress> generateWordsList(Long userId,Long languageId){
       UserLanguage userLanguage = userLanguageRepo.findByLanguageIdAndUserId(languageId,userId).orElse(null);
       if(userLanguage == null)
           throw new ApiException(UserLanguageError.USER_NOT_LEARNING_LANGUAGE,"User not learning specified language");

       List<UserWordProgress> words = userWordProgressRepo.find10MostNeededRepetition(userLanguage.getId());
       return words;
    }

    private WordRepetitionData createWordRepetitionTask(Long userId,Long languageId){
        WordRepetitionData wordRepetitionData = new WordRepetitionData();
        List<WordRepetitionItem> wordRepetitionItemList = new ArrayList<>();

        List<UserWordProgress> wordsList = generateWordsList(userId,languageId);
        List<UserWordProgress> shuffled = new ArrayList<>(wordsList);
        wordsList.forEach(userWordProgress->{
            List<WordRepetitionAnswer> repetitionAnswers = new ArrayList<>();
            repetitionAnswers.add(new WordRepetitionAnswer(userWordProgress.getWord().getTranslation(),true));
            while(repetitionAnswers.size() < 4){
                String badAnswer = null;
                while(badAnswer == null) {
                    Collections.shuffle(shuffled);
                    String newPossiblyAnswer = shuffled.get(0).getWord().getTranslation();
                    boolean exists = repetitionAnswers.stream()
                            .anyMatch(a -> a.getAnswer().equals(newPossiblyAnswer));
                    if(!exists)
                        badAnswer = shuffled.get(0).getWord().getTranslation();
                }
                repetitionAnswers.add(new WordRepetitionAnswer(badAnswer,false));
            }
            Collections.shuffle(repetitionAnswers);
            WordRepetitionItem wordRepetitionItem = new WordRepetitionItem(userWordProgress.getWord().getWord(),userWordProgress.getId(),repetitionAnswers);
            wordRepetitionItemList.add(wordRepetitionItem);
        });
        wordRepetitionData.setItemList(wordRepetitionItemList);
        wordRepetitionData.setCurrentProgression(0L);

        return wordRepetitionData;
    }

    public WordRepetitionAnswerResponseDTO userAnswerOnWordRepetitionTask(UserAnswerToWordRepetitionTaskCommand command) {
        LanguageTask languageTask = getTask(command.getTaskId()).orElse(null);
        if(languageTask == null)
            throw new ApiException(TaskError.TASK_NOT_EXIST,String.format("Couldn't find task with id = '%s'",command.getTaskId()));
        if(languageTask.getUserId() != jwtUtil.getUserAuthInfo().getId())
            throw new ApiException(TaskError.TASK_INCORRECT_USER,"Current user is incorrect for this task");

        WordRepetitionData wordRepetitionData = (WordRepetitionData)languageTask.getTaskData();
        System.out.println(wordRepetitionData.getItemList().size());
        if(command.getTaskItemId() >= wordRepetitionData.getItemList().size()  || command.getTaskItemId() < 0 )
            throw new ApiException(TaskError.TASK_INCORRECT_ITEM_ID,"taskItemId could not be found");

        WordRepetitionItem taskItem = wordRepetitionData.getItemList().get(Math.toIntExact(command.getTaskItemId()));
        if(command.getAnswerId() >= taskItem.getPossibleAnswers().size() || command.getAnswerId()<0)
            throw new ApiException(TaskError.TASK_INCORRECT_ANSWER_ID, "answerId could not be found");



        UserWordProgress userWordProgress = userService.getUserLanguageWord(languageTask.getUserId(), languageTask.getLanguageId(),taskItem.getWordId() );

        if(taskItem.getPossibleAnswers().get(Math.toIntExact(command.getAnswerId())).isTrueAnswer()){
            //CorrectAnswer
            userWordProgress.setNextRepetitionDate(Instant.now().plus(Duration.ofDays(1)));
            userWordProgressRepo.save(userWordProgress);
        }else{
            //IncorrectAnswer
        }

        WordRepetitionAnswerResponseDTO responseDTO = new WordRepetitionAnswerResponseDTO();
        responseDTO.setUserAnswerId(command.getAnswerId());
        int correctAnswerId = IntStream.range(0, taskItem.getPossibleAnswers().size())
                .filter(i -> taskItem.getPossibleAnswers().get(i).isTrueAnswer())
                .findFirst()
                .orElse(-1);
        responseDTO.setCorrectAnswerId((long) correctAnswerId);
        return responseDTO;
    }

}
