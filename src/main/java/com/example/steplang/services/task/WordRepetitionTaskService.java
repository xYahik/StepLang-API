package com.example.steplang.services.task;

import com.example.steplang.commands.task.GetWordRepetitionTaskCommand;
import com.example.steplang.commands.task.UserAnswerToWordRepetitionTaskCommand;
import com.example.steplang.dtos.task.WordRepetitionAnswerResponseDTO;
import com.example.steplang.entities.language.UserLanguage;
import com.example.steplang.entities.language.UserWordProgress;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.model.task.wordrepetition.WordRepetitionAnswer;
import com.example.steplang.model.task.wordrepetition.WordRepetitionData;
import com.example.steplang.model.task.wordrepetition.WordRepetitionItem;
import com.example.steplang.errors.TaskError;
import com.example.steplang.errors.UserLanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.model.task.TaskReward;
import com.example.steplang.model.task.WordRepetitionStatusInfo;
import com.example.steplang.repositories.language.UserLanguageRepository;
import com.example.steplang.repositories.language.UserWordProgressRepository;
import com.example.steplang.repositories.task.LanguageTaskRepository;
import com.example.steplang.services.UserService;
import com.example.steplang.utils.JwtUtil;
import com.example.steplang.utils.enums.UnderstandingLevel;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class WordRepetitionTaskService {

    private final UserWordProgressRepository userWordProgressRepo;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserLanguageRepository userLanguageRepo;
    private final LanguageTaskRepository languageTaskRepo;

    public WordRepetitionData createWordRepetitionTask(Long userId, Long languageId){
        WordRepetitionData wordRepetitionData = new WordRepetitionData();
        wordRepetitionData.setCorrectlyAnswered(0L);
        List<WordRepetitionItem> wordRepetitionItemList = new ArrayList<>();

        List<UserWordProgress> wordsList = generateWordsList(userId,languageId);
        List<UserWordProgress> shuffled = new ArrayList<>(wordsList);
        wordsList.forEach(userWordProgress->{
            List<WordRepetitionAnswer> repetitionAnswers = new ArrayList<>();
            repetitionAnswers.add(new WordRepetitionAnswer(userWordProgress.getWord().getTranslation(),true));
            int extraBadAnswers = Math.min(wordsList.size(), 4);
            while(repetitionAnswers.size() < extraBadAnswers){
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
            WordRepetitionItem wordRepetitionItem = new WordRepetitionItem(userWordProgress.getWord().getWord(),userWordProgress.getId(),false,repetitionAnswers);
            wordRepetitionItemList.add(wordRepetitionItem);
        });
        wordRepetitionData.setItemList(wordRepetitionItemList);
        wordRepetitionData.setCurrentProgression(0L);

        return wordRepetitionData;
    }

    @Transactional
    public WordRepetitionAnswerResponseDTO userAnswerOnWordRepetitionTask(UserAnswerToWordRepetitionTaskCommand command) {
        LanguageTask languageTask = languageTaskRepo.findById(command.getTaskId()).orElse(null);
        if(languageTask == null)
            throw new ApiException(TaskError.TASK_NOT_EXIST,String.format("Couldn't find task with id = '%s'",command.getTaskId()));
        if(languageTask.getUserId() != jwtUtil.getUserAuthInfo().getId())
            throw new ApiException(TaskError.TASK_INCORRECT_USER,"Current user is incorrect for this task");

        WordRepetitionData wordRepetitionData = (WordRepetitionData)languageTask.getTaskData();

        if(command.getTaskItemId() >= wordRepetitionData.getItemList().size()  || command.getTaskItemId() < 0 )
            throw new ApiException(TaskError.TASK_INCORRECT_ITEM_ID,"taskItemId could not be found");

        WordRepetitionItem taskItem = wordRepetitionData.getItemList().get(Math.toIntExact(command.getTaskItemId()));
        if(taskItem.getAlreadyAnswered())
            throw new ApiException(TaskError.TASK_ALREADY_ANSWERED, "TaskItem already got answered");

        if(command.getAnswerId() >= taskItem.getPossibleAnswers().size() || command.getAnswerId()<0)
            throw new ApiException(TaskError.TASK_INCORRECT_ANSWER_ID, "answerId could not be found");



        UserWordProgress userWordProgress = userService.getUserLanguageWord(languageTask.getUserId(), languageTask.getLanguageId(),taskItem.getWordId() );

        if(taskItem.getPossibleAnswers().get(Math.toIntExact(command.getAnswerId())).isTrueAnswer()){
            //CorrectAnswer
            changeWordProgress(userWordProgress,true);
            userWordProgress.setNextRepetitionDate(calculateNextRepetition(userWordProgress));
            wordRepetitionData.setCorrectlyAnswered(wordRepetitionData.getCorrectlyAnswered()+1);
        }else{
            //IncorrectAnswer
            changeWordProgress(userWordProgress,false);
        }

        wordRepetitionData.setCurrentProgression(wordRepetitionData.getCurrentProgression()+1);
        taskItem.setAlreadyAnswered(true);
        languageTaskRepo.save(languageTask);
        userWordProgressRepo.save(userWordProgress);

        WordRepetitionAnswerResponseDTO responseDTO = new WordRepetitionAnswerResponseDTO();
        responseDTO.setUserAnswerId(command.getAnswerId());
        int correctAnswerId = IntStream.range(0, taskItem.getPossibleAnswers().size())
                .filter(i -> taskItem.getPossibleAnswers().get(i).isTrueAnswer())
                .findFirst()
                .orElse(-1);
        responseDTO.setCorrectAnswerId((long) correctAnswerId);
        return responseDTO;
    }

    private List<UserWordProgress> generateWordsList(Long userId, Long languageId){
        UserLanguage userLanguage = userLanguageRepo.findByLanguageIdAndUserId(languageId,userId).orElse(null);
        if(userLanguage == null)
            throw new ApiException(UserLanguageError.USER_NOT_LEARNING_LANGUAGE,"User not learning specified language");


        List<UserWordProgress> words = userWordProgressRepo.find10MostNeededRepetition(userLanguage.getId());
        return words;
    }

    private void changeWordProgress(UserWordProgress userWordProgress, boolean wasCorrectAnswer){
        Long currentWordProgressValue = recalculateProgressValueToNumeric(userWordProgress.getUnderstandingLevel(),userWordProgress.getUnderstandingProgress());

        if(wasCorrectAnswer){
            currentWordProgressValue = currentWordProgressValue + 5;
        }else{
            currentWordProgressValue = currentWordProgressValue - 8;
        }

        UnderstandingLevel newUnderstandingLevel;

        long newUnderstandingProgress = currentWordProgressValue % 100;
        long understandingLevelValue = currentWordProgressValue / 100;

        switch ((int) understandingLevelValue) {
            case 0 -> newUnderstandingLevel = UnderstandingLevel.NEW;
            case 1 -> {newUnderstandingLevel = UnderstandingLevel.LEARNING; }
            case 2 -> {newUnderstandingLevel = UnderstandingLevel.ALMOST_KNOWN; }
            case 3 -> {newUnderstandingLevel = UnderstandingLevel.KNOWN;}
            default -> newUnderstandingLevel = UnderstandingLevel.NEW;
        }
        if(newUnderstandingProgress < 0){
            newUnderstandingProgress = 0;

        }else if (newUnderstandingProgress >= 100) {
            newUnderstandingProgress = newUnderstandingProgress - 100;
        }

        userWordProgress.setUnderstandingLevel(newUnderstandingLevel);
        userWordProgress.setUnderstandingProgress(newUnderstandingProgress);
    }

    private Long recalculateProgressValueToNumeric(UnderstandingLevel understandingLevel, Long understandingProgress){
        long understandingLevelValue;
        switch(understandingLevel){
            case UnderstandingLevel.NEW -> understandingLevelValue = 0L;
            case UnderstandingLevel.LEARNING -> understandingLevelValue = 1L;
            case UnderstandingLevel.ALMOST_KNOWN -> understandingLevelValue = 2L;
            case UnderstandingLevel.KNOWN -> understandingLevelValue = 3L;
            default -> understandingLevelValue = 0L;
        }
        //0-399
        return understandingLevelValue * 100 + understandingProgress;
    }


    /*
    Currently we're just adding extra hours based on Numeric value of understanding progress.
    Need future upgrade to make more advanced calculating for next repetition, current is just temporary.
    */
    private Instant calculateNextRepetition(UserWordProgress userWordProgress){

        return Instant.now().plus(
                Duration.ofHours(
                    recalculateProgressValueToNumeric(
                            userWordProgress.getUnderstandingLevel(),
                            userWordProgress.getUnderstandingProgress()
                    )
        ));
    }

    public WordRepetitionStatusInfo getWordRepetitionTaskStatus(GetWordRepetitionTaskCommand command) {
        WordRepetitionData wordRepetitionData = getWordRepetitionDataFromTask(command.getTaskId());

        WordRepetitionStatusInfo currentTaskStatus = new WordRepetitionStatusInfo(
                wordRepetitionData.getCurrentProgression(),
                (long)wordRepetitionData.getItemList().size(),
                isTaskCompleted(wordRepetitionData)
                );

        return currentTaskStatus;
    }

    public TaskReward calculateWordRepetitionTaskReward(WordRepetitionData wordRepetitionData){
        return new TaskReward(wordRepetitionData.getCorrectlyAnswered(),true);
    }

    public boolean isTaskCompleted(WordRepetitionData wordRepetitionData){
        return wordRepetitionData.getCurrentProgression() == wordRepetitionData.getItemList().size();
    }

    public TaskReward getWordRepetitionTaskReward(GetWordRepetitionTaskCommand command) {
        WordRepetitionData wordRepetitionData = getWordRepetitionDataFromTask(command.getTaskId());
        return calculateWordRepetitionTaskReward(wordRepetitionData);
    }

    private WordRepetitionData getWordRepetitionDataFromTask(String taskId){
    LanguageTask languageTask = languageTaskRepo.findById(taskId).orElse(null);
    if(languageTask == null)
        throw new ApiException(TaskError.TASK_NOT_EXIST,String.format("Couldn't find task with id = '%s'",taskId));
    if(languageTask.getUserId() != jwtUtil.getUserAuthInfo().getId())
        throw new ApiException(TaskError.TASK_INCORRECT_USER,"Current user is incorrect for this task");

    return (WordRepetitionData)languageTask.getTaskData();
    }

}
