package com.example.steplang.dtos.course;

import lombok.Data;

@Data
public class ChooseWordWithImageAnswerResponseDTO {
    private Boolean isCorrect;
    private Integer correctIndex;
}
