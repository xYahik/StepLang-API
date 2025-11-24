package com.example.steplang.services.task;

import com.example.steplang.commands.task.GetArrangeWordsTaskStatusCommand;
import com.example.steplang.commands.task.UserAnswerToArrangeWordsTaskCommand;
import com.example.steplang.dtos.task.ArrangeWordsAnswerResponseDTO;
import com.example.steplang.entities.language.UserLanguage;
import com.example.steplang.errors.TaskError;
import com.example.steplang.errors.UserLanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.model.task.LanguageTask;
import com.example.steplang.model.task.TaskReward;
import com.example.steplang.model.task.arrangewords.ArrangeWordsData;
import com.example.steplang.model.task.arrangewords.ArrangeWordsDataItem;
import com.example.steplang.model.task.arrangewords.ArrangeWordsStatusInfo;
import com.example.steplang.repositories.language.UserLanguageRepository;
import com.example.steplang.repositories.language.UserWordProgressRepository;
import com.example.steplang.repositories.task.LanguageTaskRepository;
import com.example.steplang.services.user.LevelingService;
import com.example.steplang.utils.JwtUtil;
import com.example.steplang.utils.ai.AIAgent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArrangeWordsTaskService {

    private final UserLanguageRepository userLanguageRepo;
    private final UserWordProgressRepository userWordProgressRepo;
    private final AIAgent aiAgent;
    private final LanguageTaskRepository languageTaskRepo;
    private final JwtUtil jwtUtil;
    private final LevelingService levelingService;

    public ArrangeWordsData createArrangeWordsTask(Long userId, Long languageId) {
        ArrangeWordsData arrangeWordsData = new ArrangeWordsData();
        List<ArrangeWordsDataItem> arrangeWordsDataItems = null;
        List<String> wordsList = generateWordsList(userId,languageId);
        String prompt = buildAIPrompt(wordsList);
        String responseJson = aiAgent.generateAITask(prompt);

        ObjectMapper mapper = new ObjectMapper();
        try{
            arrangeWordsDataItems = mapper.readValue(responseJson, new TypeReference<List<ArrangeWordsDataItem>>() {});
        }catch (Exception e){
            e.printStackTrace();
        }
        arrangeWordsDataItems.forEach(arrangeWordsDataItem->{
            arrangeWordsDataItem.setAlreadyAnswered(false);
        });
        arrangeWordsData.setTasks(arrangeWordsDataItems);
        arrangeWordsData.setCurrentProgression(0L);
        arrangeWordsData.setCorrectlyAnswered(0L);
        return arrangeWordsData;

    }

    private String buildAIPrompt(List<String> words){
        return String.format(
                """
                   You are an AI language teaching assistant. Your task is to create a JSON describing a word‑order exercise for learning Czech.
                   The user will receive English instructions, but the words and sentences must be in Czech.
                   Input words (Czech): %s
                   
                   1. Instruction must be in English: Arrange the words to form a correct Czech sentence.
                   2. Use only words from the Czech input list; extra words allowed, not all words required.
                   3. Add 2–4 extra Czech words not needed for the sentence.
                   4. Generate all grammatically correct Czech sentences that preserve the meaning of the base sentence. Include possible variants such as:
                      - Natural changes in word order (only if the result sounds natural in Czech)
                      - Using different forms of negation
                      - Optional omission of the personal pronoun if it is naturally implied in Czech
                      - Optional particles or stylistic alternatives
                   If the user-facing word list (all_words_shuffled) contains separate tokens that should form a single inflected or compound word in the target language (e.g. a negation particle with a verb, or any other bound morpheme combination), merge them into one grammatically correct token before presenting it to the user.
                   Do not mix pronouns that refer to different persons in one sentence.
                   Do not create unnatural or ungrammatical word orders.
                   Ensure full grammatical agreement between subject and verb (person, number, gender).
                   If multiple grammatically correct variants exist but some change the emphasis or slightly alter the meaning (e.g. unusual word order), include only the most natural one(s) that best preserve the meaning of the base sentence.
                   Optionally, add a short note in "hints.note" explaining that other word orders are possible in Czech but may sound contrastive or emphasize a different part of the sentence.
                   Output the results as a list of sentences, no explanations needed.
                   5. Shuffle all words (source extra) in a list.
                   6. Base sentence is a sentence in english that need to be translated with words to Czech. All words in all_words_shuffeled should have been conjugated to adjust to final translated sentence.
                   7. source_words must include only words actually used in the sentence.
                   8. Return a JSON make 3-4 sentences and make JSON array which each element is in the exact specified format:
                   {
                     "instruction": "Arrange the words to form a correct Czech sentence.",
                     "source_words": [...],
                     "extra_words": [...],
                     "base_sentence": "...",
                     "correct_sentences": [...],
                     "all_words_shuffled": [...],
                     "hints": {
                       "level": "...",
                       "grammar_focus": "...",
                       "note": "..."
                     }
                   }
                   Respond ONLY with JSON — no explanations.
                """,
                words.toString()
        );
    }
    private List<String> generateWordsList(Long userId, Long languageId){
        UserLanguage userLanguage = userLanguageRepo.findByLanguageIdAndUserId(languageId,userId).orElse(null);
        if(userLanguage == null)
            throw new ApiException(UserLanguageError.USER_NOT_LEARNING_LANGUAGE,"User not learning specified language");

        List<String> words = userWordProgressRepo.findAllWordsByUserLanguage(userLanguage.getId());
        return words;
    }

    public ArrangeWordsAnswerResponseDTO userAnswerArrangeWordsTask(UserAnswerToArrangeWordsTaskCommand command) {
        LanguageTask languageTask = languageTaskRepo.findById(command.getTaskId()).orElse(null);
        if(languageTask == null)
            throw new ApiException(TaskError.TASK_NOT_EXIST,String.format("Couldn't find task with id = '%s'",command.getTaskId()));
        if(languageTask.getUserId() != jwtUtil.getUserAuthInfo().getId())
            throw new ApiException(TaskError.TASK_INCORRECT_USER,"Current user is incorrect for this task");

        ArrangeWordsData arrangeWordsData = (ArrangeWordsData)languageTask.getTaskData();

        if(command.getTaskItemId() >= arrangeWordsData.getTasks().size()  || command.getTaskItemId() < 0 )
            throw new ApiException(TaskError.TASK_INCORRECT_ITEM_ID,"taskItemId could not be found");

        ArrangeWordsDataItem taskItem = arrangeWordsData.getTasks().get(Math.toIntExact(command.getTaskItemId()));
        if(taskItem.getAlreadyAnswered())
            throw new ApiException(TaskError.TASK_ALREADY_ANSWERED, "TaskItem already got answered");

        taskItem.setUserAnswerOrder(command.getAnswerIdsOrder());

        //Prepare lists to check
        List<String> wordsList = taskItem.getAll_words_shuffled()
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        List<List<Long>> correctAnswers = new ArrayList<>();
        taskItem.getCorrect_sentences().forEach(sentence ->{
            if(sentence.endsWith(".") || sentence.endsWith("?") || sentence.endsWith("!"))
                sentence = sentence.substring(0,sentence.length()-1);

            List<String> sentenceSplit = Arrays.stream(sentence.split(" ")).toList();
            List<Long> answerOrderIds = new ArrayList<>();

            sentenceSplit.forEach(word ->{
                answerOrderIds.add((long) wordsList.indexOf(word.toLowerCase()));
            });

            correctAnswers.add(answerOrderIds);
        });

        ArrangeWordsAnswerResponseDTO arrangeWordsAnswerResponseDTO = new ArrangeWordsAnswerResponseDTO();
        arrangeWordsAnswerResponseDTO.setCorrectAnswers(taskItem.getCorrect_sentences());
        if(correctAnswers.contains(taskItem.getUserAnswerOrder())) {
            arrangeWordsData.setCorrectlyAnswered(arrangeWordsData.getCorrectlyAnswered()+1);
            arrangeWordsAnswerResponseDTO.setIsCorrectAnswer(true);
        }
        else {
            arrangeWordsAnswerResponseDTO.setIsCorrectAnswer(false);
        }
        arrangeWordsData.setCurrentProgression(arrangeWordsData.getCurrentProgression()+1);
        taskItem.setAlreadyAnswered(true);

        if(isTaskCompleted(arrangeWordsData)){
            levelingService.AddUserLanguageXP(languageTask.getUserId(),languageTask.getLanguageId(),calculateArrangeWordsTaskReward(arrangeWordsData).getEarnedExp());
        }

        languageTaskRepo.save(languageTask);

        return arrangeWordsAnswerResponseDTO;
    }

    public ArrangeWordsStatusInfo getArrangeWordsTaskStatus(String taskId) {
        ArrangeWordsData arrangeWordsData = getArrangeWordsDataFromTask(taskId);

        ArrangeWordsStatusInfo currentTaskStatus = new ArrangeWordsStatusInfo(
                arrangeWordsData.getCurrentProgression(),
                (long)arrangeWordsData.getTasks().size(),
                isTaskCompleted(arrangeWordsData)
        );

        return currentTaskStatus;
    }

    private Boolean isTaskCompleted(ArrangeWordsData arrangeWordsData) {
        return arrangeWordsData.getCurrentProgression() == arrangeWordsData.getTasks().size();
    }

    private ArrangeWordsData getArrangeWordsDataFromTask(String taskId) {
        LanguageTask languageTask = languageTaskRepo.findById(taskId).orElse(null);
        if(languageTask == null)
            throw new ApiException(TaskError.TASK_NOT_EXIST,String.format("Couldn't find task with id = '%s'",taskId));
        if(languageTask.getUserId() != jwtUtil.getUserAuthInfo().getId())
            throw new ApiException(TaskError.TASK_INCORRECT_USER,"Current user is incorrect for this task");

        ArrangeWordsData arrangeWordsData = (ArrangeWordsData)languageTask.getTaskData();

        return arrangeWordsData;
    }

    private TaskReward calculateArrangeWordsTaskReward(ArrangeWordsData arrangeWordsData) {
        return new TaskReward(arrangeWordsData.getCorrectlyAnswered(),true);
    }
}
