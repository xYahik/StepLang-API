package com.example.steplang.dtos.task;

import lombok.Data;

import java.util.List;

@Data
public class WordRepetitionItemDTO {
    private String baseWord;
    private List<String> possibleAnswers;
}
