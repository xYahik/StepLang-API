package com.example.steplang.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class LevelingLogDTO {
    private Long previousLevel;
    private Long previousExperience;
    private Long currentLevel;
    private Long currentExperience;
    private Instant date;
    private Long expToPreviousLevel;
    private Long expToCurrentLevel;
}
