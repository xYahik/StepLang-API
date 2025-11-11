package com.example.steplang.model.task.arrangewords;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArrangeWordsDataItem {
    private String instruction;
    private List<String> source_words;
    private List<String> extra_words;
    private String base_sentence;
    private List<String> correct_sentences;
    private List<String> all_words_shuffled;
    private ArrangeWordsDataHints hints;

    private Boolean alreadyAnswered;
    private List<Long> userAnswerOrder;
}
