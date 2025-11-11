package com.example.steplang.services.task;

import com.example.steplang.entities.language.UserLanguage;
import com.example.steplang.errors.UserLanguageError;
import com.example.steplang.exceptions.ApiException;
import com.example.steplang.model.task.arrangewords.ArrangeWordsData;
import com.example.steplang.model.task.arrangewords.ArrangeWordsDataItem;
import com.example.steplang.repositories.language.UserLanguageRepository;
import com.example.steplang.repositories.language.UserWordProgressRepository;
import com.example.steplang.utils.ai.AIAgent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ArrangeWordsTaskService {

    private final UserLanguageRepository userLanguageRepo;
    private final UserWordProgressRepository userWordProgressRepo;
    private final AIAgent aiAgent;
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

        arrangeWordsData.setTasks(arrangeWordsDataItems);
        return arrangeWordsData;

    }

    private String buildAIPrompt(List<String> words){
        return String.format(
                """
                   You are an AI language teaching assistant. Your task is to create a JSON describing a word‑order exercise for learning Czech.
                   The user will receive English instructions, but the words and sentences must be in Czech.
                   Input words (Czech): %s
                   
                   1. Instruction must be in English: Arrange the words to form a correct Czech sentence.
                   2. Use mainly words from the Czech input list; extra words allowed, not all words required.
                   3. Add 2–4 extra Czech words not needed for the sentence.
                   4. Generate all grammatically correct Czech sentences that preserve the meaning of the base sentence. Include possible variants such as:
                      - Natural changes in word order (only if the result sounds natural in Czech)
                      - Using different forms of negation
                      - Optional omission of the personal pronoun if it is naturally implied in Czech
                      - Optional particles or stylistic alternatives
                   Do not mix pronouns that refer to different persons in one sentence.
                   Do not create unnatural or ungrammatical word orders.
                   Ensure full grammatical agreement between subject and verb (person, number, gender).
                   Output the results as a list of sentences, no explanations needed.
                   5. Shuffle all words (source extra) in a list.
                   6. Base sentence is a sentence in english that need to be translated with words to Czech. All words in all_words_shuffeled should have been conjugated to adjust to final translated sentence.
                   7. source_words must include only words actually used in the sentence.
                   8. Return a JSON make 3-4 sentences and make JSON array which each element is in the exact specified format:
                   {
                     "instruction": "Arrange the words to form a correct Czech sentence.",
                     "source_words": [...],
                     "extra_words": [...],
                     "base_sentences": "...",
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
}
