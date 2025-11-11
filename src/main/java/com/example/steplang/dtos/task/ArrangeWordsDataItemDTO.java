package com.example.steplang.dtos.task;

import lombok.Data;

import java.util.List;

@Data
public class ArrangeWordsDataItemDTO {
    private String instruction;
    private String base_sentences;
    private List<String> all_words_shuffled;
}
