package com.example.steplang.entities.task.wordrepetition;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class WordRepetitionItem {
    private String baseWord;
    private Long wordId;
    private Boolean alreadyAnswered;
    private List<WordRepetitionAnswer> possibleAnswers;
}
