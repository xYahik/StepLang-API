package com.example.steplang.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class WordRepetitionAnswerResponseDTO {
    private Long userAnswerId;
    private Long correctAnswerId;
}
