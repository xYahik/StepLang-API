package com.example.steplang.dtos.task;

import lombok.Data;
import java.util.List;

@Data
public class ArrangeWordsAnswerResponseDTO {
    private Boolean isCorrectAnswer;
    private List<String> correctAnswers;
}
